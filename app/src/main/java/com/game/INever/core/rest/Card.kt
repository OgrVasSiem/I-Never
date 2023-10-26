package com.game.INever.core.rest

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class NetworkCard(
    val name: String,
    val color: String,
    val image: String,
    val freeTopic: Boolean,
    val alertImage: String,
    val questions: List<String>
)

data class CardsResponse(
    val topics: List<NetworkCard>
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
    val id: Long = 0L,
    val cardId: Long,
    val text: String
)

fun NetworkCard.toCard(): Card {
    return Card(
        name = this.name,
        color = this.color,
        image = this.image,
        freeTopic = this.freeTopic,
        alertImage = this.alertImage
    )

}

data class QuestionWithCard(
    @Embedded
    val question: Question,
    @Relation(parentColumn = "cardId", entityColumn = "id")
    val card: Card
)
