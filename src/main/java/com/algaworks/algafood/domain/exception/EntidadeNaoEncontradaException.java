package com.algaworks.algafood.domain.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
public abstract class EntidadeNaoEncontradaException extends NegocioException {
	//com o abstract class a classe EntidadeNaoEncontradaException não pode mais ser instanciada.
	//Ela servirá apenas para capturar exceções, caso necessário

	private static final long serialVersionUID = 1L;
	
	
	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
		
	}


}
