package com.skyapi.weatherapiservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class) // chon mikhaim hameye khata ha ro modierat kone
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // status ke barmigardone bara har khata
    @ResponseBody
    public ErrorDTO handleGeneralException(HttpServletRequest request , Exception exception){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(new Date());
        errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorDTO.setPath(request.getServletPath());

        LOGGER.error(exception.getMessage() , exception);

        return errorDTO;
    }
//    Approach 2
//    @ExceptionHandler(Exception.class) // chon mikhaim hameye khata ha ro modierat kone
//    public ResponseEntity<ErrorDTO> handleGeneralException2(HttpServletRequest request , Exception exception){
//        ErrorDTO errorDTO1 = new ErrorDTO();
//        errorDTO1.setTimestamp(new Date());
//        errorDTO1.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        errorDTO1.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//        errorDTO1.setPath(request.getServletPath());
//
//        LOGGER.error(exception.getMessage() , exception);
//
//        return new ResponseEntity<>(errorDTO1,HttpStatus.INTERNAL_SERVER_ERROR);
//    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        LOGGER.error(ex.getMessage() , ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(new Date());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(((ServletWebRequest) request).getRequest().getServletPath());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {
            errorDTO.addError(fieldError.getDefaultMessage());
        });
        return new ResponseEntity<>(errorDTO,headers,status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolationException(HttpServletRequest request , Exception exception){
        ErrorDTO errorDTO1 = new ErrorDTO();
        errorDTO1.setTimestamp(new Date());
        errorDTO1.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO1.addError(exception.getMessage());
        errorDTO1.setPath(request.getServletPath());

        LOGGER.error(exception.getMessage() , exception);

        return new ResponseEntity<>(errorDTO1,HttpStatus.BAD_REQUEST);
    }

}
