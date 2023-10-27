package com.game.INever.core.rest

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class CardsResponse(
    val topics: List<NetworkCard>
)
data class NetworkCard(
    val name: String,
    val color: String,
    val image: String,
    val freeTopic: Boolean,
    val alertImage: String,
    val questions: List<String>
)

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val color: String,
    val image: String,
    val freeTopic: Boolean,
    val alertImage: String,
)

@Entity
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,  // Используем один и тот же идентификатор
    val cardId: Long,   // Этот идентификатор будет ссылаться на идентификатор карточки
    val text: String,
)


fun NetworkCard.toCard(): Card {
    return Card(
        name = this.name,
        color = this.color,
        image = this.image,
        freeTopic = this.freeTopic,
        alertImage = this.alertImage,
    )
}

data class QuestionWithCard(
    @Embedded
    val question: Question,
    @Relation(parentColumn = "cardId", entityColumn = "id")
    val card: Card
)

data class QuestionCount(
    val cardId: Long,
    val textCount: Int
)



