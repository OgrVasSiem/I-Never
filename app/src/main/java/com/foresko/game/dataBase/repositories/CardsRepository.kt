package com.foresko.game.dataBase.repositories

import com.foresko.game.core.rest.Card
import com.foresko.game.core.rest.CardsRequest
import com.foresko.game.core.rest.GameModel
import com.foresko.game.core.rest.NetworkCard
import com.foresko.game.core.rest.Question
import com.foresko.game.dataBase.dao.CardDao
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