package com.example.peliculas.utils

class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }
        fun <T> error(message: String?, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }
        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
        fun <T> stopLoader(data: T?) : Resource<T> {
            return Resource(Status.STOP, data, null)
        }
        fun <T> noInternet(message: String?, data: T?) : Resource<T> {
            return Resource(Status.NOINTERNET, data, message)
        }
        fun <T> timeOut(message: String?, data: T?) : Resource<T> {
            return Resource(Status.TIMEOUT, data, message)
        }
        fun <T> throwError(message: String?, data: T?) : Resource<T> {
            return Resource(Status.THROW, data, message)
        }
    }
}