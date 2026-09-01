package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {
    @Query("SELECT * FROM lists ORDER BY updatedAt DESC")
    fun getAllLists(): Flow<List<ListEntity>>

    @Query("SELECT * FROM lists ORDER BY updatedAt DESC")
    fun getAllListsSync(): List<ListEntity>

    @Query("SELECT * FROM lists WHERE id = :id LIMIT 1")
    suspend fun getListById(id: Long): ListEntity?

    @Query("SELECT * FROM lists WHERE id = :id LIMIT 1")
    fun observeListById(id: Long): Flow<ListEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity): Long

    @Update
    suspend fun updateList(list: ListEntity)

    @Delete
    suspend fun deleteList(list: ListEntity)

    @Query("DELETE FROM lists WHERE id = :id")
    suspend fun deleteListById(id: Long)
}
