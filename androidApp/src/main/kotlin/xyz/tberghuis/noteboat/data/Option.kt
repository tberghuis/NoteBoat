package xyz.tberghuis.noteboat.data

import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// better name than option???
@Entity(
  tableName = "option",
)
data class Option(
  @PrimaryKey
  @ColumnInfo(name = "option_key")
  val optionKey: String,
  @NonNull @ColumnInfo(name = "option_value") val optionValue: String
)

@Dao
interface OptionDao {
  @Query("SELECT * FROM option WHERE option_key = :optionKey")
  fun getOptionFlow(optionKey: String): Flow<Option>

  @Query("SELECT option_value FROM option WHERE option_key = :optionKey")
  fun getOption(optionKey: String): String

  @Query("update option set option_value = :optionValue where option_key = :optionKey")
  suspend fun updateOption(optionKey: String, optionValue: String)
}