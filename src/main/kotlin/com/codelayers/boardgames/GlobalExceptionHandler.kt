package com.codelayers.boardgames

import com.codelayers.boardgames.controller.ApiError
import com.codelayers.boardgames.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(ex: UserNotFoundException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ApiError(
                    status = HttpStatus.NOT_FOUND.value(),
                    error = "USER_NOT_FOUND",
                    message = ex.message!!
                )
            )
    }
}
