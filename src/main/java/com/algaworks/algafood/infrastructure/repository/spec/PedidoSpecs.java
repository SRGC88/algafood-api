package com.algaworks.algafood.infrastructure.repository.spec;

import java.util.ArrayList;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;

public class PedidoSpecs {
	
	public static Specification<Pedido> usandoFiltro(PedidoFilter filtro){
		return (root, query, builder) -> {
			//uso do fetch para evitar o problema do n+1 do jpa,
			//pois a cada consulta de pedido são feitas consultas em restaurante, cozinha e cliente
			if(Pedido.class.equals(query.getResultType())){
				root.fetch("restaurante").fetch("cozinha"); //um restaurante tem uma cozinha
				root.fetch("cliente");
				root.fetch("enderecoEntrega").fetch("cidade").fetch("estado");
			}
			
		
			
			var predicates = new ArrayList<Predicate>();
			
			if (filtro.getClienteId() != null) {
				predicates.add(builder.equal(root.get("cliente"), filtro.getClienteId())); //constroi um predicate
			}
			
			if (filtro.getRestauranteId() != null) {
				predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId())); //constroi um predicate
			}
			
			if (filtro.getDataCriacaoInicio() != null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
			}
			
			if (filtro.getDataCriacaoFim() != null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
			}
			
			return builder.and(predicates.toArray(new Predicate[0]));
		};
		
	}
	
}
