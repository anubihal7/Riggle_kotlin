package com.rigle.servicehub.utils.view

class FieldValidator {
    enum class ValidationType{
        EMPTY,LENGTH,EMAIL,PHONE
    }
    enum class ErrorType{
        INLINE,TOAST
    }
   fun addValidationType(){}

   data class Field(val key:String)
}