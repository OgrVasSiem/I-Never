package com.foresko.game.application.core

import arrow.core.Either

typealias RemoteQueryHandler<COMMAND, RESULT> = QueryHandler<COMMAND, @JvmSuppressWildcards Either<TechnicalError, RESULT>>