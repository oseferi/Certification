package com.ikubinfo.certification.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.TechnologyDao;
import com.ikubinfo.certification.model.Technology;
import com.ikubinfo.certification.service.TechnologyService;

@Service("technologyService")
public class TechnologyServiceImpl implements TechnologyService {

	@Autowired
	TechnologyDao technologyDao;
	
	@Override
	public ArrayList<Technology> getAllActive() {
		return technologyDao.getAllActive();
	}
	
	@Override
	public Technology find(int id) {
		return technologyDao.find(id);
	}

}
