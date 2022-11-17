package com.example.shoppinglistapp

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.SearchView
import androidx.core.animation.doOnEnd
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shoppinglistapp.adapter.ShoppingListAdapter
import com.example.shoppinglistapp.data.AppDatabase
import com.example.shoppinglistapp.data.ListItem
import com.example.shoppinglistapp.databinding.ActivityScrollingBinding
import com.example.shoppinglistapp.dialog.SLDialog
import com.example.shoppinglistapp.touch.ListRecyclerTouchCallback
import com.example.shoppinglistapp.viewmodel.ListViewModel
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class ScrollingActivity : AppCompatActivity(), SLDialog.SLDialogHandler {
    companion object {
        const val KEY_TODO_EDIT = "KEY_TODO_EDIT"
        const val PREF_SETTINGS = "SETTINGS"
        const val KEY_FIRST_START = "KEY_FIRST_START"
    }

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var shopListsViewModel: ListViewModel

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shopListsViewModel = ViewModelProvider(this)[ListViewModel::class.java]

        initRecyclerView()

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        binding.fabAddItem.setOnClickListener { view ->
            val listDialog = SLDialog()
            listDialog.show(supportFragmentManager, "TodoDialog")
        }

        binding.fabDeleteAll.setOnClickListener{
            shopListsViewModel.deleteAllItems()
        }

        if (isItFirstStart()) {
            MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText("Create item")
                .setSecondaryText("Click here to add new item")
                .setTarget(binding.fabAddItem)
                .show()
        }
        saveAppWasStarted()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)

        return super.onCreateOptionsMenu(menu)
    }

    fun saveAppWasStarted() {
        val sp = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(KEY_FIRST_START, false)
        editor.commit()
    }

    fun isItFirstStart() : Boolean {
        val sp = getSharedPreferences(PREF_SETTINGS, MODE_PRIVATE)
        return sp.getBoolean(KEY_FIRST_START, true)
    }

    fun initRecyclerView() {
        adapter = ShoppingListAdapter(this, shopListsViewModel)
        binding.recyclerShopList.adapter = adapter

        val callback: ItemTouchHelper.Callback = ListRecyclerTouchCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.recyclerShopList)
        shopListsViewModel.allItems.observe(this) { todos ->
            adapter.submitList(todos)
        }
    }

    fun showEditDialog(itemToEdit: ListItem) {
        val dialog = SLDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_TODO_EDIT, itemToEdit)
        dialog.arguments = bundle

        dialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }

    override fun itemCreated(listItem: ListItem) {
        shopListsViewModel.insertItem(listItem)

        Snackbar.make(
            binding.root,
            "Item added",
            Snackbar.LENGTH_LONG
        ).setAction(
            "Undo"
        ) {
            adapter.deleteLast()
        }.show()
    }

    override fun itemUpdated(listItem: ListItem) {
        shopListsViewModel.updateItem(listItem)
    }

    public fun setPriceText(message: String) {
        val dialog = SLDialog()

    }
}