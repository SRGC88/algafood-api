package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;

import com.algaworks.algafood.domain.exception.NegocioException;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	private String codigo;
	
	@Column(nullable = false)
	private BigDecimal subtotal;
	
	@Column(nullable = false)
	private BigDecimal taxaFrete;
	
	@Column(nullable = false)
	private BigDecimal valorTotal;
	
	@Embedded
	private Endereco enderecoEntrega;
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO;
	
	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCriacao;
	
	@Column(columnDefinition = "datetime")
	private OffsetDateTime dataConfirmacao;
	
	@Column(columnDefinition = "datetime")
	private OffsetDateTime dataCancelamento;
	
	@Column(columnDefinition = "datetime")
	private OffsetDateTime dataEntrega;
	
	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FormaPagamento formaPagamento;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Restaurante restaurante;
	
	@JoinColumn(name = "usuario_cliente_id", nullable = false)
	@ManyToOne
	private Usuario cliente;
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL) //com o cascade é possível inserir os itens do pedido no momento de salvar o pedido
	private List<ItemPedido> itens = new ArrayList<>();

	
	
	public void calcularValorTotal() {
		getItens().forEach(ItemPedido::calcularPrecoTotal); //itera sobre cada instância de ItemPedido e calcula o PrecoTotal.
		
		this.subtotal = getItens().stream() //Cria um fluxo (Stream) de itens do pedido, permitindo processamento funcional sobre os dados.
				.map(item -> item.getPrecoTotal()) //para cada item obtém o precoTotal calculado anteriormente
				.reduce(BigDecimal.ZERO, BigDecimal::add); // soma cada preco a variavel subtotal
		
		this.valorTotal = this.subtotal.add(this.taxaFrete);
	}
	
	public void confirmar() {
		setStatus(StatusPedido.CONFIRMADO);
		setDataConfirmacao(OffsetDateTime.now());
	}
	
	public void entregar() {
		setStatus(StatusPedido.ENTREGUE);
		setDataEntrega(OffsetDateTime.now());
	}
	
	public void cancelar() {
		setStatus(StatusPedido.CANCELADO);
		setDataCancelamento(OffsetDateTime.now());
	}
	
	private void setStatus(StatusPedido novoStatus) {
		if (getStatus().naoPodeAlterarPara(novoStatus)) {
			throw new NegocioException(
					String.format("Status do pedido %s não pode ser alterado de %s para %s",
							getCodigo(),
							getStatus(),
							novoStatus.getDescricao()));
		}
		
		this.status = novoStatus;
	}
	
	// Executa antes de persistir um novo registro de entidade no banco, executa esse metodo. 
	// É um metodo de callback do JPA.
	@PrePersist 
	private void gerarCodigo() {
		setCodigo(UUID.randomUUID().toString());
	}
}
