package com.foresko.gamenever.application.core.query

import arrow.core.Either
import com.foresko.gamenever.core.apollo.TechnicalError

typealias RemoteQueryHandler<COMMAND, RESULT> = QueryHandler<COMMAND, @JvmSuppressWildcards Either<TechnicalError, RESULT>>