package com.example.BookSocialNetwork;

import com.example.BookSocialNetwork.entities.Role;
import com.example.BookSocialNetwork.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@RequiredArgsConstructor
public class BookSocialNetworkApplication {

	private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(BookSocialNetworkApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(){
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()){
				roleRepository.save(
						Role
								.builder()
								.name("USER")
								.build()
				);
			}
		};
		}
}
