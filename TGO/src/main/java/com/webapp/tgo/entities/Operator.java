package com.webapp.tgo.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "operator")
public class Operator implements Serializable {

    public static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;
    

	@OneToOne
	@JoinColumn(name = "userid", referencedColumnName = "id")
    private User user;
	
	@Length(max = 255)
    @Column(name = "Companynameviet", nullable = false)
	private String companyNameViet;
	
	@Length(max = 255)
	@Column(name = "business_registration_code")
	private String businessRegistrationCode;
	

	@Length(max =255)
	@Column(name = "tax_code")
	private String taxCode;
	
	@Length(max =255)
	@Column(name = "representative")
	private String representative;
public String getRepresentative() {
		return representative;
	}
	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	//	@OrderBy("id ASC")
//	@ManyToMany
//	@JoinTable(
//	            name = "operator_tour_xref",
//	            joinColumns = @JoinColumn(name = "operatorid"),
//	            inverseJoinColumns = @JoinColumn(name = "tourid")
//	    )
//    private Set<Tour> tours;
	@OneToMany(mappedBy = "operator", cascade = CascadeType.ALL)
	private Set<Tour> tours;
	public Operator(){}
	public int getId() {
		return id;
	}
	
	@OneToMany(mappedBy = "operator", cascade = CascadeType.ALL)
	private Set<Contract> contracts;
	public Set<Contract> getContracts() {
		return contracts;
	}
	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Set<Tour> getTours() {
		return tours;
	}
	public void setTours(Set<Tour> tours) {
		this.tours = tours;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
//	public String getCompanyName() {
//		return companyNameViet;
//	}
//	public void setCompanyName(String companyName) {
//		this.companyNameViet = companyName;
//	}
	public String getBusinessRegistrationCode() {
		return businessRegistrationCode;
	}
	public void setBusinessRegistrationCode(String businessRegistrationCode) {
		this.businessRegistrationCode = businessRegistrationCode;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public Operator(User user, String companyNameViet) {
		super();
		this.user = user;
		this.companyNameViet = companyNameViet;
	}
	public String getCompanyNameViet() {
		return companyNameViet;
	}
	public void setCompanyNameViet(String companyNameViet) {
		this.companyNameViet = companyNameViet;
	}
	
	
	
}
