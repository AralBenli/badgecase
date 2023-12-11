package com.example.badgecase.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.badgecase.ui.home.adapter.ListAdapter
import com.example.badgecase.ui.home.adapter.SliderRecyclerAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by AralBenli on 8.12.2023.
 */

object SharedPrefManager {
    private const val PREF_SLIDER_ITEMS = "pref_slider_item"
    private const val PREF_LIST_ITEMS = "pref_list_item"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    fun saveSliderItems(context: Context, sliderItems: List<SliderRecyclerAdapter.SliderItem>) {
        val jsonString = Gson().toJson(sliderItems)
        val preferences = getSharedPreferences(context)
        preferences.edit().putString(PREF_SLIDER_ITEMS, jsonString).apply()
    }

    fun getSliderItems(context: Context): List<SliderRecyclerAdapter.SliderItem> {
        val preferences = getSharedPreferences(context)
        val jsonString = preferences.getString(PREF_SLIDER_ITEMS, null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, object : TypeToken<List<SliderRecyclerAdapter.SliderItem>>() {}.type)
        } else {
            emptyList()
        }
    }

    fun savePraiseItems(context: Context, sliderItems: List<ListAdapter.ListItem>) {
        val jsonString = Gson().toJson(sliderItems)
        //Log.d("SharedPrefManager", "Saved JSON: $jsonString")
        val preferences = getSharedPreferences(context)
        preferences.edit().putString(PREF_LIST_ITEMS, jsonString).apply()
    }


    fun getPraiseItems(context: Context): List<ListAdapter.ListItem> {
        val preferences = getSharedPreferences(context)
        val jsonString = preferences.getString(PREF_LIST_ITEMS, null)
        return if (jsonString != null) {
            Gson().fromJson(jsonString, object : TypeToken<List<ListAdapter.ListItem>>() {}.type)
        } else {
            emptyList()
        }
    }
}

