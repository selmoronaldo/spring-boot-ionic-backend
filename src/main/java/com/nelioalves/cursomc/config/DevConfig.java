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
	
	//POG - dá erro na chamada do value abaixo, vindo de application-dev.properties, 
	//      funciona se vim diretamente de application.properties
	//Value("${spring.jpa.hibernate.ddl-auto}")  //spring.jpa.hibernate.ddl-auto
	private String strategy = "create"; // = "create"; //  = "none" 
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		//evita que o BD seja recarregado
		if (!"create".equals(strategy)) {
			return false;
		}
		dbService.instantiateTestDatabase();
		return true;
	}

    @Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}

}
