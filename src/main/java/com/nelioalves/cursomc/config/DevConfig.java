package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;
import com.nelioalves.cursomc.services.EmailService;
import com.nelioalves.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Value("${spring.jpa.hibernate.ddl-auto}")  //spring.jpa.hibernate.ddl-auto
	private String strategy; // "create" //  "none" // create-drop
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		//evita que o BD seja sempre recarregado
		String temp = this.getStrategy();
		System.out.println("PONTO 1 - " + temp);
		if (!"create-drop".equals(this.getStrategy())) {
			return false;
		}
		System.out.println("PONTO 2");
		dbService.instantiateTestDatabase();
		System.out.println("PONTO 3");
		return true;
	}

    @Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
    
    public String getStrategy() {
    	return this.strategy;
    }

}
