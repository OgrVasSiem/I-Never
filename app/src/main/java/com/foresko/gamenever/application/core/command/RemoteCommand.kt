package com.foresko.gamenever.application.core.command

import arrow.core.Either
import com.foresko.gamenever.core.apollo.TechnicalError

typealias RemoteCommand<RESULT> = Command<@JvmSuppressWildcards Either<TechnicalError, RESULT>>