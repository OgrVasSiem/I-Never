package com.game.INever.application.core

import arrow.core.Either

typealias RemoteQueryHandler<COMMAND, RESULT> = QueryHandler<COMMAND, @JvmSuppressWildcards Either<TechnicalError, RESULT>>