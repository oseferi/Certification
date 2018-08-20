package com.ikubinfo.certification.dao;

import java.util.ArrayList;

import com.ikubinfo.certification.model.Status;


public interface StatusDao {
	public Status findById(int id);
	public ArrayList<Status> getAllActive();
}
