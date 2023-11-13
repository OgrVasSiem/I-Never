package com.foresko.gamenever.core.rest

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val color: String,
    val image: String,
    val freeTopic: Boolean,
    val alertImage: String,
)

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = Card::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("cardId"),
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val cardId: Long,
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

data class QuestionCount(
    val cardId: Long,
    val textCount: Int
)

data class GameModel(
    val categoryName: String,
    val question: String,
    val colorInt: Int?
)


