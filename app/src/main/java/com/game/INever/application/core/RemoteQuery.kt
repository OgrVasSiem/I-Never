package com.game.INever.application.core

import arrow.core.Either

typealias RemoteQuery<RESULT> = Query<@JvmSuppressWildcards Either<TechnicalError, RESULT>>

