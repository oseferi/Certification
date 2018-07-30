package com.ikubinfo.certification.dao;

import java.util.ArrayList;

import com.ikubinfo.certification.model.Technology;

public interface TechnologyDao {
	public ArrayList<Technology> getAllActive();

	public Technology find(int id);
}
