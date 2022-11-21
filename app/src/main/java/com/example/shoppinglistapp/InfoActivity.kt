package com.example.shoppinglistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppinglistapp.databinding.ActivityInfoBinding
import android.content.Context

class InfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityInfoBinding
    val KEY_DESC = "KEY_DESC"
    val KEY_PRICE = "KEY_PRICE"
    val KEY_NAME = "KEY_NAME"
    val KEY_CATEGORY = "KEY_CATEGORY"
    val KEY_PURCHASED = "KEY_PURCHASED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

        if (bundle != null) {
            binding.itemName.text = bundle.getString(KEY_NAME)
            binding.itemPrice.text = getString(
                R.string.itemPriceString, bundle.getString(KEY_PRICE).toString().toFloat())
            binding.itemDesc.text = bundle.getString(KEY_DESC)
            binding.cbPurchased.isChecked = bundle.getBoolean(KEY_PURCHASED)

            when (bundle.getInt(KEY_CATEGORY)) {
                0 -> {
                    binding.itemCategory.text = "Food"
                    binding.itemIcon.setImageResource(
                        R.drawable.groceries)
                }
                1 -> {
                    binding.itemCategory.text = "Clothing"
                    binding.itemIcon.setImageResource(
                        R.drawable.clothing)
                }
                2 -> {
                    binding.itemCategory.text = "Travel"
                    binding.itemIcon.setImageResource(
                        R.drawable.travel)
                }
            }
        }

        binding.ibBack.setOnClickListener {
            val intentResult = Intent()
            setResult(RESULT_CANCELED, intentResult)
            finish()
        }
    }
}