package com.algaworks.algafood.domain.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.service.EnvioEmailService.Mensagem;

@Service
public class FluxoPedidoService {
	
	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@Autowired
	private EnvioEmailService envioEmail;
	
	
	
	
	
	@Transactional
	public void confirmar(String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		
		pedido.confirmar();
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
				.corpo("O pedido de código <strong>"
						+ pedido.getCodigo() + "</strong> foi confirmado!")
				.destinatarios(Set.of(pedido.getCliente().getEmail()))
				.build();
			//  se quisesse passar mais de 1 destinatario (usando a anotação Singular na classe Mensagem)	
			// .destinatario("teste@algaworks.com")
		
		envioEmail.enviar(mensagem);
		
	}
	
	//de confirmado para entregue - para o pedido ser entregue ele precisa estar no status confirmado
	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		
		pedido.entregar();
	}
	
	
	//de entregue para cancelado - para ser cancelado precisa estar no status criado
	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		
		pedido.cancelar();
	}
}
