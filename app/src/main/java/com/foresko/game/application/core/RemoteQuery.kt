package com.foresko.game.application.core

import arrow.core.Either

typealias RemoteQuery<RESULT> = Query<@JvmSuppressWildcards Either<TechnicalError, RESULT>>

