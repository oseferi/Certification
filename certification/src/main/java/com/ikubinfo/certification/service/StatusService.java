package com.ikubinfo.certification.service;

import java.util.ArrayList;

import com.ikubinfo.certification.model.Status;

public interface StatusService {
	public Status findById(int id);
	public ArrayList<Status> getAllActive();
	public Status getOnProgressStasus();	
	public Status getFailedStatus();
	public Status getSuccessStatus();
}
