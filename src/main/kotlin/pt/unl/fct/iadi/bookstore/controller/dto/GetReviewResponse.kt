package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import pt.unl.fct.iadi.bookstore.domain.Review
import java.util.UUID

class GetReviewResponse(
    @field:Schema(description = "Identifier of the review", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: UUID,
    @field:Schema(description = "Rating from 1 to 5", example = "4")
    val rating: Int,
    @field:Schema(description = "Optional review comment", example = "Great book for Kotlin beginners")
    val comment: String?
) {
    companion object {
        fun fromReview(review: Review): GetReviewResponse {
            return GetReviewResponse(
                id = review.id,
                rating = review.rating,
                comment = review.comment
            )
        }
    }
}
