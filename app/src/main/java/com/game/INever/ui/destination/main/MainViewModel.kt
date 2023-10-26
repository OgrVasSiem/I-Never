package com.game.INever.ui.destination.main

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.api.Amplitude
import com.game.INever.core.rest.Card
import com.game.INever.core.rest.CardsRequest
import com.game.INever.core.rest.Question
import com.game.INever.core.rest.toCard
import com.game.INever.dataBase.repositories.CardRepository
import com.game.INever.dataStore.PremiumDataStore
import com.game.INever.ui.destination.main.components.CardState
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val premiumDataStore: PremiumDataStore,
    private val cardApiService: CardsRequest,
    private val cardRepository: CardRepository
) : ViewModel() {

    // Стейты
    private val _card = MutableStateFlow<List<Card>?>(null)
    val card: StateFlow<List<Card>?> = _card.asStateFlow()

    private val _cardStates = MutableStateFlow<List<CardState>>(listOf())
    val cardStates: StateFlow<List<CardState>> = _cardStates.asStateFlow()

    private val _questions = mutableStateOf(listOf<Question>())
    val questions: State<List<Question>> = _questions

    var premiumEndDateInEpochMilli by mutableLongStateOf(0L)
    var premiumIsActive by mutableStateOf<Boolean?>(null)
        private set



    init {
        Amplitude.getInstance().logEvent("premium_screen")

        viewModelScope.launch {
            fetchAndCacheCards()
            loadCardsFromDatabase()
            monitorPremiumStatus()
        }
    }


    private suspend fun loadCardsFromDatabase() {
        try {
            _card.value = cardRepository.getAllCards()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private suspend fun fetchAndCacheCards() {
        try {
            val fetchedCards = cardApiService.infoGet().topics
            cardRepository.insertAll(fetchedCards)
            _card.value = fetchedCards.map { it.toCard() }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private suspend fun monitorPremiumStatus() {
        premiumDataStore.data.collectLatest {
            premiumEndDateInEpochMilli = it.expiryDateTime
            premiumIsActive = it.isActive
        }

        card.collect { cardList ->
            val updatedCardStates = cardList?.map { card ->
                CardState(
                    cardData = mutableStateOf(card),
                    isSelected = mutableStateOf(false),
                    showDialog = mutableStateOf(false)
                )
            } ?: listOf()
            _cardStates.value = updatedCardStates
        }
    }

    fun toggleCardSelected(card: Card) {
        val updatedStates = _cardStates.value.map {
            if (it.cardData.value == card) {
                it.isSelected.value = !it.isSelected.value
            }
            it
        }
        _cardStates.value = updatedStates
    }

    fun loadQuestionsForActiveCards(activeCards: List<Card>) {
        viewModelScope.launch {
            _questions.value = activeCards.flatMap { card ->
                cardRepository.getQuestionsForCard(card.id)
            }
        }
    }
}
