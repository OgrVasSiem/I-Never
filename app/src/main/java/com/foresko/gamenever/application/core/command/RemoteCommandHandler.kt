package com.foresko.gamenever.application.core.command

import arrow.core.Either
import com.foresko.gamenever.core.apollo.TechnicalError

typealias RemoteCommandHandler<COMMAND, RESULT> = CommandHandler<COMMAND, @JvmSuppressWildcards Either<TechnicalError, RESULT>>