package com.example.shoppinglistapp.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.ScrollingActivity
import com.example.shoppinglistapp.data.ListItem
import com.example.shoppinglistapp.databinding.SlDialogBinding

class SLDialog : DialogFragment() {

    interface SLDialogHandler {
        fun itemCreated(item: ListItem)

        fun itemUpdated(item: ListItem)
    }

    lateinit var sLDialogHandler: SLDialogHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SLDialogHandler) {
            sLDialogHandler = context
        } else {
            throw java.lang.RuntimeException(
                "The activity does not implement the ListHandler interface")
        }
    }

    private var isEditMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        // Are we in edit mode? - Have we received a Todo object to edit?
        if (arguments != null && requireArguments().containsKey(
                ScrollingActivity.KEY_TODO_EDIT)) {
            isEditMode = true
            dialogBuilder.setTitle("Edit Item")
        } else {
            isEditMode = false
            dialogBuilder.setTitle("New Item")
        }

        val dialogViewBinding = SlDialogBinding.inflate(
            requireActivity().layoutInflater)
        dialogBuilder.setView(dialogViewBinding.root)

        val categoriesAdapter = ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.categories, android.R.layout.simple_spinner_item
        )
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogViewBinding.spinnerCategories.adapter = categoriesAdapter

        //pre-fill the dialog if we are in edit mode
        if (isEditMode) {
            val itemToEdit =
                requireArguments().getSerializable(
                    ScrollingActivity.KEY_TODO_EDIT) as ListItem

            dialogViewBinding.etItemTitle.setText(itemToEdit.itemTitle)
            dialogViewBinding.etItemPrice.setText(itemToEdit.itemPrice)
            dialogViewBinding.etItemDesc.setText(itemToEdit.itemDesc)
            dialogViewBinding.cbPurchased.isChecked = itemToEdit.wasBought
            dialogViewBinding.spinnerCategories.setSelection(itemToEdit.itemCat)
        }

        dialogBuilder.setPositiveButton("Ok") {
                dialog, which ->

            if (isEditMode) {
                val itemToEdit =
                    (requireArguments().getSerializable (
                        ScrollingActivity.KEY_TODO_EDIT
                    ) as ListItem).copy(
                        itemTitle = dialogViewBinding.etItemTitle.text.toString(),
                        itemPrice = dialogViewBinding.etItemPrice.text.toString(),
                        itemDesc = dialogViewBinding.etItemDesc.text.toString(),
                        wasBought = dialogViewBinding.cbPurchased.isChecked,
                        itemCat = dialogViewBinding.spinnerCategories.selectedItemPosition
                    )
                sLDialogHandler.itemUpdated(itemToEdit)
            } else {
                sLDialogHandler.itemCreated(
                    ListItem(
                        null,
                        dialogViewBinding.etItemTitle.text.toString(),
                        dialogViewBinding.etItemPrice.text.toString(),
                        dialogViewBinding.etItemDesc.text.toString(),
                        dialogViewBinding.cbPurchased.isChecked,
                        dialogViewBinding.spinnerCategories.selectedItemPosition
                    )
                )
            }
        }
        dialogBuilder.setNegativeButton("Cancel") {
                dialog, which ->
        }

        return dialogBuilder.create()
    }

}