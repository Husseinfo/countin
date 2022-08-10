package io.github.husseinfo.countin.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CountDAO {
    @get:Query("SELECT * FROM count")
    val all: List<CountModel>?

    @Query("SELECT * FROM count WHERE id = :id LIMIT 1")
    fun findById(id: Int): CountModel?

    @Insert
    fun insertAll(vararg counts: CountModel?)

    @Delete
    fun delete(count: CountModel?)
}
