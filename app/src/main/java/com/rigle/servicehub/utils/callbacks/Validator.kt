package com.rigle.servicehub.utils.callbacks

interface Validator {
    fun isValidField(s: String): Boolean
}