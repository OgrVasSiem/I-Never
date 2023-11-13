package com.foresko.gamenever.application.core.command

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommandDispatcher @Inject constructor(
    private val handlers: Map<Class<out Command<*>>, @JvmSuppressWildcards CommandHandler<*, *>>
) {

    @Suppress("UNCHECKED_CAST")
    suspend fun <COMMAND : Command<RESULT>, RESULT> dispatch(command: COMMAND): RESULT {
        val commandHandler = checkNotNull(
            handlers[command::class.java] as? CommandHandler<COMMAND, RESULT>
        ) {
            "Command handler for $command is not found"
        }

        return commandHandler.handle(command)
    }
}
