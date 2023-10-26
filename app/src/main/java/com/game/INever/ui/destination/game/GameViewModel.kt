package com.game.INever.ui.destination.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val currentQuestion = mutableStateOf<String?>(null)

    private val pageSize = 2
    val currentPage = mutableStateOf(0)


    init {


        viewModelScope.launch {
            val questionsWithCards = cardRepository.getQuestionsWithCards(savedStateHandle.navArgs<GameScreenNavArgs>().ids)

        }
    }

}
