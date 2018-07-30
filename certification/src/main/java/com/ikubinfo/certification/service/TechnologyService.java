package com.ikubinfo.certification.service;

import java.util.ArrayList;

import com.ikubinfo.certification.model.Technology;

public interface TechnologyService {
	public ArrayList<Technology> getAllActive() ;

	public Technology find(int id);
}
