package pt.unl.fct.iadi.bookstore.domain

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import java.util.UUID

data class Review(

    @field:Schema(description = "Identifier of the review", example = "123e4567-e89b-12d3-a456-426614174000")
    val id : UUID,

    @field:Schema(description = "Rating from 1 to 5", example = "5", required = true)
    @field:Min(1)
    @field:Max(5)
    val rating : Int,

    @field:Schema(description = "Optional review comment", example = "Excellent read with practical examples")
    @field:Size(max = 500)
    val comment : String? = null
)
