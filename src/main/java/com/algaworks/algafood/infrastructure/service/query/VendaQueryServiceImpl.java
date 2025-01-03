package com.algaworks.algafood.infrastructure.service.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.StatusPedido;
import com.algaworks.algafood.domain.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.service.VendaQueryService;


@Repository
public class VendaQueryServiceImpl implements VendaQueryService {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		var builder = manager.getCriteriaBuilder();
		
		var query = builder.createQuery(VendaDiaria.class); //o que a query retorna
		
		var root = query.from(Pedido.class);
		
		var functionConvertTzDataCriacao = builder.function("convert_tz", Date.class, 
				root.get("dataCriacao"),
				builder.literal("+00:00"), 
				builder.literal(timeOffset)); // convert_tz é a função do mysql para mudar o offset da data
		
		var functionDateDataCriacao = builder.function("date", Date.class, functionConvertTzDataCriacao); //date é nome da função do mysql que remove as horas da data
		
		var selection = builder.construct(VendaDiaria.class, 
				functionDateDataCriacao,
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal"))); // montagem dos campos do select
		
		query.select(selection); // finaliza o select
		
		var predicates = new ArrayList<Predicate>(); //monta os filtros
		
		if (filtro.getRestauranteId() != null) {
			predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
		}
		
		if (filtro.getDataCriacaoInicio() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
		}
		
		if (filtro.getDataCriacaoFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
		}
		
		predicates.add(root.get("status").in(
				StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));
		query.where(predicates.toArray(new Predicate[0])); // adiciona os filtros no where
		
		
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList();
	}
	
}

	/*
	Query:
	select date(convert_tz(p.data_criacao, '+00:00', '-03:00')) as data_criacao,
	  count(p.id) as total_vendas,
	  sum(p.valor_total) as total_faturado
	from pedido p
	where p.status in ('CONFIRMADO', 'ENTREGUE')
	group by date(convert_tz(p.data_criacao, '+00:00', '-03:00'))
	*/
