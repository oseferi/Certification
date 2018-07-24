package com.ikubinfo.certification.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ikubinfo.certification.model.Role;

public class test {

	public static void main(String[] args) {
		/*EntityManagerFactory emf = Persistence.createEntityManagerFactory("mnf-pu");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        Role role = new Role();
        role.setTitle("Admin");
        role.setDeleted(false);
        em.persist(role);
        em.getTransaction().commit();

        emf.close();
		
		try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, DBProperties.class);) {
			DBProperties dbProperties = context.getBean(DBProperties.class);
			
			System.out.println("This is dbProperties: " + dbProperties.toString());
		}

*/
	}

}
