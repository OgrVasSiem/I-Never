package com.foresko.gamenever.dataBase.repositories

import com.foresko.gamenever.core.rest.Card
import com.foresko.gamenever.core.rest.CardsRequest
import com.foresko.gamenever.core.rest.GameModel
import com.foresko.gamenever.core.rest.NetworkCard
import com.foresko.gamenever.core.rest.Question
import com.foresko.gamenever.dataBase.dao.CardDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardDao: CardDao,
    private val cardsRequest: CardsRequest
) {

    suspend fun fetchAndSaveCards() = withContext(Dispatchers.IO) {
        clearAllCards()
        val response = cardsRequest.infoGet()
        response.topics.forEach { networkCard ->
            saveNetworkCardToDb(networkCard)
        }
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

    suspend fun getQuestionsForCard(cardId: Long): List<Question> {
        return cardDao.getQuestionsForCard(cardId)
    }

    suspend fun clearAllCards() {
        cardDao.clearAllCards()
        cardDao.clearAllQuestions()
    }

    suspend fun getPaginatedQuestionsWithCards(
        idsList: List<Long>,
    ): Flow<List<GameModel>> {
        return withContext(Dispatchers.IO) {
            cardDao.getPaginatedQuestionsWithCards(idsList)
        }
    }
}
