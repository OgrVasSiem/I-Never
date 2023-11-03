package com.foresko.game.ui.destination.game

import android.app.Activity
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foresko.game.core.ads.Ads
import com.foresko.game.core.rest.GameModel
import com.foresko.game.dataBase.repositories.CardRepository
import com.foresko.game.dataStore.PremiumDataStore
import com.foresko.game.ui.destination.main.components.CardState
import com.foresko.game.ui.destinations.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val ads: Ads,
    private val cardRepository: CardRepository,
    private val premiumDataStore: PremiumDataStore,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val idsList = savedStateHandle.navArgs<GameScreenNavArgs>().ids

    private var questionsListNew by mutableStateOf(emptyList<GameModel>())
    private val initialQuestionCount = mutableIntStateOf(0)

    private var swipeCount = 0

    val currentQuestionNumber = mutableIntStateOf(1)

    val displayList by derivedStateOf { questionsListNew.take(4) }
    private val _displayList = MutableStateFlow<List<GameModel>>(emptyList())

    private val _currentIndex = mutableIntStateOf(0)
    var currentIndex: State<Int> = _currentIndex

    private var _colorIndex = MutableStateFlow(0)
    val colorIndex = _colorIndex.asStateFlow()

    private val baseOffsets = listOf(0.dp, (-40).dp, (-80).dp, (-115).dp)

    val offsets = List(4) { i -> mutableStateOf(baseOffsets[i]) }

    private var offset = 0

    var premiumIsActive by mutableStateOf<Boolean?>(null)
        private set

    private var premiumEndDateInEpochMilli by mutableLongStateOf(0L)

    private val cardColors = listOf(
        Color(0xFFF4413C), Color(0xFF9968FF), Color(0xFFFEC133), Color(0xFF65B969),
        Color(0xFF3C64F4), Color(0xFFFF932F), Color(0xFFF03CF4), Color(0xFF5FBAC0),
        Color(0xFF3C43F4), Color(0xFFF57600), Color(0xFF2FB101), Color(0xFF599BFE),
        Color(0xFFFFB74D), Color(0xFF9C2EAE), Color(0xFFDB328E), Color(0xFF25B1D0)
    )

    init {
        viewModelScope.launch {
            loadCards()
            initAds()
            monitorPremiumStatus()
        }
    }
    private suspend fun monitorPremiumStatus() {
        premiumDataStore.data.collectLatest {
            premiumEndDateInEpochMilli = it.expiryDateTime
            premiumIsActive = it.isActive
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

            if (initialQuestionCount.intValue == 0) {
                initialQuestionCount.intValue = questionsWithColor.size
            }

            questionsListNew = questionsListNew + questionsWithColor
            _displayList.value = questionsListNew.take(4)
            offset += questionsWithColor.size
            _currentIndex.intValue = _displayList.value.size - 1
            _colorIndex.value = (_colorIndex.value + questionsWithColor.size) % cardColors.size
        }
    }

    fun getInitialQuestionCount(): Int {
        return initialQuestionCount.intValue
    }

    fun onCardSwiped(activity: Activity) {
        viewModelScope.launch {
            questionsListNew = questionsListNew.drop(1)
            currentQuestionNumber.intValue += 1

            swipeCount++
            /*if (swipeCount % 10 == 0) {
                showAds(activity)
            }*/
        }
    }

    private fun initAds() {
        if (ads.interstitialAd == null) {
            ads.initAds()
        }
    }
    private fun showAds(activity: Activity) {
        if (premiumIsActive == true) {
            return
        }
            ads.showAd(activity = activity) {
            }
    }
}