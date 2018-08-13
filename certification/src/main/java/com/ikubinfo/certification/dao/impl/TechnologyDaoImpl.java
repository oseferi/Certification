package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ikubinfo.certification.dao.TechnologyDao;
import com.ikubinfo.certification.model.Technology;

@Repository(value = "TechnologyDao")
@Scope("singleton")
@Component
public class TechnologyDaoImpl implements TechnologyDao {
	private static Logger log = Logger.getLogger(UserDaoImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Technology> getAllActive() {
		try {
			return (ArrayList<Technology>) entityManager
					.createQuery("SELECT technology FROM Technology technology WHERE technology.deleted=0")
					.getResultList();
		} catch (Exception e) {
			log.warn("Technologies could not be retrieved! Error message :" + e.getMessage());
			return null;
		}
	}

	@Override
	public Technology find(int id) {
		try {
			return entityManager.find(Technology.class, id);
		} catch (Exception e) {
			log.error("Technology cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

}
