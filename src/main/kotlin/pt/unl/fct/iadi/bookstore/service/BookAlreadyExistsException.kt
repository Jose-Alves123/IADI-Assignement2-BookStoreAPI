package pt.unl.fct.iadi.bookstore.service

class BookAlreadyExistsException(val isbn: String) : RuntimeException()
