package com.perfulandia.usuario_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UsuarioBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioBackApplication.class, args);
	}

}

