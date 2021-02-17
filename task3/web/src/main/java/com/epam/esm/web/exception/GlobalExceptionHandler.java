package com.epam.esm.web.exception;

import com.epam.esm.service.constant.ServiceError;
import com.epam.esm.service.dto.ErrorDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.certificate.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.certificate.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.certificate.UnableDeleteGiftCertificateException;
import com.epam.esm.service.exception.certificate.UnableUpdateGiftCertificate;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import com.epam.esm.service.exception.tag.UnableDeleteTagException;
import com.epam.esm.service.exception.user.UserNotFoundException;
import com.epam.esm.service.exception.validation.GiftCertificateNotValidException;
import com.epam.esm.service.exception.validation.GiftCertificateParamsNotValidException;
import com.epam.esm.service.exception.validation.TagNotValidException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(value = GiftCertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerGiftCertificateNotFoundException(GiftCertificateNotFoundException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = GiftCertificateAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlerGiftCertificateAlreadyExistException(GiftCertificateAlreadyExistException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = UnableDeleteGiftCertificateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlerUnableDeleteGiftCertificateException(UnableDeleteGiftCertificateException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = UnableUpdateGiftCertificate.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlerUnableUpdateGiftCertificate(UnableUpdateGiftCertificate e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerTagNotFoundException(TagNotFoundException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = TagAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlerTagAlreadyExistException(TagAlreadyExistException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = UnableDeleteTagException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handlerUnableDeleteTagException(UnableDeleteTagException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerUserNotFoundException(UserNotFoundException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = TagNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handlerTagNotValidException(TagNotValidException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = GiftCertificateNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handlerGiftCertificateNotValidException(GiftCertificateNotValidException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = GiftCertificateParamsNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handlerGiftCertificateParamsNotValidException(GiftCertificateParamsNotValidException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
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
