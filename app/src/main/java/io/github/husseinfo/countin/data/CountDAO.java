package io.github.husseinfo.countin.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountDAO {
    @Query("SELECT * FROM count")
    List<CountModel> getAll();


    @Query("SELECT * FROM count WHERE id = :id LIMIT 1")
    CountModel findById(int id);

    @Insert
    void insertAll(CountModel... counts);

    @Delete
    void delete(CountModel count);
}
