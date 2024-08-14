package com.taskManager.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	private final String resourceName;
	private final String fieldName;
	private final long fieldValue;
	public ResourceNotFoundException(String resourceName, String fieldName, Long id) {
		super(String.format("%s not found with %s: '%s'",resourceName,fieldName,id));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = id;
	}


}
