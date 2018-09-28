package com.webapp.tgo.service;

import java.util.List;

import com.webapp.tgo.entities.Contract;

public interface ContractService {
	Iterable<Contract> findAll();

    List<Contract> search(String q);

    Contract findOne(int id);

    void save(Contract Contract);

    void delete(int id);
}
