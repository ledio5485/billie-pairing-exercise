package io.billie.common.exception

import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ConstraintViolationException::class, IllegalArgumentException::class, IllegalStateException::class)
    fun handleConstraintViolationException(
        e: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.BAD_REQUEST.reasonPhrase,
            code = HttpStatus.BAD_REQUEST.value(),
            detail = e.message ?: "Invalid request."
        )
        return buildException(error)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(
        e: EntityNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.NOT_FOUND.reasonPhrase,
            code = HttpStatus.NOT_FOUND.value(),
            detail = e.message ?: "Resource not found."
        )
        return buildException(error)
    }

    @ExceptionHandler(Throwable::class)
    fun handleException(
        e: Throwable,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(
            status = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            detail = e.message ?: "Internal error, please try again."
        )
        return buildException(error)
    }

    private fun buildException(error: ApiError) =
        ResponseEntity
        .status(error.code)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(error)
}
