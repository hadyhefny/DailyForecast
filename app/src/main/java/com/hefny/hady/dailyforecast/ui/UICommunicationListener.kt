package com.hefny.hady.dailyforecast.ui

interface UICommunicationListener {
    fun showProgressBar(isLoading: Boolean)
    fun showError(errorMessage: String)
    fun hideKeyboard()
}