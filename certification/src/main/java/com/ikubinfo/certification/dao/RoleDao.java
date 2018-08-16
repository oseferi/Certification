package com.ikubinfo.certification.dao;

import java.util.ArrayList;
import com.ikubinfo.certification.model.Role;

public interface RoleDao {

	public Role findById(int id);
	public ArrayList<Role> getAll();
	
}
