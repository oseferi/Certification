package com.ikubinfo.certification.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.RoleDao;
import com.ikubinfo.certification.model.Role;
import com.ikubinfo.certification.service.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDao roleDao;
	
	@Override
	public boolean add(Role role) {
		return roleDao.add(role);
	}

	@Override
	public boolean remove(Role role) {
		return roleDao.remove(role);
	}

	@Override
	public boolean update(Role role) {
		return roleDao.update(role);
	}

	@Override
	public Role findById(int id) {
		return roleDao.findById(id);
	}

	@Override
	public ArrayList<Role> getAll() {
		return roleDao.getAll();
	}

}
