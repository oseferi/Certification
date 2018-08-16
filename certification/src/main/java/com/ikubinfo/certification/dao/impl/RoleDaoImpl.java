package com.ikubinfo.certification.dao.impl;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.ikubinfo.certification.dao.RoleDao;
import com.ikubinfo.certification.model.Role;

@Repository(value = "RoleDao")
@Scope("singleton")
@Component
public class RoleDaoImpl implements RoleDao {
	private static Logger log = Logger.getLogger(RoleDaoImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Role findById(int id) {
		try {
			return entityManager.find(Role.class, id);
		} catch (Exception e) {
			log.error("Role cannot be found! Error message :" + e.getMessage());
			return null;
		}
	}

	@Override
	public ArrayList<Role> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
