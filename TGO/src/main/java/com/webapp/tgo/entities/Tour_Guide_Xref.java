package com.webapp.tgo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tour_guide_xref")
public class Tour_Guide_Xref implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;
	@ManyToOne
	@JoinColumn(name = "guideid", referencedColumnName = "id")
	private Guide guide;
	@ManyToOne
	@JoinColumn(name = "tourid", referencedColumnName = "id")
	private Tour tour;
	
	@Column(name="status")
	private int status;
	
	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name = "updatedDate")
	private Date updatedDate; 
	

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Tour_Guide_Xref(int id, Guide guide, Tour tour, int status, Date createdDate) {
		super();
		this.id = id;
		this.guide = guide;
		this.tour = tour;
		this.status = status;
		this.createdDate = createdDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Guide getGuide() {
		return guide;
	}

	public void setGuide(Guide guide) {
		this.guide = guide;
	}

	public Tour getTour() {
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Tour_Guide_Xref( Guide guide, Tour tour, int status) {
		super();
		this.guide = guide;
		this.tour = tour;
		this.status = status;
	}
	public Tour_Guide_Xref() {};
}
