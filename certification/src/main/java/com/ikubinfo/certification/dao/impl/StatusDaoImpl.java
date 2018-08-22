package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.ikubinfo.certification.dao.StatusDao;
import com.ikubinfo.certification.model.Status;

@Repository(value = "StatusDao")
public class StatusDaoImpl implements StatusDao {
	private static Logger log = Logger.getLogger(StatusDaoImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Status findById(int id) {
		try {
			return entityManager.find(Status.class, id);
		} catch (Exception e) {
			log.error("Role cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@Override
	public ArrayList<Status> getAllActive() {
		try{
			return (ArrayList<Status>)entityManager
				.createQuery("Select status From Status status  Where status.deleted=0",Status.class)
				.getResultList();
		}
		catch (NoResultException e) {
			log.warn("Statuses could not be retrieved! Error message :" + e.getMessage());
			return null;			
		}
	}

}
