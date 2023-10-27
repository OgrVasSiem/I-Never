package com.game.INever.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.game.INever.core.rest.Card
import com.game.INever.core.rest.Question
import com.game.INever.core.rest.QuestionCount
import com.game.INever.core.rest.QuestionWithCard


@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT * FROM Card")
    suspend fun getAllCards(): List<Card>

    @Query("SELECT * FROM Question WHERE cardId = :cardId")
    suspend fun getQuestionsForCard(cardId: Long): List<Question>

    @Query("SELECT * FROM Question INNER JOIN Card ON Question.cardId = Card.id WHERE Card.id IN (:ids)")
    suspend fun getQuestionsWithCards(ids: List<Long>): List<QuestionWithCard>

    @Query("DELETE FROM Card")
    suspend fun clearAllCards()

    @Query("DELETE FROM Question")
    suspend fun clearAllQuestions()

    @Query("SELECT cardId, COUNT(id) AS textCount FROM Question GROUP BY cardId")
    suspend fun getQuestionCountForCards(): List<QuestionCount>

}
