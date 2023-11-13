package com.foresko.gamenever.core.utils

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <A, B, C> Flow<Either<A, B>>.mapRight(block: suspend (B) -> C): Flow<Either<A, C>> {
    return map { either -> either.map { block(it) } }
}