package com.webapp.tgo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webapp.tgo.entities.Language;
import com.webapp.tgo.repository.LanguageRepository;

@Service
public class LanguageServiceImpl implements LanguageService{
	Logger log = LoggerFactory.getLogger(LanguageServiceImpl.class);
	@Autowired
	private LanguageRepository languageRepository;

	@Override
	public Iterable<Language> findAll() {
//		log.info("----size of list language: "+languageRepository.findAll());
		return languageRepository.findAll();
	}

	@Override
	public void save(Language language) {
		languageRepository.save(language);
		
	}

	@Override
	public void delete(int id) {
		languageRepository.delete(id);
		
	}

	@Override
	public Language findByLanguage(String language) {
		log.info("find language: "+languageRepository.findByLanguage(language));
		return languageRepository.findByLanguage(language);
	}

	@Override
	public Language findOne(int id) {
		return languageRepository.findOne(id);
	}
	
	
}
