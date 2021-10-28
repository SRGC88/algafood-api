package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;


@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemtype = ProblemType.ENTIDADE_NAO_ENCONTRADA;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemtype, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
		
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?>handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);
		
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType, detail).build();
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
		
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = Problem.builder()
				.title(status.getReasonPhrase())
				.status(status.value())
				.build();
		
		} else if (body instanceof String) {
			body = Problem.builder()
					.title((String)body)
					.status(status.value())
					.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
		
		return Problem.builder()
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail);
	}
	
	
	
	
	/*@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException(){
		
		Problema problema = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem("O tipo de mídia não é aceito.").build();
		
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
				.body(problema);
	}*/
}
