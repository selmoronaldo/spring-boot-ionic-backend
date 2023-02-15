package com.nelioalves.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.nelioalves.cursomc.services.DBService;
import com.nelioalves.cursomc.services.EmailService;
import com.nelioalves.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;
	}
	
	//OBS:
	// - O BEAN SERVE TB PARA DEIXAR UMA CLASS COMO COMPONENTE.
	//   PARA ESTE CASO COM A CLASSE ABSTRATA, QUE NÃO PODE INSTANCIAR. COM O
	//   BEAN, DEIXANDO-O COMO COMPONENTE É INSTANCIADO INDIRETAMENTE QUANTAS
	//   VEZES FOR SOLICITADO. (SOLICITADO NO INSERT DA CLASSE PedidoService)
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}

}
