package com.ikubinfo.certification.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikubinfo.certification.dao.StatusDao;
import com.ikubinfo.certification.model.Status;
import com.ikubinfo.certification.service.StatusService;

@Service("statusService")
public class StatusServiceImpl implements StatusService {

	@Autowired
	StatusDao statusDao;
	
	@Override
	public Status findById(int id) {
		return statusDao.findById(id);
	}

	@Override
	public ArrayList<Status> getAllActive() {
		return statusDao.getAllActive();
	}

	@Override
	public Status getOnProgressStasus() {
		return statusDao.findById(1);
	}

	@Override
	public Status getFailedStatus() {
		return statusDao.findById(2);
	}

	@Override
	public Status getSuccessStatus() {
		return statusDao.findById(3);
	}

	
	
	
}
