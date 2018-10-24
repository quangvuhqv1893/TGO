package com.webapp.tgo.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "Tour")
public class Tour implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	private int id;

	@Length(max = 255)
	@Column(name = "Tourname", nullable = false)
	private String tourName;
	
	//lưu trạng thái tour -1: hủy, 0: khởi tạo, 1: hoàn thành, 2: đang chạy, 3: đang kiểm duyệt
	@Column(name = "status", nullable = false)
	private int status;
	
	@Column(name = "startDate", nullable = false)
	private Date startDate;
	

//	@Length(max = 255)
	@Column(name = "endDate", nullable = false)
	private Date endDate;

	@Column(name = "Tourprice", nullable = true)
	private int tourPrice;
	
	@Column(name="requirement")
	private String requirement;
	
	@Column(name="Amount", nullable = false, columnDefinition = "int default 100")
	private int amount;

	@Column(name="currentAmount",nullable = false, columnDefinition = "int default 100")
	private int currentAmount;
	
	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	@ManyToOne
	@JoinColumn(name ="languageid", referencedColumnName ="id")
	private Language language;
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "tour_location_xref", joinColumns = @JoinColumn(name = "tourid"), inverseJoinColumns = @JoinColumn(name = "locationid"))
	private Set<Location> locations;

//	@ManyToMany
//	@JoinTable(name = "operator_tour_xref", joinColumns = @JoinColumn(name = "tourid"), inverseJoinColumns = @JoinColumn(name = "operatorid"))
//	private Set<Operator> operators;
	@ManyToOne
	@JoinColumn(name = "operatorId", referencedColumnName = "id")
	private Operator operator;

//	@ManyToMany
//	@JoinTable(name = "tour_guide_xref", joinColumns = @JoinColumn(name = "tourid"), inverseJoinColumns = @JoinColumn(name = "guideid"))
//	private Set<Guide> guides;

	@OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
	private Set<Tour_Guide_Xref> tourGuideXref;
	
	@OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
	private Set<Contract> contracts;
	
	public Set<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}

	public Tour() {
	}

	@Column(name = "createdDate")
	private Date createdDate; 
	
	@Column(name = "updatedDate")
	private Date updatedDate; 
	
	
//	public List<Operator_Tour_Guide_Xref> getTourGuideXref() {
//		return operatorTourGuideXref;
//	}

//	public void setTourGuideXref(List<Operator_Tour_Guide_Xref> tourGuideXref) {
//		this.operatorTourGuideXref = tourGuideXref;
//	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTourName() {
		return tourName;
	}

	public void setTourName(String tourName) {
		this.tourName = tourName;
	}

/*	public String getTourTime() {
		return tourTime;
	}

	public void setTourTime(String tourTime) {
		this.tourTime = tourTime;
	}*/

	public int getTourPrice() {
		return tourPrice;
	}

	public void setTourPrice(int tourPrice) {
		this.tourPrice = tourPrice;
	}

	public Set<Location> getLocations() {
		return locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}



	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Tour(String tourName, Date endDate, int tourPrice) {
		super();
		this.tourName = tourName;
		this.endDate = endDate;
		this.tourPrice = tourPrice;
	}

	public Tour(String tourName, int status, Date startDate, Date endDate, int tourPrice,
			Set<Location> locations, Operator operator) {
		super();
		this.tourName = tourName;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.tourPrice = tourPrice;
		this.locations = locations;
		this.operator = operator;
//		this.operatorTourGuideXref = operatorTourGuideXref;
	}

	public Set<Tour_Guide_Xref> getTourGuideXref() {
		return tourGuideXref;
	}

	public void setTourGuideXref(Set<Tour_Guide_Xref> tourGuideXref) {
		this.tourGuideXref = tourGuideXref;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
