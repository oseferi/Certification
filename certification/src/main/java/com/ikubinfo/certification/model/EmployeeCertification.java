package com.ikubinfo.certification.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="employee_certifications",uniqueConstraints=
@UniqueConstraint(columnNames={"user_id", "certificate_id"})
)
public class EmployeeCertification implements Serializable{

	private static final long serialVersionUID = 8621206485256765828L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable = false)
	private int id;
	
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "certificate_id", nullable = false)
	private Certificate certificate;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_assigned")
	private Date dateAssigned;
	
	@Column(name="status", nullable = true)
	private Boolean status;
	
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
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@PrePersist
	  protected void onCreate() {
		dateAssigned = new Date();
	  }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((certificate == null) ? 0 : certificate.hashCode());
		result = prime * result + id;
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
		if (id != other.id)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	public EmployeeCertification() {
		super();
	}
	public EmployeeCertification(User user, Certificate certificate, Date dateAssigned, boolean status, int score) {
		super();
		this.user = user;
		this.certificate = certificate;
		this.dateAssigned = dateAssigned;
		this.status = status;
		this.score = score;
	}
	@Override
	public String toString() {
		return "EmployeeCertification [id=" + id + ", user=" + user + ", certificate=" + certificate + ", dateAssigned="
				+ dateAssigned + ", status=" + status + ", score=" + score + ", deleted=" + deleted + "]";
	}
	
	
	
}
