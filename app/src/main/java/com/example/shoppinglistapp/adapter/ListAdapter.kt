package com.example.shoppinglistapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.ScrollingActivity
import com.example.shoppinglistapp.data.ListItem
import com.example.shoppinglistapp.databinding.ListRowBinding
import com.example.shoppinglistapp.dialog.SLDialog
import com.example.shoppinglistapp.touch.ListTouchHelperCallback
import com.example.shoppinglistapp.viewmodel.ListViewModel
import java.util.*

class ShoppingListAdapter(
    private val context: Context,
    private val listViewModel: ListViewModel
    ) : ListAdapter<ListItem, ShoppingListAdapter.ViewHolder>(ListDiffCallback()),
    ListTouchHelperCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listRowBinding = ListRowBinding.inflate(
            LayoutInflater.from(context),
            parent, false
        )
        return ViewHolder(listRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(holder.adapterPosition)
        holder.bind(currentItem)
    }

    fun deleteLast() {
        val lastItem = getItem(currentList.lastIndex)
        listViewModel.deleteItem(lastItem)
    }

    override fun onDismissed(position: Int) {
        listViewModel.deleteItem(getItem(position))
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val tmpList = mutableListOf<ListItem>()
        tmpList.addAll(currentList)
        Collections.swap(tmpList, fromPosition, toPosition)
        submitList(tmpList)
    }

    inner class ViewHolder(private val listRowBinding: ListRowBinding) :
        RecyclerView.ViewHolder(listRowBinding.root) {
        fun bind(listItem: ListItem) {
            listRowBinding.tvName.text = listItem.itemTitle
            listRowBinding.cvItem.isChecked = listItem.wasBought

            listRowBinding.tvPrice.text = (context as ScrollingActivity).getString(
                R.string.itemPriceString, listItem.itemPrice.toFloat())

            listRowBinding.btnDelete.setOnClickListener {
                listViewModel.deleteItem(listItem)
            }

            listRowBinding.ibEdit.setOnClickListener {
                (context as ScrollingActivity).showEditDialog(listItem)
            }

            listRowBinding.ibInfo.setOnClickListener {
                (context as ScrollingActivity).showItemDesc(listItem)
            }

            listRowBinding.cvItem.setOnClickListener {
                listRowBinding.cvItem.toggle()
                listItem.wasBought = listRowBinding.cvItem.isChecked
                listViewModel.updateItem(listItem)
            }
            when (listItem.itemCat) {
                0 -> {
                    listRowBinding.ivIcon.setImageResource(
                        R.drawable.groceries)
                }
                1 -> {
                    listRowBinding.ivIcon.setImageResource(
                        R.drawable.clothing)
                }
                2 -> {
                    listRowBinding.ivIcon.setImageResource(
                        R.drawable.travel)
                }
            }
        }
    }
}

class ListDiffCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem._itemID == newItem._itemID
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem == newItem
    }
}