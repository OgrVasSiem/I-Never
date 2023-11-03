package com.foresko.game.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foresko.game.core.rest.Card
import com.foresko.game.core.rest.GameModel
import com.foresko.game.core.rest.Question
import com.foresko.game.core.rest.QuestionCount
import kotlinx.coroutines.flow.Flow


@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT * FROM cards")
    suspend fun getAllCards(): List<Card>

    @Query("SELECT * FROM questions WHERE cardId = :cardId")
    suspend fun getQuestionsForCard(cardId: Long): List<Question>

    @Query("DELETE FROM cards")
    suspend fun clearAllCards()

    @Query("DELETE FROM questions")
    suspend fun clearAllQuestions()

    @Query("SELECT cardId, COUNT(id) AS textCount FROM questions GROUP BY cardId")
    suspend fun getQuestionCountForCards(): List<QuestionCount>

    @Query("""
   SELECT cards.name AS categoryName, 
          questions.text AS question, 
          null AS colorInt
   FROM cards 
   INNER JOIN questions ON questions.cardId = cards.id 
   WHERE cardId IN (:ids) 
   ORDER BY RANDOM()""")
    fun getPaginatedQuestionsWithCards(ids: List<Long>): Flow<List<GameModel>>


}
