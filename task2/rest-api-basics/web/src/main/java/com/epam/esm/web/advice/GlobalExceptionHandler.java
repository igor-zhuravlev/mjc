package com.epam.esm.web.advice;

import com.epam.esm.constant.ServiceError;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.certificate.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.certificate.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.certificate.UnableDeleteGiftCertificateException;
import com.epam.esm.service.exception.certificate.UnableUpdateGiftCertificate;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import com.epam.esm.dto.ErrorDto;
import com.epam.esm.service.exception.tag.UnableDeleteTagException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

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
}
