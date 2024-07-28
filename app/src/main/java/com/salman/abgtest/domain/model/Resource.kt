package com.salman.abgtest.domain.model


/**
 * Created by Muhammed Salman email(mahmadslman@gmail.com) on 7/27/2024.
 */
class Resource<out T> private constructor(
    val status: Status,
    val data: T?,
    val message: String?,
    val exception: Throwable?
) {

    override fun toString(): String {
        return "Resource[" +
                "status=" + status + '\'' +
                ",message='" + message + '\'' +
                ",data=" + data +
                ']'
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(status = Status.Success, data = data, message = null, exception = null)
        }

        fun <T> error(msg: String?, data: T? = null): Resource<T> {
            return Resource(status = Status.Error, data = data, message = msg, exception = null)
        }

        fun <T> error(msg: String?, exception: Throwable? = null): Resource<T> {
            return Resource(
                status = Status.Error,
                data = null,
                message = msg,
                exception = exception
            )
        }

        fun <T> error(exception: Throwable?, data: T? = null): Resource<T> {
            return Resource(
                status = Status.Error,
                data = data,
                message = exception?.message,
                exception = exception
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(status = Status.Loading, data = data, message = null, exception = null)
        }

        fun <T> idle(data: T? = null): Resource<T> {
            return Resource(status = Status.Idle, data = data, message = null, exception = null)
        }
    }
}

sealed class Status {

    data object Idle : Status()
    data object Success : Status()
    data object Error : Status()
    data object Loading : Status()
}