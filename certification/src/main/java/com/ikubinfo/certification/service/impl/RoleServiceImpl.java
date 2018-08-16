package com.ikubinfo.certification.service.impl;

import java.io.Serializable;
import java.util.ArrayList;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.RoleDao;
import com.ikubinfo.certification.model.Role;
import com.ikubinfo.certification.service.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService, Serializable {
	
	private static final long serialVersionUID = -969765987579610857L;

	@Autowired
	RoleDao roleDao;
	

	@Override
	public Role findById(int id) {
		return roleDao.findById(id);
	}

	@Override
	public ArrayList<Role> getAll() {
		return roleDao.getAll();
	}

}
