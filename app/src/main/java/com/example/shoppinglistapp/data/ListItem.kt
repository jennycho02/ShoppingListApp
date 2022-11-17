package com.example.shoppinglistapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "necessities")
data class ListItem (
    @PrimaryKey(autoGenerate = true) var _itemID: Long?,
    @ColumnInfo(name = "itemtitle") var itemTitle: String,
    @ColumnInfo(name = "itemprice") var itemPrice: String,
    @ColumnInfo(name = "itemdesc") var itemDesc: String,
    @ColumnInfo(name = "wasBought") var wasBought: Boolean,
    @ColumnInfo(name = "itemcategory") var itemCat: Int) : java.io.Serializable {
}