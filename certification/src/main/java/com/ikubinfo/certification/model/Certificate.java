package com.ikubinfo.certification.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "certificates")
public class Certificate implements Serializable{
	

	private static final long serialVersionUID = 5305566387492793877L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, length = 11)
	private int id;
	
	@Column(name = "title", nullable = false, length = 200)
	private String title;
	
	@Column(name = "description", nullable = false, length = 5000)
	private String description;
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name = "technology_id", nullable = false)
	private Technology technology;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Technology getTechnology() {
		return technology;
	}
	public void setTechnology(Technology technology) {
		this.technology = technology;
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
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((technology == null) ? 0 : technology.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Certificate other = (Certificate) obj;
		if (deleted != other.deleted)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (technology == null) {
			if (other.technology != null)
				return false;
		} else if (!technology.equals(other.technology))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Certificate [id=" + id + ", title=" + title + ", description=" + description + ", technology="
				+ technology + ", deleted=" + deleted + "]";
	}
	/**
	 * @param title
	 * @param description
	 * @param technology
	 * @param deleted
	 */
	public Certificate(String title, String description, Technology technology, boolean deleted) {
		super();
		this.title = title;
		this.description = description;
		this.technology = technology;
		this.deleted = deleted;
	}
	/**
	 * 
	 */
	public Certificate() {
		super();
	}
	
	
}
