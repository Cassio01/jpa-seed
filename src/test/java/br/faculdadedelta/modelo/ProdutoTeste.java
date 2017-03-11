package br.faculdadedelta.modelo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import br.edu.faculdadedelta.modelo.Produto;
import br.edu.faculdadedelta.util.JPAUtil;

public class ProdutoTeste {

	private EntityManager em;

	@Test
	public void deveExcluirProduto() {
		deveSalvarProduto();

		TypedQuery<Long> query = em.createQuery(
				"SELECT MAX(p.id) FROM Produto p", Long.class);
		Long id = query.getSingleResult();
		em.getTransaction().begin();

		Produto produto = em.find(Produto.class, id);
		em.remove(produto);
		em.getTransaction().commit();

		Produto produtoExcluido = em.find(Produto.class, id);
		assertNull("não deve achar o produto excluído ", produtoExcluido);

	}

	@Test
	public void deveAlterarProduto() {
		deveSalvarProduto();

		TypedQuery<Produto> query = em.createQuery("SELECT p FROM Produto p ",
				Produto.class).setMaxResults(1);

		Produto produto = query.getSingleResult();

		Integer versao = produto.getVersion();

		em.getTransaction().begin();
		produto.setFabricante("HP");
		produto = em.merge(produto);
		em.getTransaction().commit();

		assertNotEquals("Versao deve ser diferente", versao,
				produto.getVersion());
	}

	@Test
	public void devePesquisarProdutos() {
		for (int i = 0; i <= 10; i++) {
			deveSalvarProduto();
		}

		TypedQuery<Produto> query = em.createQuery("SELECT p FROM Produto p ",
				Produto.class);
		List<Produto> produtos = query.getResultList();

		assertFalse("deve ter itens da lista ", produtos.isEmpty());
		assertTrue("deve ter pelo menos 10 itens", produtos.size() >= 10);
	}

	@Test
	public void deveSalvarProduto() {
		Produto produto = new Produto();
		produto.setNome("NoteBook");
		produto.setFabricante("Dell");

		assertTrue("entidade não tem id", produto.isTransient());

		em.getTransaction().begin();
		em.persist(produto);
		em.getTransaction().commit();

		assertFalse("entidade agora tem id", produto.isTransient());
	}

	@Before
	public void instanciarEntityManager() {
		em = JPAUtil.INSTANCE.getEntityManager();
	}

	@After
	public void fecharEntityManager() {
		if (em.isOpen()) {
			em.close();
		}
	}

	@AfterClass
	public static void deveLimparBase() {
		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();

		entityManager.getTransaction().begin();

		Query query = entityManager.createQuery("DELETE FROM Produto p");
		int registrosExcluidos = query.executeUpdate();

		entityManager.getTransaction().commit();

		assertTrue("deve ter ecluído registros", registrosExcluidos > 0);
	}
}