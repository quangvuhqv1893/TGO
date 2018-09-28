package com.webapp.tgo.entities;

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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "user")
public class User implements Serializable {

    public static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @NotEmpty
    @Length(max = 255)
    @Column(name = "username", nullable = false)
    private String username;

    @NotEmpty
    @Length(min = 8)
    @Column(name = "password", nullable = false)
    private String password;

//    @NotEmpty
    @Length(max = 255)
    @Column(name = "fullname", nullable = false)
    private String fullname;

//    @NotEmpty
    @Length(max = 255)
    @Column(name = "Address")
    private String address;

//    @NotEmpty
    @Length(max = 255)
    @Column(name = "phonenumber")
    private String phonenumber;

//    @NotEmpty
    @Email
    @Column(name = "email")
    private String email;
    
//lưu trạng thái user được kiểm duyệt hay chưa?1:0
@Column(name ="status", nullable = false)
private int status;

    public int getStatus() {
	return status;
}

public void setStatus(int status) {
	this.status = status;
}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleID", referencedColumnName = "id")
//    @JsonBackReference
    private Role roles;

    public User(String username, String password, String fullname, String address, String phonenumber, String email, Role roles) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.address = address;
        this.phonenumber = phonenumber;
        this.email = email;
        this.roles = roles;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }


}