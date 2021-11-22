package com.bcp.wstipocambio.controller.advise;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.bcp.wstipocambio.exception.ApiException;

@RestControllerAdvice
public class ApiAdvise {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ResponseMessage<Object>> responseException(ApiException e){
		
		ResponseMessage<Object> msg = ResponseMessage.<Object>builder()
				.code(e.getCode())
				.message(e.getMessage())
				.status(false)				
				.build();
		
		return new ResponseEntity<>(msg,e.getHttpStatus());
	}
	

}
