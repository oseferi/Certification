package com.ikubinfo.certification.service;

import java.util.ArrayList;

import com.ikubinfo.certification.model.Role;

public interface RoleService {
	public boolean add(Role role);
	public boolean remove(Role role);
	public boolean update(Role role);
	
	public Role findById(int id);
	public ArrayList<Role> getAll();
}
