package com.ikubinfo.certification.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.hibernate.stat.SecondLevelCacheStatistics;

@ManagedBean
@ViewScoped
public class UserCertificationBean implements Serializable{

	private static Logger logger = Logger.getLogger(UserCertificationBean.class);
	private static final long serialVersionUID = 3992705742006284117L;
	
}
