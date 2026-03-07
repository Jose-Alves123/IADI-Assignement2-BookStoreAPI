package pt.unl.fct.iadi.bookstore.controller.dto

import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL

class UpdateBookPatchRequest(
    @field:Size(min = 1, max = 120)
    val title: String? = null,

    @field:Size(min = 1, max = 80)
    val author: String? = null,

    @field:Positive
    val price: Double? = null,

    @field:URL
    val image: String? = null
)
