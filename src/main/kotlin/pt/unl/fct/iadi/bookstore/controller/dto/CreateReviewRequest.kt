package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

class CreateReviewRequest(
    @field:Schema(description = "Rating from 1 to 5", example = "4", required = true)
    @field:Min(1)
    @field:Max(5)
    val rating: Int,

    @field:Schema(description = "Optional review comment", example = "Great book for Kotlin beginners")
    @field:Size(max = 500)
    val comment: String? = null
)
