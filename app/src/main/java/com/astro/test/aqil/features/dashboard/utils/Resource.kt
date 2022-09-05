package com.astro.test.aqil.features.dashboard.utils

data class Resource<out T>(var status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> done(data: T?): Resource<T> {
            return Resource(Status.DONE, data, null)
        }

    }

}