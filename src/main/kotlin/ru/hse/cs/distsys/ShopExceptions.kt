package ru.hse.cs.distsys

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class NotFoundException(message: String): ResponseStatusException(HttpStatus.NOT_FOUND, message)

class AuthorizationError(message: String): ResponseStatusException(HttpStatus.FORBIDDEN, message)