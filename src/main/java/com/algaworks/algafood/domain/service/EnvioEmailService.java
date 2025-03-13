package com.algaworks.algafood.domain.service;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;

public interface EnvioEmailService {
	
	void enviar(Mensagem mensagem);
	
	
	@Getter
	@Builder
	class Mensagem {
		
		//@Singular
		private Set<String> destinatarios;
		
		//@NonNull
		private String assunto;
		
		//@NonNull
		private String corpo;
		
	}

}
