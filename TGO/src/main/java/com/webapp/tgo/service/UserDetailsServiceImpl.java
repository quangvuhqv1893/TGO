package com.webapp.tgo.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.tgo.entities.Guide;
import com.webapp.tgo.entities.Language;
import com.webapp.tgo.entities.Location;
import com.webapp.tgo.entities.Operator;
import com.webapp.tgo.entities.Role;
import com.webapp.tgo.entities.User;
import com.webapp.tgo.repository.GuideRepository;
import com.webapp.tgo.repository.LanguageRepository;
import com.webapp.tgo.repository.LocationRepository;
import com.webapp.tgo.repository.OperatorRepository;
import com.webapp.tgo.repository.RoleRepository;
import com.webapp.tgo.repository.UserRepository;
import com.webapp.tgo.util.Constant;

@Service
public class UserDetailsServiceImpl implements UserDetailService {
	Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
	@Autowired
	private GuideRepository guideRepository;
	@Autowired
	private OperatorRepository operatorRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private LanguageRepository languageRepository;
	
	@Autowired
	private LocationRepository locationRepository;

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    @Override
    @Transactional
    public String save(User user){
    	try {
        userRepository.save(user);
        return Constant.MESS_CHANGE_PASS_IS_SUCCESS;
    	}catch (Exception e) {
    		log.error("-----reset password is fail!!!!!!!!");
    		return Constant.MESS_CHANGE_PASS_IS_FAIL;
		}
		}

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(s);
        
        if(user==null){
        	System.out.println("null");
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Role roles= user.getRoles();
            grantedAuthorities.add(new SimpleGrantedAuthority(roles.getName()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),grantedAuthorities);
    }
	@Override
	public String changePassword(User user, String oldPassword, String newPassword) {
//		if(BCrypt.checkpw(oldPassword, user.getPassword())) {
//			user.setPassword(passwordEncoder.encode(newPassword));
//			userRepository.save(user);
//			return Constant.MESS_CHANGE_PASSWORD_SUCCESS;
//		}
			return Constant.MESS_CHANGE_PASSWORD_FAIL;
//		
	}
	/* @descripton: create new account
	 * @see com.webapp.tgo.service.UserDetailService#createNewUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	/*@Override
	@Transactional
	public String createNewUser(String username, String password, String email, String phone, String nameViet,
			String nameEng, String cardNumber, String cardType, String gender, String accountType, String address, String fullname) {
		try {
			log.info("-------start register user");
		if(userRepository.findByUsername(username)!=null) {
			log.info("-------username has been existed");
			return Constant.MESS_EXIST_USER;
		}
		log.info("username: "+username);
		log.info("password: "+password);
//		log.info("repassword: "+ repassword);
		log.info("email: "+email);
		log.info("phone: "+phone);
		log.info("nameViet: "+nameViet);
		log.info("nameEng: "+nameEng);
		log.info("cardNumber: "+ cardNumber);
		log.info("cardType: "+cardType);
		log.info("gender: "+gender);
		log.info("accountType: "+accountType);
		log.info("address: "+address);
		log.info("fullname: "+fullname);
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setAddress(address);
		user.setFullname(fullname);
		user.setPhonenumber(phone);
		user.setPassword(password);
		
		if(Constant.ENTITY_GUIDE.equalsIgnoreCase(accountType)) {
			log.info("-------start register guide");
			Guide guide = new Guide();
			user.setRoles(roleRepository.findByName(Constant.ROLE_GUIDE));
			userRepository.save(user);
			user = userRepository.findByUsername(username);
			log.info("userid: "+user.getId());
			guide.setUser(user);
			guide.setCardnumber(cardNumber);
			guide.setCardtype(cardType);
			guide.setGender(gender);
			guideRepository.save(guide);
			return Constant.MESS_SUCCESS;
		}else if(Constant.ENTITY_OPERATOR.equalsIgnoreCase(accountType)) {
			log.info("-------start register operator");
			Operator operator = new Operator();
			user.setRoles(roleRepository.findByName(Constant.ROLE_OPERATOR));
			userRepository.save(user);
			user = userRepository.findByUsername(username);
			log.info("userid: "+user.getId());
			operator.setUser(user);
			operator.setCompanyNameEng(nameEng);
			operator.setCompanyNameViet(nameViet);
			operatorRepository.save(operator);
			return Constant.MESS_SUCCESS;
		}else return Constant.MESS_FAIL;
		}catch (Exception e) {
			log.error("-------fail register!!",e);
			return Constant.MESS_FAIL;
		}

	}*/
	@Override
	@Transactional
	public String createNewOperator(User user, String companyName, String representative,
			String businessRegistrationCode, String taxCode) {
		try {
			user.setRoles(roleRepository.findByName(Constant.ROLE_OPERATOR));
				userRepository.save(user);
				log.info("----------------save user is success!!!");
				Operator operator = new Operator();
				operator.setUser(user);
				operator.setCompanyNameViet(companyName);
				operator.setBusinessRegistrationCode(businessRegistrationCode);
				operator.setTaxCode(taxCode);
				operator.setRepresentative(representative);
				operatorRepository.save(operator);
				log.info("----------------save operator is success!!!");
			return Constant.MESS_SUCCESS;
		}catch (Exception e) {
			return Constant.MESS_FAIL;
		}
	}
	@Override
	public String createNewGuide(User user, String birthDate, String language, String location, String cardNumber,
			String cardType, String expirationDate, StringBuffer exp, String gender) {
		try {
			user.setRoles(roleRepository.findByName(Constant.ROLE_GUIDE));
			userRepository.save(user);
			log.info("----------------save user is success!!!");
			Guide guide = new Guide();
			guide.setUser(user);
			guide.setCardnumber(cardNumber);
			guide.setCardtype(cardType);
			guide.setExperience(exp.toString());
			guide.setGender(gender);
			
			Date birthdate = null,expirationdate=null;
			try {
				birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
				expirationdate =new SimpleDateFormat("yyyy-MM-dd").parse(expirationDate); 
			} catch (Exception e) {
				log.error("error in Date method: "+e);
			}
			log.info("birthdate: "+birthdate);
			log.info("expirationdate: "+expirationdate);
			guide.setBirthdate(birthdate);
			guide.setExpirationdate(expirationdate);
			//set list language
			Set<Language> languages= new HashSet<Language>();
			languages.add(languageRepository.findByLanguage(language));
			guide.setLanguages(languages);
			//set list location
			Set<Location> locations=new HashSet<Location>();
			locations.add(locationRepository.findByLocationName(location));
			guide.setLocations(locations);
			
			guideRepository.save(guide);
			log.info("----------------save guide is success!!!");
			return Constant.MESS_SUCCESS;
		}catch (Exception e) {
			log.error("-------------save guide is fail!!!");
			return Constant.MESS_FAIL;
		}
	}
	@Override
	@Transactional
	public String cancelApproveUser(int userid, int role) {
		try {
			userRepository.changeApproveUser(userid, Constant.VALUE_DELETE_TOUR, role);
			return Constant.MESS_SUCCESS;
		} catch (Exception e) {
			log.error("cancel approve user is error!!",e);
			return Constant.MESS_FAIL;
		}
	}
	@Override
	@Transactional
	public String acceptApproveUser(int userid, int role) {
		try {
			userRepository.changeApproveUser(userid, Constant.VALUE_FINISH_TOUR, role);
			return Constant.MESS_SUCCESS;
		} catch (Exception e) {
			log.error("accept approve user is error!!",e);
			return Constant.MESS_FAIL;
		}
	}
}