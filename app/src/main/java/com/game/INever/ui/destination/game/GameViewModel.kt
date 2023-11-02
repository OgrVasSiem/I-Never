package com.game.INever.ui.destination.game

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
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

    val _currentIndex = mutableStateOf(0)
    var currentIndex: State<Int> = _currentIndex

    private var _colorIndex = MutableStateFlow(0)
    val colorIndex = _colorIndex.asStateFlow()


    private var offset = 0

    val cardColors = listOf(
        Color(0xFFE57373), Color(0xFFF06292), Color(0xFFBA68C8), Color(0xFF9575CD),
        Color(0xFF7986CB), Color(0xFF64B5F6), Color(0xFF4FC3F7), Color(0xFF4DD0E1),
        Color(0xFF4DB6AC), Color(0xFF81C784), Color(0xFFAED581), Color(0xFFFFD54F),
        Color(0xFFFFB74D), Color(0xFFFF8A65), Color(0xFFA1887F), Color(0xFF90A4AE)
    )

    init {
        viewModelScope.launch {
            loadCards()
        }
    }

    private suspend fun loadCards() {
        cardRepository.getPaginatedQuestionsWithCards(
            idsList = idsList.toList(),
        ).collectLatest { newQuestions ->

            val questionsWithColor = newQuestions.mapIndexed { index, gameModel ->
                val colorInt = cardColors[(_colorIndex.value + index) % cardColors.size].toArgb()
                gameModel.copy(colorInt = colorInt)
            }

            questionsListNew = questionsListNew + questionsWithColor
            _displayList.value = questionsListNew.take(4)
            offset += questionsWithColor.size
            _currentIndex.value = _displayList.value.size - 1
            _colorIndex.value = (_colorIndex.value + questionsWithColor.size) % cardColors.size
        }
    }
    val baseOffsets = listOf(0.dp, (-40).dp, (-80).dp, (-120).dp)

    val offsets = List(4) { i -> mutableStateOf(baseOffsets[i]) }

    fun onCardSwiped() {
        viewModelScope.launch {
            // Удаление верхней карты
            questionsListNew = questionsListNew.drop(1)
        }
    }

}