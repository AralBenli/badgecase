package com.example.badgecase.ui.home.adapter

/**
 * Created by AralBenli on 8.12.2023.
 */

import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.badgecase.R
import com.example.badgecase.databinding.ItemSliderBinding
import com.example.badgecase.models.TwoContents

class SliderRecyclerAdapter(private val context: Context) :
    RecyclerView.Adapter<SliderRecyclerAdapter.ViewHolder>() {

    var sliderItemList = mutableListOf<TwoContents>()

    init {
        val recyclerView = RecyclerView(context)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
    }

    inner class ViewHolder(private val binding: ItemSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(two: TwoContents) {
            binding.apply {
                val firstContent = two.firstContent
                val secondContent = two.secondContent
                //Log.d("testAdapter:::::::firstContent::::::::", firstContent.toString())
                //Log.d("testAdapter:::::::secondContent:::::::", secondContent.toString())

                if (firstContent != null) {
                    bindPraiseResultItem(
                        firstContent,
                        homeSliderTitle,
                        homeSliderImageView,
                        homeListVRating,
                        homeSliderTotalBadgeTxt
                    )
                }
                if (secondContent != null) {
                    bindPraiseResultItem(
                        secondContent,
                        homeSliderTitle2,
                        homeSliderImageView2,
                        homeListVRating2,
                        homeSliderTotalBadgeTxt2
                    )
                }
                root.visibility = if (firstContent != null || secondContent != null) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    private fun bindPraiseResultItem(
        praiseResultItem: SliderItem,
        titleView: TextView,
        imageView: ImageView,
        ratingBar: RatingBar,
        totalCountView: TextView
    ) {
        titleView.text = praiseResultItem.title
        imageView.setImageResource(praiseResultItem.imageResName)
        if (praiseResultItem.stars > 0) {
            ratingBar.visibility = View.VISIBLE
            ratingBar.rating = praiseResultItem.stars
        } else {
            ratingBar.visibility = View.INVISIBLE
        }
        val count = praiseResultItem.totalCount
        totalCountView.text = "$count Adet"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sliderItem = sliderItemList[position]
        holder.bind(sliderItem)
    }

    override fun getItemCount(): Int = sliderItemList.size


    fun submitData(part1: List<SliderItem>, part2: List<SliderItem>) {
        val newSliderItemList = mutableListOf<TwoContents>()

        for (i in 0 until maxOf(part1.size, part2.size)) {
            val firstContent = if (i < part1.size) part1[i] else null
            val secondContent = if (i < part2.size) part2[i] else null
            newSliderItemList.add(TwoContents(firstContent, secondContent))
            //  Log.d("follow" , "${newSliderItemList.size}")
        }

        sliderItemList.clear()
        sliderItemList.addAll(newSliderItemList)

        notifyDataSetChanged()
    }

    data class SliderItem(
        val imageResName: Int,
        val id: Int,
        val title: String,
        val ID: Int,
        val stars: Float,
        val totalCount: Int
    )
}

