package br.faculdadedelta.modelo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import br.edu.faculdadedelta.modelo.Cliente;
import br.edu.faculdadedelta.util.JPAUtil;

public class ClienteTeste {

	private static final String CPF_PADRAO = "111.111.111-11";
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultarClienteComIdNome(){
		deveSalvarCliente();
		Query query = em.createQuery("SELECT new Cliente(c.id, c.nome) FROM Cliente c WHERE c.cpf =:cpf");
		query.setParameter("cpf", CPF_PADRAO);
		List<Cliente> clientes = query.getResultList();
		
		assertFalse("verifica se há registros na lista", clientes.isEmpty());
		
		for (Cliente cliente : clientes) {
			assertNull("verifica que o cpf deve está null", cliente.getCpf());
			cliente.setCpf(CPF_PADRAO);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultarCPF(){
		deveSalvarCliente();
		
		Query query = em.createQuery("SELECT c.cpf FROM Cliente c WHERE c.nome LIKE :nome ");
		query.setParameter("nome", "Cassio");
		List<String> cpfs = query.getResultList();
		assertFalse(cpfs.isEmpty());
	}
	
	@Test
	public void deveSalvarCliente(){
		Cliente cliente = new Cliente();
		cliente.setNome("Cassio");
		cliente.setCpf(CPF_PADRAO);

		assertTrue("entidade não tem id", cliente.isTransient());

		em.getTransaction().begin();
		em.persist(cliente);
		em.getTransaction().commit();

		assertFalse("entidade agora tem id", cliente.isTransient());
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

		Query query = entityManager.createQuery("DELETE FROM Cliente c");
		int registrosExcluidos = query.executeUpdate();

		entityManager.getTransaction().commit();

		assertTrue("deve ter ecluído registros", registrosExcluidos > 0);
	}
}