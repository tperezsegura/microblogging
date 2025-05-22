package org.personal.adapter.view

interface ResponseView<T> {
    fun displayResult(viewModel: ResponseViewModel<T>)
}