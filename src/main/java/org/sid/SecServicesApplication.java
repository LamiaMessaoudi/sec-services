package org.sid;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import entities.AppRole;
import services.AccountService;

@SpringBootApplication
@EntityScan("entities")
@EnableJpaRepositories("dao")
@ComponentScan({"entities","dao","services","web","sec"})
public class SecServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecServicesApplication.class, args);
	}
	@Bean
	CommandLineRunner start(AccountService accountService)
	{
		
		return args->{
			accountService.save(new AppRole(null,"USER"));
			accountService.save(new AppRole(null,"ADMIN"));
		   Stream.of("user1","user2","user3","admin").forEach(un->{
			   accountService.saveUser(un, "1234", "1234");
		   });
		   accountService.addRoleToUser("admin", "ADMIN");
		};
	}
	@Bean 
	public BCryptPasswordEncoder getBCPE()
	{
		return new BCryptPasswordEncoder();
	}

}
