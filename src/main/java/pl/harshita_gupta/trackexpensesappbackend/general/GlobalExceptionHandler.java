package pl.harshita_gupta.trackexpensesappbackend.general;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.error.ErrorResponseDTO;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.error.ErrorResponseDescriptionListDTO;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorCode;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorStrategy;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * GlobalExceptionHandler Controller exception handler for all application exceptions.
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @Autowired
    private ErrorStrategy errorStrategy;

    /**
     * This handler is not used, ConstraintViolationException is handle as Throwable exception.
     *
     * @param e ConstraintViolationException
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(final ConstraintViolationException e) {
        log.error("ConstraintViolationException: {}", e.getMessage());

        return new ResponseEntity<>(
                new ErrorResponseDTO(
                        ErrorCode.TEA003.getBusinessStatus(),
                        errorStrategy.returnExceptionMessage(ErrorCode.TEA003.getBusinessMessage()),
                        errorStrategy.returnExceptionDescription(String.format("Throwable exception %s",
                                e.getMessage())),
                        ErrorCode.TEA003.getBusinessStatusCode()),
                HttpStatus.valueOf(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseDTO> handleThrowableException(final Throwable ex) {
        log.error("handleThrowableException: {}", ex.getMessage());

        return new ResponseEntity<>(
                new ErrorResponseDTO(
                        ErrorCode.TEA004.getBusinessStatus(),
                        errorStrategy.returnExceptionMessage(ErrorCode.TEA004.getBusinessMessage()),
                        errorStrategy.returnExceptionDescription(String.format("Throwable exception %s",
                                ex.getMessage())),
                        ErrorCode.TEA004.getBusinessStatusCode()),
                HttpStatus.valueOf(ErrorCode.TEA004.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        log.error("handleHttpMessageNotReadableExceptionException: {}", ex.getMessage());

        return new ResponseEntity<>(
                new ErrorResponseDTO(ErrorCode.TEA003.getBusinessStatus(),
                        errorStrategy.returnExceptionMessage(ErrorCode.TEA003.getBusinessMessage()),
                        errorStrategy.returnExceptionDescription(
                                String.format("HttpMessageNotReadableException exception %s", ex.getMessage())),
                        ErrorCode.TEA003.getBusinessStatusCode()),
                HttpStatus.valueOf(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(AppRuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleAppRuntimeException(final AppRuntimeException ex) {
        log.error("handleAppRuntimeException message: {}, object: {}",
                ex.getBusinessMessage(),
                ex.getDescription()
        );

        return new ResponseEntity<>(
                new ErrorResponseDTO(
                        ex.getBusinessStatus(),
                        errorStrategy.returnExceptionMessage(ex.getBusinessMessage()),
                        errorStrategy.returnExceptionDescription(ex.getDescription()),
                        ex.getBusinessStatusCode()),
                HttpStatus.valueOf(ex.getBusinessStatusCode()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleNoHandlerFoundException(final NoHandlerFoundException ex) {
        log.error("handleNoHandlerFoundException message: {}, headers: {},  httpMethod: {}, request Url{}",
                ex.getMessage(),
                ex.getHeaders(),
                ex.getHttpMethod(),
                ex.getRequestURL()
        );

        return new ResponseEntity<>(
                new ErrorResponseDTO(
                        ErrorCode.TEA002.getBusinessStatus(),
                        errorStrategy.returnExceptionMessage(ErrorCode.TEA002.getBusinessMessage()),
                        errorStrategy.returnExceptionDescription(String.format("no handle for url %s",
                                ex.getRequestURL())),
                        ErrorCode.TEA002.getBusinessStatusCode()),
                HttpStatus.valueOf(ErrorCode.TEA002.getBusinessStatusCode())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDescriptionListDTO> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException ex) {

        log.info("MethodArgumentNotValidException in: {}", ex.getObjectName());

        return new ResponseEntity<>(
                new ErrorResponseDescriptionListDTO(
                        ErrorCode.TEA003.getBusinessStatus(),
                        errorStrategy.returnExceptionMessage(ErrorCode.TEA003.getBusinessMessage()),
                        errorStrategy.returnExceptionDescriptionList(buildBusinessDescription(ex)),
                        ErrorCode.TEA003.getBusinessStatusCode()
                ),
                HttpStatus.valueOf(ErrorCode.TEA003.getBusinessStatusCode()));
    }

    private List<String> buildBusinessDescription(final MethodArgumentNotValidException ex) {
        List<String> businessDescription = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String description = String.format("error: field: %s, default message: %s, rejected value: %s",
                    fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue()
            );

            log.error(description);
            businessDescription.add(description);
        });

        return businessDescription;
    }

}
