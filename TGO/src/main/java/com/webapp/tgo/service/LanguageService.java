package com.webapp.tgo.service;


import com.webapp.tgo.entities.Language;

public interface LanguageService {
	Iterable<Language> findAll();

    void save(Language language);

    void delete(int id);

	Language findByLanguage(String language);

	Language findOne(int id);
    
    
}
