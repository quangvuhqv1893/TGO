package com.webapp.tgo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.webapp.tgo.entities.Language;

public interface LanguageRepository extends CrudRepository<Language, Integer>{
	Page<Language> findAll(Pageable pageable);
	Language findByLanguage(String language);
}
