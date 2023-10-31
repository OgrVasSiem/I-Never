package com.game.INever.ui.destination.game

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.INever.core.rest.GameModel
import com.game.INever.dataBase.repositories.CardRepository
import com.game.INever.ui.destinations.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val idsList = savedStateHandle.navArgs<GameScreenNavArgs>().ids

    private var questionsListNew by mutableStateOf(emptyList<GameModel>())

    val displayList by derivedStateOf { questionsListNew.take(4) }
    private val _displayList = MutableStateFlow<List<GameModel>>(emptyList())

    private val _allCardsLoaded = MutableStateFlow(false)
    val allCardsLoaded = _allCardsLoaded.asStateFlow()

    private var _currentIndex = MutableStateFlow(0)
    var currentIndex = _currentIndex.asStateFlow()

    private var _colorIndex = MutableStateFlow(0)
    val colorIndex = _colorIndex.asStateFlow()


    private var offset = 0

    init {
        viewModelScope.launch {
            loadCards()
        }
    }

    private suspend fun loadCards() {
        cardRepository.getPaginatedQuestionsWithCards(
            idsList = idsList.toList(),
        ).collectLatest { newQuestions ->
            questionsListNew = questionsListNew + newQuestions
            _displayList.value = questionsListNew.take(4)
            offset += newQuestions.size
            _currentIndex.value = _displayList.value.size - 1

        }
    }

    fun onCardSwiped() {
        questionsListNew = questionsListNew.drop(1)
        _displayList.value = questionsListNew.take(4)
        _colorIndex.value = (_colorIndex.value + 1) % cardColors.size  // cardColors - это ваш список цветов
    }

}
