package io.github.husseinfo.countin.data

import androidx.room.*

@Dao
interface CountDAO {
    @get:Query("SELECT * FROM count ORDER BY date DESC")
    val all: List<CountModel>?

    @Query("SELECT * FROM count WHERE id = :id LIMIT 1")
    fun findById(id: Int): CountModel?

    @Insert
    fun insertAll(vararg counts: CountModel)

    @Delete
    fun delete(count: CountModel)

    @Update
    fun update(model: CountModel)
}
