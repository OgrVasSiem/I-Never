package com.game.INever.dataBase.repositories

import com.game.INever.core.rest.Card
import com.game.INever.core.rest.CardsRequest
import com.game.INever.core.rest.NetworkCard
import com.game.INever.core.rest.Question
import com.game.INever.core.rest.QuestionWithCard
import com.game.INever.dataBase.dao.CardDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardDao: CardDao,
    private val cardsRequest: CardsRequest) {

    suspend fun fetchAndSaveCards() = withContext(Dispatchers.IO) {
        clearAllCards()
        val response = cardsRequest.infoGet()
        response.topics.forEach { networkCard ->
            saveNetworkCardToDb(networkCard)
        }
    }

    suspend fun insertAll(cards: List<NetworkCard>) {
        cards.forEach { saveNetworkCardToDb(it) }
    }

    private suspend fun saveNetworkCardToDb(networkCard: NetworkCard) {
        val cardToInsert = Card(
            name = networkCard.name,
            color = networkCard.color,
            image = networkCard.image,
            freeTopic = networkCard.freeTopic,
            alertImage = networkCard.alertImage
        )
        val cardId = cardDao.insert(cardToInsert)

        val questions = networkCard.questions.map { questionText ->
            Question(
                cardId = cardId,
                text = questionText
            )
        }

        cardDao.insertQuestions(questions)
    }

    suspend fun getAllCards(): List<Card> {
        return cardDao.getAllCards()
    }

    suspend fun getQuestionsWithCards(ids: List<Long>): List<QuestionWithCard> {
        return cardDao.getQuestionsWithCards(ids)
    }

    suspend fun getQuestionsForCard(cardId: Long): List<Question> {
        return cardDao.getQuestionsForCard(cardId)
    }

    suspend fun clearAllCards() {
        cardDao.clearAllCards()
        cardDao.clearAllQuestions()
    }
}
