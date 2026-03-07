package pt.unl.fct.iadi.bookstore.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID

@ResponseStatus(HttpStatus.NOT_FOUND)
class ReviewNotFoundException(
    val isbn: String,
    val reviewId: UUID
) : RuntimeException()
