package com.irfanirawansukirman.pipileman.abstraction.ui

data class UIState<T>(val status: Status, val data: T?, val error: String) {

    enum class Status {
        SUCCESS, ERROR, LOADING, TIMEOUT, FINISH
    }

    companion object {
        fun <T> success(data: T): UIState<T> {
            return UIState(Status.SUCCESS, data, "")
        }

        fun <T> error(error: String): UIState<T> {
            return UIState(Status.ERROR, null, error)
        }

        fun <T> timeout(error: String): UIState<T> {
            return UIState(Status.TIMEOUT, null, error)
        }

        fun <T> loading(): UIState<T> {
            return UIState(Status.LOADING, null, "")
        }

        fun <T> finish(): UIState<T> {
            return UIState(Status.FINISH, null, "")
        }
    }
}