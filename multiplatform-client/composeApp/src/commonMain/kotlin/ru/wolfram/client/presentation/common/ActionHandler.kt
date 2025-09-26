package ru.wolfram.client.presentation.common

interface ActionHandler<A : Action> {
    fun handleAction(action: A)
}