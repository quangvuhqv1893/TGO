package com.webapp.tgo.entities;

import java.io.Serializable;
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
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="Language")
public class Language implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private int id;
	
	
	@Length(max = 255)
    @Column(name = "language", nullable = false, unique = true)
	private String language;
	@ManyToMany
	@JoinTable(
	            name = "guide_language_xref",
	            joinColumns = @JoinColumn(name = "languageid"),
	            inverseJoinColumns = @JoinColumn(name = "guideid")
	    )
    private Set<Guide> guides;
	
	@OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
	private Set<Tour> tours;
	
	public Set<Tour> getTours() {
		return tours;
	}

	public void setTours(Set<Tour> tours) {
		this.tours = tours;
	}

	public Set<Guide> getGuides() {
		return guides;
	}

	public void setGuides(Set<Guide> guides) {
		this.guides = guides;
	}

	public Language() {}

	

	public Language(int id, String language, Set<Guide> guides) {
		super();
		this.id = id;
		this.language = language;
		this.guides = guides;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	};
	
}
