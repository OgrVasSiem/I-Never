package com.game.INever.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.game.INever.core.rest.Card
import com.game.INever.core.rest.Question
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

    @Query("SELECT * FROM Question INNER JOIN Card ON Question.cardId = Card.id WHERE Question.cardId IN (:cardId)")
    suspend fun getQuestionsWithCards(cardId: List<Long>): List<QuestionWithCard>


}
