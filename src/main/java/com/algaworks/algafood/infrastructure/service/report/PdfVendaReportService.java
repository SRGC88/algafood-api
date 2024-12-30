package com.algaworks.algafood.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;
import com.algaworks.algafood.domain.service.VendaQueryService;
import com.algaworks.algafood.domain.service.VendaReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Service
public class PdfVendaReportService implements VendaReportService{
	
	
	@Autowired
	private VendaQueryService vendaQueryService;
	

	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
		
		try {
		
			// pega o fluxo de dados de um arquivo para dentro do classhpath do projeto (carrega o arquivo jasper no projeto)
			var inpuStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper"); 
			
			var parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR")); //aqui tbm poderia passar os parâmetros que o usuario informa na api para exibi-los no relatório
			
			var VendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset); //Faz a consulta no banco
			
			var dataSource = new JRBeanCollectionDataSource(VendasDiarias); //Carrega o dataSource com o resultado obtido da consulta no banco
			
			//objeto que representa um relatório preenchido (carrega um objeto jasper com os resultados dos passsos anteriores)
			var jasperPrint = JasperFillManager.fillReport(inpuStream, parametros, dataSource);
			
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			throw new ReportException("Não foi possível emitir relatório de vendas diárias", e);
		} 
	}

}
