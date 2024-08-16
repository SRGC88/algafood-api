package com.algaworks.algafood.domain.exception;


public class UsusarioNaoEncontradoException extends EntidadeNaoEncontradaException{

	private static final long serialVersionUID = 1L;
	
	
	public UsusarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public UsusarioNaoEncontradoException(Long usuarioId) {
		this(String.format("Não existe um cadastro de usuário com código %d", usuarioId));
	}

}
