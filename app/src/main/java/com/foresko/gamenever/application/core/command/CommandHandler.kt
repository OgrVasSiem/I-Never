package com.foresko.gamenever.application.core.command

fun interface CommandHandler<COMMAND : Command<RESULT>, RESULT> {

    suspend fun handle(command: COMMAND): RESULT
}