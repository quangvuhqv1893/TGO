package com.webapp.tgo.entities;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="role")
public class Role implements Serializable{

    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false,unique = true)
    private  int id;

    @Length(max = 255)
    @NotEmpty
    @Column(name = "name",nullable = false)
    private  String name;

    @OneToMany(mappedBy = "roles",cascade = CascadeType.REMOVE,orphanRemoval = true)
//    @OrderBy("id DESC")
//    @JsonBackReference
    private Set<User> users=new HashSet<User>();

    public Role(){}

    public Role(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

//    public void addUser(User user){
//        users.add(user);
//        user.setRoles(this);
//    }
//
//    public void removeUser(User user){
//        users.remove(user);
//        user.setRoles(null);
//    }





}
