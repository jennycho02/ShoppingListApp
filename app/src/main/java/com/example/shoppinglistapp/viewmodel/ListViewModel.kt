package com.example.shoppinglistapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shoppinglistapp.data.AppDatabase
import com.example.shoppinglistapp.data.ListDAO
import com.example.shoppinglistapp.data.ListItem


class ListViewModel(application: Application) :
    AndroidViewModel(application) {

    val allItems: LiveData<List<ListItem>>

    private var listDAO: ListDAO

    init {
        listDAO = AppDatabase.getInstance(application).
          listDao()
        allItems = listDAO.getAllItems()
    }

    fun insertItem(item: ListItem)  {
        Thread {
            listDAO.addItem(item)
        }.start()
    }

    fun deleteItem(item: ListItem)  {
        Thread {
            listDAO.deleteItem(item)
        }.start()
    }

    fun deleteAllItems()  {
        Thread {
            listDAO.deleteAllItems()
        }.start()
    }

    fun updateItem(item: ListItem) {
        Thread {
            listDAO.updateItem(item)
        }.start()
    }
}