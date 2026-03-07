package pt.unl.fct.iadi.bookstore.service

import java.util.UUID

class ReviewNotFoundException(
    val isbn: String,
    val reviewId: UUID
) : RuntimeException()
