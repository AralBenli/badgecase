package com.example.badgecase.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.badgecase.databinding.ItemListBinding
import com.example.badgecase.utils.parseDate

/**
 * Created by AralBenli on 8.12.2023.
 */
class ListAdapter(private val context: Context) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    private val praiseList = mutableListOf<ListItem>()


    inner class ViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListItem) {
            binding.apply {
                val dateString = item.time
                val formatString = "M/d/yyyy h:mm a"
                if (dateString !=null) {
                    val (date, timeSuffix) = dateString.parseDate(formatString)
                    if (date != null) {
                        authorTime.text = timeSuffix
                    } else {
                        authorTime.text = ""
                    }
                }
                authorName.text = item.authorName
                if (item.imageResName!=null) listBadgeIcon.setImageResource(item.imageResName)
                listBadgeTitle.text = item.badgeTitle
                if (item.stars !=null) authorRating.rating = item.stars
                listOverview.text = item.overview
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val data = praiseList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return praiseList.size
    }

    fun submitData(newList: List<ListItem>) {
        praiseList.clear()
        praiseList.addAll(newList)
        notifyDataSetChanged()
    }

    data class ListItem(
        val id: Int,
        val imageResName: Int,
        val authorName: String?,
        val time: String?,
        val badgeTitle: String?,
        val stars: Float,
        val overview: String?
    )


}