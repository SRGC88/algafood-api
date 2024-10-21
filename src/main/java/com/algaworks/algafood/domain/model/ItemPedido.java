package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class ItemPedido {
	
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	
	@Column(nullable = false)
	private Integer quantidade;
	
	@Column(nullable = false)
	private BigDecimal precoUnitario;
	
	@Column(nullable = false)
	private BigDecimal precoTotal;
	
	private String observacao;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Pedido pedido;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Produto produto;
	
	
	public void calcularPrecoTotal() {
		BigDecimal precoUnitario = this.getPrecoUnitario();
		Integer quantidade = this.getQuantidade();
		
		if (precoUnitario == null) {
			precoUnitario = BigDecimal.ZERO;
		}
		if(quantidade == null) {
			quantidade = 0;
		}
		
		this.setPrecoTotal(precoUnitario.multiply(new BigDecimal(quantidade)));
	}

}
