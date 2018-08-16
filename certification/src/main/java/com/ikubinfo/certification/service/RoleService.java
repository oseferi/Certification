package com.ikubinfo.certification.service;

import java.util.ArrayList;

import com.ikubinfo.certification.model.Role;

public interface RoleService {
	public Role findById(int id);
	public ArrayList<Role> getAll();
}
