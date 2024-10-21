package com.algaworks.algafood.domain.model;

public enum StatusPedido {
	
	CRIADO("Criado"),
	CONFIRMADO("Confirmado"),
	ENTREGUE("Entregue"),
	CANCELADO("Cancelado");
	
	private String descricao;
	
	//Construtor que recebe uma descrição e retorna essa descrição
	StatusPedido(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

}
