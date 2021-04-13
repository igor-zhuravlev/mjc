package com.epam.esm.web.exception;

import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.dto.ErrorDto;
import com.epam.esm.service.exception.ResourceAlreadyExistException;
import com.epam.esm.service.exception.ResourceNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.UnableDeleteResourceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    private ErrorDto handle(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(code, null, locale);
        return new ErrorDto(message, code);
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handlerServiceException(ServiceException e) {
        logger.error(e.getMessage());
        return handle(ServiceError.INTERNAL_SERVER_ERROR.getCode());
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerResourceNotFoundException(ResourceNotFoundException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlerResourceAlreadyExistException(ResourceAlreadyExistException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = UnableDeleteResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlerUnableDeleteResourceException(UnableDeleteResourceException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(BindException e) {
        Map<String, String> constraints = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            constraints.put(fieldName, errorMessage);
        });
        ErrorDto errorDto = handle(ServiceError.INVALID_ARGUMENTS.getCode());
        errorDto.setConstraints(constraints);
        return errorDto;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> constraints = new HashMap<>();
        e.getConstraintViolations().forEach(constraintViolation -> {
            constraints.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
        });
        ErrorDto errorDto = handle(ServiceError.INVALID_ARGUMENTS.getCode());
        errorDto.setConstraints(constraints);
        return errorDto;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(ServiceError.INVALID_ARGUMENT_TYPE.getCode(),
                new Object[]{e.getRequiredType().getName()}, locale);

        Map<String, String> constraints = new HashMap<>();
        constraints.put(e.getName(), message);

        ErrorDto errorDto = handle(ServiceError.INVALID_ARGUMENTS.getCode());
        errorDto.setConstraints(constraints);
        return errorDto;
    }
}
