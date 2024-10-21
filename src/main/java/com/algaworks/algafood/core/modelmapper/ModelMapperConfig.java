package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.EnderecoModel;
import com.algaworks.algafood.api.model.input.ItemPedidoInput;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		//return new ModelMapper();
		
		
		  var modelMapper = new ModelMapper();
		  
		// O trecho comentado abaixo faz o mapeamento de referência por métodos, pois nas duas classes possuem o método que pode ser referenciado na classe de destino
		// O get da classe de origem possui uma correspondência no set da classe de destino
		  
		/*
		 * modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
		 * .addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);
		 */
		  
		  // Faz o mapeamento para a classe EnderecoModel
		  var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
		  
		  // Faz a referência do nome do estado entre as duas classes (Endereco e EnderecoModel)
		  enderecoToEnderecoModelTypeMap.<String>addMapping(enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(), 
				  (enderecoModelDest, value) -> enderecoModelDest.getCidade().setEstado(value));
		  
		  //Pula o set no atributo id da classe ItemPedido no momento de fazer o map a partir da classe ItemPedidoInput 
		  //(que tentará descarregar nela o valor do atributo produtoId)
		  modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
		  		.addMappings(mapper -> mapper.skip(ItemPedido::setId));
		  		
		  
		  return modelMapper;
		 
	}

}
