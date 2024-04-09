package io.github.coolishbee

class UniversalCallback<T> {
    var success: ((T) -> Unit) ?= null
    var failure: ((ApiError) -> Unit) ?= null
}