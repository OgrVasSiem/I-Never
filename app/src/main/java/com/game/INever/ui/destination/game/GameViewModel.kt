package com.game.INever.ui.destination.game

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.game.INever.core.rest.QuestionWithCard
import com.game.INever.dataBase.repositories.CardRepository
import com.game.INever.ui.destinations.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val idsList = savedStateHandle.navArgs<GameScreenNavArgs>().getIdsList()

    // Список вопросов, который будет содержать все вопросы, связанные с переданными IDs.
    private val _questionsList = mutableStateOf<List<QuestionWithCard>>(emptyList())
    val questionsList: State<List<QuestionWithCard>> = _questionsList

    // Текущий индекс вопроса в списке вопросов.
    private val _currentIndex = mutableStateOf(0)
    val currentIndex: State<Int> = _currentIndex

    // Текущий вопрос.
    val currentQuestion: State<QuestionWithCard?> = derivedStateOf {
        _questionsList.value.getOrNull(_currentIndex.value)
    }

    init {
        viewModelScope.launch {
            val idsList = savedStateHandle.navArgs<GameScreenNavArgs>().getIdsList()
            val questionsFromDB = cardRepository.getQuestionsWithCards(idsList)

            // Заполняем список вопросов и перемешиваем их.
            _questionsList.value = questionsFromDB.shuffled()
        }
    }

        // Метод для перехода к следующему вопросу.
    fun nextQuestion() {
        if (_currentIndex.value < _questionsList.value.size - 1) {
            _currentIndex.value += 1
        } else {
            // Обработка, если достигли последнего вопроса. Например, возвращаемся к первому вопросу.
            _currentIndex.value = 0
        }
    }
}
