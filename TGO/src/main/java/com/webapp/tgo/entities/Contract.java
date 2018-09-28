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

/**
 * @author Quang Vũ
 *
 */
@Entity
@Table(name = "contract")
public class Contract implements Serializable {
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
	@JoinColumn(name = "operatorid", referencedColumnName = "id")
	private Operator operator;

	@ManyToOne
	@JoinColumn(name = "tourid", referencedColumnName = "id")
	private Tour tour;

	@Column(name = "departureday", nullable = false)
	private Date departureDay;
	
	@Column(name ="finishday")
	private Date finishday;
	
	@Column(name = "salary")
	private int salary;

	@Column(name = "status")
	private String status;

	@Column(name = "createdDate")
	private Date createdDate;
	
	public Contract() {
	}

	public Contract(int id, Guide guide, Operator operator, Tour tour, Date departureDay, int salary) {
		super();
		this.id = id;
		this.guide = guide;
		this.operator = operator;
		this.tour = tour;
		this.departureDay = departureDay;
		this.salary = salary;
	}

	public Contract(int id, Guide guide, Operator operator, Tour tour, Date departureDay, int salary, String status) {
		super();
		this.id = id;
		this.guide = guide;
		this.operator = operator;
		this.tour = tour;
		this.departureDay = departureDay;
		this.salary = salary;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Tour getTour() {
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public Date getDepartureDay() {
		return departureDay;
	}

	public void setDepartureDay(Date departureDay) {
		this.departureDay = departureDay;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public Contract(int id, Guide guide, Operator operator, Tour tour, Date departureDay, Date finishday,
			int salary, String status, Date createdDate) {
		super();
		this.id = id;
		this.guide = guide;
		this.operator = operator;
		this.tour = tour;
		this.departureDay = departureDay;
		this.finishday = finishday;
		this.salary = salary;
		this.status = status;
		this.createdDate = createdDate;
	}

	public Date getFinishday() {
		return finishday;
	}

	public void setFinishday(Date finishday) {
		this.finishday = finishday;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	};

}
