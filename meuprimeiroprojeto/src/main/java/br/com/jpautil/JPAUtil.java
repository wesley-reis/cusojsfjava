package br.com.jpautil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Named
@ApplicationScoped
public class JPAUtil {
	
	private EntityManagerFactory factory = null;
	
	public JPAUtil() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("meuprimeiroprojeto");
		}
	}
	
	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

	
	public Object getPrimaryKey(Object entidade) {
		return factory.getPersistenceUnitUtil().getIdentifier(entidade);
	}
}
