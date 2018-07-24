package com.ikubinfo.certification.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class EmployeeCertification implements Serializable{

	@Id
	@Column(name="user_id", nullable = false)
	private User user;
	
	@Id
	@Column(name="certificate_id", nullable = false)
	private Certificate certificate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_assigned", insertable = false)
	private Date dateAssigned;
	
	@Column(name="status")
	private boolean status;
	
	@Column(name = "score", length = 11)
	private int score;
	
	@Column(name = "deleted", nullable = true)
	private boolean deleted;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	public Date getDateAssigned() {
		return dateAssigned;
	}
	public void setDateAssigned(Date dateAssigned) {
		this.dateAssigned = dateAssigned;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((certificate == null) ? 0 : certificate.hashCode());
		result = prime * result + ((dateAssigned == null) ? 0 : dateAssigned.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + score;
		result = prime * result + (status ? 1231 : 1237);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeCertification other = (EmployeeCertification) obj;
		if (certificate == null) {
			if (other.certificate != null)
				return false;
		} else if (!certificate.equals(other.certificate))
			return false;
		if (dateAssigned == null) {
			if (other.dateAssigned != null)
				return false;
		} else if (!dateAssigned.equals(other.dateAssigned))
			return false;
		if (deleted != other.deleted)
			return false;
		if (score != other.score)
			return false;
		if (status != other.status)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	public EmployeeCertification(User user, Certificate certificate, Date dateAssigned, boolean status, int score,
			boolean deleted) {
		super();
		this.user = user;
		this.certificate = certificate;
		this.dateAssigned = dateAssigned;
		this.status = status;
		this.score = score;
		this.deleted = deleted;
	}
	
	public EmployeeCertification () {};
}
