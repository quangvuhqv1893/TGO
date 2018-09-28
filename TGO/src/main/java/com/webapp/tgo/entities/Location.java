package com.webapp.tgo.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="location")
public class Location implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;
	
	@Length(max = 255)
    @Column(name = "Locationname", nullable = false,unique = true)
	private String locationName;
	
	@Length(max = 255)
    @Column(name = "Locationtype", nullable = false)
	private String locationType;
	
	@ManyToMany
	@JoinTable(
	            name = "guide_location_xref",
	            joinColumns = @JoinColumn(name = "locationid"),
	            inverseJoinColumns = @JoinColumn(name = "guideid")
	    )
    private Set<Guide> guides;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
	            name = "tour_location_xref",
	            joinColumns = @JoinColumn(name = "locationid"),
	            inverseJoinColumns = @JoinColumn(name = "tourid")
	    )
    private Set<Tour> tours;
	
	public Location() {}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public Set<Guide> getGuides() {
		return guides;
	}
	public void setGuides(Set<Guide> guides) {
		this.guides = guides;
	}
	public Set<Tour> getTours() {
		return tours;
	}
	public void setTours(Set<Tour> tour) {
		this.tours = tour;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Location(int id, String locationName, String locationType, Set<Guide> guides, Set<Tour> tour) {
		super();
		this.id = id;
		this.locationName = locationName;
		this.locationType = locationType;
		this.guides = guides;
		this.tours = tour;
	}
	
	
}
