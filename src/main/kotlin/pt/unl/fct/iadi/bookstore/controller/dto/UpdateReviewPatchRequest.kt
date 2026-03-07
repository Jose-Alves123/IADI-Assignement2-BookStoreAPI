package pt.unl.fct.iadi.bookstore.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

class UpdateReviewPatchRequest(
    @field:Schema(description = "Optional rating update from 1 to 5", example = "5")
    @field:Min(1)
    @field:Max(5)
    val rating: Int? = null,

    @field:Schema(description = "Optional review comment update", example = "Updated after a second read")
    @field:Size(max = 500)
    val comment: String? = null
)
