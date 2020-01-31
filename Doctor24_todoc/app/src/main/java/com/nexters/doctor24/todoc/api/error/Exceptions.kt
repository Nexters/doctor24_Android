package com.nexters.doctor24.todoc.api.error

class NoResponseException(message: String? = ErrorHandler.UNKNOWN_ERROR) : Exception(message)

class NoDataException(message: String? = ErrorHandler.NO_SUCH_DATA) : Exception()