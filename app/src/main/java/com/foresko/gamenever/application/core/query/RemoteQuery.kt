package com.foresko.gamenever.application.core.query

import arrow.core.Either
import com.foresko.gamenever.core.apollo.TechnicalError


typealias RemoteQuery<RESULT> = Query<@JvmSuppressWildcards Either<TechnicalError, RESULT>>

