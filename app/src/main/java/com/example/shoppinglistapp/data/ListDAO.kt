package com.example.shoppinglistapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListDAO {

    @Query("SELECT * FROM necessities")
    fun getAllItems() : LiveData<List<ListItem>>

    @Insert
    fun addItem(necessities: ListItem)

    @Delete
    fun deleteItem(necessities: ListItem)

    @Query("DELETE FROM necessities")
    fun deleteAllItems()

    @Update
    fun updateItem(necessities: ListItem)
}