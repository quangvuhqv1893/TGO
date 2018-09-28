package com.webapp.tgo.repository;

import org.springframework.data.repository.CrudRepository;

import com.webapp.tgo.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    Role findByName(String name);

}
