package com.foresko.gamenever.application.operations.commands.dataStoreCommand

import com.foresko.gamenever.application.core.command.Command
import com.foresko.gamenever.application.core.command.CommandHandler
import com.foresko.gamenever.dataStore.ReminderAboutPremiumDataStore
import javax.inject.Inject

data class UpdateReminderAboutPremiumCommand(val createdDebtsCount: Long) : Command<Unit>

class UpdateReminderAboutPremiumCommandHandler @Inject constructor(
    private val reminderAboutPremiumDataStore: ReminderAboutPremiumDataStore
) : CommandHandler<UpdateReminderAboutPremiumCommand, Unit> {

    override suspend fun handle(command: UpdateReminderAboutPremiumCommand) {
        reminderAboutPremiumDataStore.updateData { it + command.createdDebtsCount }
    }
}
