package br.faculdadedelta.util;

import static org.junit.Assert.*;
import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.edu.faculdadedelta.util.JPAUtil;

public class JPAUtilTeste {

	private EntityManager em;
	
	@Test
	public void deveTerInstanciaDoEntityManager(){
		assertNotNull("deve ter instanciado o entity manager ", em);
	}
	@Test
	public void deveFecharEntityManager(){
		em.close();
		assertNotNull("EntityManager deve estar fechado ", em.isOpen());
	}
	
	@Test
	public void deveAbrirUmaTransacao(){
		assertFalse("transacao deve estar fechada ", em.getTransaction().isActive());
		em.getTransaction().begin();
		
		assertTrue("transacao deve estar aberta ", em.getTransaction().isActive());
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
}
