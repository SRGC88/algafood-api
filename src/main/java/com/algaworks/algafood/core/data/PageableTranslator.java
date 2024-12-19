package com.algaworks.algafood.core.data;


import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;



public class PageableTranslator {
	
	public static Pageable translate(Pageable pageable, Map<String, String> fieldsMapping) {
	   
		var orders = pageable.getSort().stream() // pageable.getSort() devolve um order
			.filter(order -> fieldsMapping.containsKey(order.getProperty())) //filtra apenas as propriedades existentes no mapeamento, que sao as orders válidas 
			.map(order -> new Sort.Order(order.getDirection(), 
					fieldsMapping.get(order.getProperty()))) // instancia um novo order atribuindo como valor o conteúdo da propriedade definida no fieldsMapping
			.collect(Collectors.toList()); 
		
	    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
	    		Sort.by(orders)); //instancia um novo pageable
	}

}
