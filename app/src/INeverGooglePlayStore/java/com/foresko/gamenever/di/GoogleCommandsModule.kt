package com.foresko.gamenever.di

import com.foresko.gamenever.application.core.command.CommandHandler
import com.foresko.gamenever.application.operations.commands.subscriptionCommand.PurchaseGoogleSubscribeCommand
import com.foresko.gamenever.application.operations.commands.subscriptionCommand.PurchaseGoogleSubscribeCommandHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class GoogleCommandsModule {

    @Binds
    @IntoMap
    @CommandsModule.CommandHandlerKey(PurchaseGoogleSubscribeCommand::class)
    abstract fun bindPurchaseGoogleSubscribeCommandHandler(
        handler: PurchaseGoogleSubscribeCommandHandler
    ): CommandHandler<*, *>
}