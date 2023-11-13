package com.foresko.gamenever.application.operations.commands.dataStoreCommand

import com.foresko.gamenever.application.core.command.Command
import com.foresko.gamenever.application.core.command.CommandHandler
import com.foresko.gamenever.dataStore.Premium
import com.foresko.gamenever.dataStore.PremiumDataStore
import javax.inject.Inject

data class UpdatePremiumCommand(val premium: Premium?) : Command<Unit>

class UpdatePremiumCommandHandler @Inject constructor(
    private val premiumDataStore: PremiumDataStore
) : CommandHandler<UpdatePremiumCommand, Unit> {

    override suspend fun handle(command: UpdatePremiumCommand) {
        premiumDataStore.updateData { command.premium ?: it }
    }
}