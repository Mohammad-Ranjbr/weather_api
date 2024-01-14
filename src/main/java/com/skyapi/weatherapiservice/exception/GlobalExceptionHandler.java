package com.skyapi.weatherapiservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class) // chon mikhaim hameye khata ha ro modierat kone
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGeneralException(HttpServletRequest request , Exception exception){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(new Date());
        errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorDTO.setPath(request.getServletPath());

        LOGGER.error(exception.getMessage() , exception);

        return errorDTO;
    }

}
