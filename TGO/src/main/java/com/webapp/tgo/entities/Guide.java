package com.webapp.tgo.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;



@Entity
@Table(name = "guide")
public class Guide  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;
	
	@OneToOne
	@JoinColumn(name = "userid", referencedColumnName = "id")
    private User user;
	
    @Column(name = "birthdate", nullable = true)
    private Date birthdate;
	
	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	@Length(max = 50)
    @Column(name = "cardnumber", nullable = true)
    private String cardnumber;
	
	@Length(max = 50)
    @Column(name = "cardtype", nullable = true)
    private String cardtype;
    
	@Length(max = 50)
    @Column(name = "status", nullable = true)
	private  String status;
	
	@Column(name = "expirationdate", nullable = true)
    private Date expirationdate;
    
	@Length(max = 255)
    @Column(name = "experience", nullable = true)
	private String experience;
    
	@Length(max = 20)
    @Column(name = "gender", nullable = true)
    private String gender;
	@OrderBy("id ASC")
	@ManyToMany
	@JoinTable(
	            name = "guide_language_xref",
	            joinColumns = @JoinColumn(name = "guideid"),
	            inverseJoinColumns = @JoinColumn(name = "languageid")
	    )
    private Set<Language> languages;
	@OrderBy("id ASC")
	@ManyToMany
	@JoinTable(
	            name = "guide_location_xref",
	            joinColumns = @JoinColumn(name = "guideid"),
	            inverseJoinColumns = @JoinColumn(name = "locationid")
	    )
    private Set<Location> locations;
//	@OrderBy("id ASC")
//	@ManyToMany
//	@JoinTable(
//	            name = "tour_guide_xref",
//	            joinColumns = @JoinColumn(name = "guideid"),
//	            inverseJoinColumns = @JoinColumn(name = "tourid")
//	    )
//    private Set<Tour> tours;
	@OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
	private Set<Tour_Guide_Xref> tourGuideXref;
    public Set<Tour_Guide_Xref> getTourGuideXref() {
		return tourGuideXref;
	}
    
	@OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
	private Set<Contract> contracts;

	public Set<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}

	public void setTourGuideXref(Set<Tour_Guide_Xref> tourGuideXref) {
		this.tourGuideXref = tourGuideXref;
	}

	public Guide(){
    }

	public int getId() {
		return id;
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

	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getExpirationdate() {
		return expirationdate;
	}

	public void setExpirationdate(Date expirationdate) {
		this.expirationdate = expirationdate;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Set<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<Location> getLocations() {
		return locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

	public Guide(int id, User user, String cardnumber, String cardtype, String status, Date expirationdate,
			String experience, String gender) {
		super();
		this.id = id;
		this.user = user;
		this.cardnumber = cardnumber;
		this.cardtype = cardtype;
		this.status = status;
		this.expirationdate = expirationdate;
		this.experience = experience;
		this.gender = gender;
		
	}

	
}
