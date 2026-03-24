package xyz.tberghuis.noteboat.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Fruittie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @SerialName("name")
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("calories")
    val calories: String,
)

data class CartItemDetails(
    val fruittie: Fruittie,
    val count: Int,
)
