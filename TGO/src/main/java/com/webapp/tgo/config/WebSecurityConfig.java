package com.webapp.tgo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
    // Web Security

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Bean
    public UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	//config message
//    @Bean
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
//        bean.addBasenames("classpath:messages");
//        return bean;
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    // Authentication and Authorization

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
		http
                .authorizeRequests()
                .antMatchers("/index").permitAll()
                .antMatchers("/guide").hasRole("GUIDE")
                .antMatchers("/operator").hasRole("OPERATOR")
                .antMatchers("/admin").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/default")
                .failureUrl("/login?error")
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/403");
    }
    


}
