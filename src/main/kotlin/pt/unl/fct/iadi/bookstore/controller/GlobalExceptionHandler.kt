package pt.unl.fct.iadi.bookstore.controller

import jakarta.servlet.http.HttpServletRequest
import java.util.Locale
import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pt.unl.fct.iadi.bookstore.controller.dto.ErrorResponse
import pt.unl.fct.iadi.bookstore.service.BookAlreadyExistsException
import pt.unl.fct.iadi.bookstore.service.BookNotFoundException
import pt.unl.fct.iadi.bookstore.service.ReviewNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler(private val messageSource: MessageSource) {
    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFound(
            ex: BookNotFoundException,
            locale: Locale,
            request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val responseLocale =
                resolveSupportedLocale(locale, request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))
        val message =
                messageSource.getMessage("error.book.notFound", arrayOf(ex.isbn), responseLocale)

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_LANGUAGE, responseLocale.language)
                .body(ErrorResponse("NOT_FOUND", message))
    }

    @ExceptionHandler(BookAlreadyExistsException::class)
    fun handleBookAlreadyExists(ex: BookAlreadyExistsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse("CONFLICT", "Book with ISBN '${ex.isbn}' already exists"))
    }

    @ExceptionHandler(ReviewNotFoundException::class)
    fun handleReviewNotFound(ex: ReviewNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse(
                                "NOT_FOUND",
                                "Review '${ex.reviewId}' was not found for book '${ex.isbn}'"
                        )
                )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val message =
                ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Validation failed"

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse("VALIDATION_ERROR", message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse("BAD_REQUEST", ex.message ?: "Invalid request"))
    }

    private fun resolveSupportedLocale(locale: Locale, acceptLanguageHeader: String?): Locale {
        if (acceptLanguageHeader.isNullOrBlank()) {
            return Locale.ENGLISH
        }

        return if (locale.language.equals("pt", ignoreCase = true)) {
            Locale.forLanguageTag("pt")
        } else {
            Locale.ENGLISH
        }
    }
}
