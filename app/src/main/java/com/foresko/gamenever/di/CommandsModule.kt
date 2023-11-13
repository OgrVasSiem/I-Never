package com.foresko.gamenever.di

import com.foresko.gamenever.application.core.command.Command
import com.foresko.gamenever.application.core.command.CommandHandler
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignInWithGoogleCommand
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignInWithGoogleCommandHandler
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignOutCommand
import com.foresko.gamenever.application.operations.commands.authorizationCommands.SignOutCommandHandlerImpl
import com.foresko.gamenever.application.operations.commands.dataStoreCommand.UpdatePremiumCommand
import com.foresko.gamenever.application.operations.commands.dataStoreCommand.UpdatePremiumCommandHandler
import com.foresko.gamenever.application.operations.commands.subscriptionCommand.PurchaseInAppSubscriptionCommand
import com.foresko.gamenever.application.operations.commands.subscriptionCommand.PurchaseInAppSubscriptionCommandHandlerImpl
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
@InstallIn(SingletonComponent::class)
abstract class CommandsModule {
    @MapKey
    annotation class CommandHandlerKey(val value: KClass<out Command<*>>)

    @Binds
    @IntoMap
    @CommandHandlerKey(SignInWithGoogleCommand::class)
    abstract fun bindSignInWithGoogleCommandHandler(
        handler: SignInWithGoogleCommandHandler
    ): CommandHandler<*, *>

    @Binds
    @IntoMap
    @CommandHandlerKey(SignOutCommand::class)
    abstract fun bindSignOutCommandHandlerImpl(
        handler: SignOutCommandHandlerImpl
    ): CommandHandler<*, *>

    @Binds
    @IntoMap
    @CommandHandlerKey(PurchaseInAppSubscriptionCommand::class)
    abstract fun bindPurchaseInAppSubscriptionCommandHandler(
        purchaseInAppSubscriptionCommandHandler: PurchaseInAppSubscriptionCommandHandlerImpl
    ): CommandHandler<*, *>

    @Binds
    @IntoMap
    @CommandHandlerKey(UpdatePremiumCommand::class)
    abstract fun bindUpdatePremiumCommandHandler(
        handler: UpdatePremiumCommandHandler
    ): CommandHandler<*, *>
}