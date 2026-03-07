package pt.unl.fct.iadi.bookstore.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class BookAlreadyExistsException(val isbn: String) : RuntimeException()
