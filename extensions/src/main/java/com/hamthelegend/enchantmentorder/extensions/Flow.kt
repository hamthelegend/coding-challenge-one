@file:Suppress("unused")

package com.hamthelegend.enchantmentorder.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

inline fun <T, R> Flow<T>.mapToMutableStateFlow(
    scope: CoroutineScope,
    initialValue: R,
    crossinline transform: suspend (value: T) -> R
): MutableStateFlow<R> {
    val state = MutableStateFlow(initialValue)

    scope.launch {
        collect { value ->
            state.update { transform(value) }
        }
    }

    return state
}

inline fun <T, R> StateFlow<T>.mapToMutableStateFlow(
    scope: CoroutineScope,
    crossinline transform: (value: T) -> R
): MutableStateFlow<R> {
    val state = MutableStateFlow(transform(value))

    scope.launch {
        collect { collectedValue ->
            state.update { transform(collectedValue) }
        }
    }

    return state
}

inline fun <T, R> Flow<T>.mapToStateFlow(
    scope: CoroutineScope,
    initialValue: R,
    crossinline transform: (value: T) -> R,
): StateFlow<R> {
    return map { transform(it) }
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = initialValue)
}

inline fun <T, R> StateFlow<T>.mapToStateFlow(
    scope: CoroutineScope,
    crossinline transform: (value: T) -> R,
): StateFlow<R> {
    return map { transform(it) }
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = transform(value))
}

inline fun <T1, T2, R> combineToStateFlow(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    scope: CoroutineScope,
    initialValue: R,
    crossinline transform: (a: T1, b: T2) -> R,
): StateFlow<R> {
    return combine(flow1, flow2) { a, b -> transform(a, b) }
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = initialValue)
}

inline fun <T1, T2, R> combineToStateFlow(
    stateFlow1: StateFlow<T1>,
    stateFlow2: StateFlow<T2>,
    scope: CoroutineScope,
    crossinline transform: (a: T1, b: T2) -> R,
): StateFlow<R> {
    return combine(stateFlow1, stateFlow2) { a, b -> transform(a, b) }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = transform(stateFlow1.value, stateFlow2.value),
        )
}

inline fun <T1, T2, T3, R> combineToStateFlow(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    scope: CoroutineScope,
    initialValue: R,
    crossinline transform: (a: T1, b: T2, c: T3) -> R,
): StateFlow<R> {
    return combine(flow1, flow2, flow3) { a, b, c -> transform(a, b, c) }
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = initialValue)
}

inline fun <T1, T2, T3, R> combineToStateFlow(
    stateFlow1: StateFlow<T1>,
    stateFlow2: StateFlow<T2>,
    stateFlow3: StateFlow<T3>,
    scope: CoroutineScope,
    crossinline transform: (a: T1, b: T2, c: T3) -> R,
): StateFlow<R> {
    return combine(stateFlow1, stateFlow2, stateFlow3) { a, b, c -> transform(a, b, c) }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = transform(stateFlow1.value, stateFlow2.value, stateFlow3.value),
        )
}

inline fun <T1, T2, T3, T4, R> combineToStateFlow(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    scope: CoroutineScope,
    initialValue: R,
    crossinline transform: (a: T1, b: T2, c: T3, d: T4) -> R,
): StateFlow<R> {
    return combine(flow1, flow2, flow3, flow4) { a, b, c, d -> transform(a, b, c, d) }
        .stateIn(scope = scope, started = SharingStarted.Eagerly, initialValue = initialValue)
}

inline fun <T1, T2, T3, T4, R> combineToStateFlow(
    stateFlow1: StateFlow<T1>,
    stateFlow2: StateFlow<T2>,
    stateFlow3: StateFlow<T3>,
    stateFlow4: StateFlow<T4>,
    scope: CoroutineScope,
    crossinline transform: (a: T1, b: T2, c: T3, d: T4) -> R,
): StateFlow<R> {
    return combine(stateFlow1, stateFlow2, stateFlow3, stateFlow4) { a, b, c, d ->
        transform(a, b, c, d)
    }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = transform(
                stateFlow1.value,
                stateFlow2.value,
                stateFlow3.value,
                stateFlow4.value,
            ),
        )
}