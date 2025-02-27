package com.algaworks.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;

	
	//@Service
	public class LocalFotoStorageService implements FotoStorageService {
			
		@Autowired
		private StorageProperties storageProperties;
	
		
		
		@Override
		public void armazenar(NovaFoto novaFoto) {
			try {
				//Monta o caminho onde será armazenada a foto
				Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
				
				// Copia os bytes do arquivo com o getInputStream para o caminho de armazenamento da foto
				FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
				
			} catch (Exception e) {
				throw new StorageException("Não foi possível armazenar o arquivo.", e);
			}
			
			
		}
		
		@Override
		public void remover(String nomeArquivo) {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			
			try {
				Files.deleteIfExists(arquivoPath);
			} catch (Exception e) {
				throw new StorageException("Não foi possível excluir o arquivo.", e);
			}
			
		}
		
		//Concatena o diretório da foto com o nome do arquivo
		//Para formar o caminho completo onde a foto será armazenada
		private Path getArquivoPath(String nomeArquivo) {
			return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));
			
		}

		@Override
		public FotoRecuperada recuperar(String nomeArquivo) {
			try {
				Path arquivoPath = getArquivoPath(nomeArquivo);
				
				FotoRecuperada fotoRecuperada = FotoRecuperada.builder()
						.inputStream(Files.newInputStream(arquivoPath))
						.build();
				
				return fotoRecuperada;
			} catch (Exception e) {
				throw new StorageException("Não foi possível recuperar o arquivo", e);
			}
		}
}
