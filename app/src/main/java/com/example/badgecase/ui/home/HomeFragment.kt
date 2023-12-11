package com.example.badgecase.ui.home

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.badgecase.R
import com.example.badgecase.api.BadgeListRequest
import com.example.badgecase.api.PraiseListRequest
import com.example.badgecase.databinding.FragmentHomeBinding
import com.example.badgecase.models.request.badge.BadgeRequest
import com.example.badgecase.models.request.praise.PraiseRequest
import com.example.badgecase.models.response.badgelistresponse.BadgeListResult
import com.example.badgecase.models.response.praiselistresponse.PraiseResultItem
import com.example.badgecase.ui.base.BaseFragment
import com.example.badgecase.ui.home.adapter.ListAdapter
import com.example.badgecase.ui.home.adapter.SliderRecyclerAdapter
import com.example.badgecase.ui.main.MainActivity
import com.example.badgecase.utils.SharedPrefManager
import com.example.badgecase.utils.calculateTotalPages
import com.example.badgecase.utils.formatNumberWithComma
import com.example.badgecase.utils.getBadgeIconMapping
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

/**
 * Created by AralBenli on 8.12.2023.
 */

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private val loginViewModel: HomeViewModel by viewModels()
    private var badgeList = arrayListOf<BadgeListResult>()
    private var praiseList = arrayListOf<PraiseResultItem>()
    private lateinit var sliderAdapter: SliderRecyclerAdapter
    private lateinit var listAdapter: ListAdapter
    private var averageRating: Double = 0.0
    private val badgeCountMap = mutableMapOf<Int, Int>()
    lateinit var layoutManager: GridLayoutManager

    override fun initViews() {
        (requireActivity() as MainActivity).statusBarTitle(true, "TAKDIR & TEŞEKKÜR")
        setupAdapters()
        initializeRequests()
        requests()
    }

    private fun setupAdapters() {
        with(binding) {
            sliderAdapter = SliderRecyclerAdapter(requireContext())
            listAdapter = ListAdapter(requireContext())
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            homeSliderRecyclerView.layoutManager = layoutManager
            homeSliderRecyclerView.isNestedScrollingEnabled = false
            homeSliderRecyclerView.setHasFixedSize(true)
            homeVerticalListRecyclerView.adapter = listAdapter
            homeSliderRecyclerView.adapter = sliderAdapter
            secondLoadHelper()
            scrollHelper()
            hideSliderOnScroll()
            tabListener()
        }
    }

    private fun initializeTabs(totalPages: Int) {
        with(binding) {
            homeTabLayout.removeAllTabs()
            for (i in 1..totalPages) {
                val tab = homeTabLayout.newTab()
                homeTabLayout.addTab(tab)
            }
        }
    }

    private fun tabListener() {
        binding.homeTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position ?: 0
                val target = (position * 4) - 1

                if (position == 0) {
                    binding.homeSliderRecyclerView.smoothScrollToPosition(position)
                } else {
                    binding.homeSliderRecyclerView.smoothScrollToPosition(target)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun scrollHelper() {
        binding.homeSliderRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            private var isScrollingRight = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dx > 0) {
                    isScrollingRight = true
                } else if (dx < 0) {
                    isScrollingRight = false
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                with(binding) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position = layoutManager.findFirstVisibleItemPosition()
                        val lastPosition = layoutManager.findLastVisibleItemPosition()

                        if (isScrollingRight && position != lastPosition) {
                            /*Log.d(
                                "listening",
                                "isScrollingRight && position != lastPosition state : last position ${position - 1}"
                            )*/
                            recyclerView.smoothScrollToPosition(lastPosition)
                            homeTabLayout.setScrollPosition(lastPosition - 2, 0f, true)
                        } else if (!isScrollingRight && position >= 1 && position <= lastPosition - 1) {
                            /*Log.d(
                                "listening",
                                "!isScrollingRight && position >= 1 && position <= lastPosition - 1 - state : last position $lastPosition"
                            )*/
                            val previousPosition = position - 1
                            if (layoutManager.findViewByPosition(previousPosition) != null) {
                                /*Log.d(
                                    "listening",
                                    "layoutManager.findViewByPosition(previousPosition) != null-state : $previousPosition"
                                )*/
                                homeTabLayout.setScrollPosition(previousPosition, 0f, true)
                            } else {
                                // Log.d("listening", "else-state : $position")
                                recyclerView.smoothScrollToPosition(position)
                                homeTabLayout.setScrollPosition(position - 1, 0f, true)
                            }
                        } else if (!isScrollingRight && position == 0) {
                            recyclerView.smoothScrollToPosition(0)
                            homeTabLayout.setScrollPosition(0, 0f, true)
                        }
                    }
                }
            }
        })
    }

    var scrollDown = false
    private fun hideSliderOnScroll() {
        with(binding) {
            homeVerticalListRecyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (scrollDown) {
                        sliderRoot.visibility = View.GONE
                    } else {
                        sliderRoot.visibility = View.VISIBLE
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 20) {
                        scrollDown = true
                    } else if (dy < -20) {
                        scrollDown = false
                    }
                }
            })
        }
    }

    private fun initializeRequests() {
        val queryBadge = BadgeListRequest("badgeList")
        loginViewModel.fetchBadgeList(queryBadge)

        val praiseRequest = PraiseListRequest("praiseList")
        loginViewModel.fetchPraiseList(praiseRequest)
    }

    private fun requests() {
        //View pager
        lifecycleScope.launchWhenStarted {
            loginViewModel._badgeListStateFlow.collectLatest { state ->
                state?.let {
                    badgeList = state.badgeList as ArrayList<BadgeListResult>
                    badgeList.map {
                        //Log.d("testTitle", it.title)
                        it.title
                    }
                }
            }
        }

        //Vertical recycler view
        lifecycleScope.launchWhenStarted {
            loginViewModel._praiseListStateFlow.collectLatest { state ->
                state?.let {
                    with(binding) {
                        praiseList = state.praiseResultItem as ArrayList<PraiseResultItem>
                        val sumPraiseRating = praiseList.sumOf { it.praiseRating.toDouble() }
                        //Log.d("sumPraiseRating", sumPraiseRating.toString())  //    1525 / 546 = 2.79

                        averageRating =
                            if (praiseList.isNotEmpty()) sumPraiseRating / praiseList.size else 0.0
                        //Log.d("sumPraiseRating", averageRating.toString())  // 2.79
                        homeListVRating.rating = averageRating.toFloat()
                        val badgeCount = praiseList.size
                        val totalBadgeText = "$badgeCount ${getString(R.string.count)}"
                        homeTotalBadgeTxt.text = totalBadgeText
                        val roundedNumber = averageRating
                        homeTotalPointTxt.text = roundedNumber.formatNumberWithComma()
                        val praiseItems = praiseList.map { praise ->
                            val id = praise.authorId.toInt()
                            val authorName = praise.relatedPerson[0].title
                            val authorTime = praise.created
                            val badgeTitle = praise.badge[0].lookupValue
                            val stars = praise.praiseRatingWithDot
                            val badgeIcon = praise.badge[0].lookupId
                            badgeCountMap[badgeIcon] = badgeCountMap.getOrDefault(badgeIcon, 0) + 1
                            //Log.d("badgesForTop", badgeCountMap.toString())
                            val overview = praise.message
                            //Log.d("ItemDebug", "authorName: $authorName, authorTime: $authorTime, badgeTitle: $badgeTitle, stars: $stars, overview: $overview")

                            val imageResId = getBadgeIconMapping()[badgeIcon] ?: R.drawable.m10
                            ListAdapter.ListItem(
                                id,
                                imageResId,
                                authorName,
                                authorTime,
                                badgeTitle,
                                stars.toFloat(),
                                overview
                            )
                        }
                        SharedPrefManager.savePraiseItems(requireContext(), praiseItems)
                        //Log.d("savedPref","$praiseItems")

                        listAdapter.submitData(praiseItems)
                        updateTopRecyclerView()
                    }
                }
            }
        }
        //Progress bar
        lifecycleScope.launchWhenStarted {
            loginViewModel._progressStateFlow.collect { showProgress ->
                if (showProgress) {
                    showLoadingProgress()
                } else {
                    dismissLoadingProgress()
                }
            }
        }
    }

    private fun updateTopRecyclerView() {
        with(binding) {
            val totalBadgeCount = badgeCountMap.values.sum()
            val sliderItems = mutableListOf<SliderRecyclerAdapter.SliderItem>()

            badgeCountMap.forEach { (badgeIcon, count) ->
                val imageResId = getBadgeIconMapping()[badgeIcon] ?: R.drawable.m10
                val badge = badgeList.find { it.id == badgeIcon }
                val title = badge?.title ?: ""
                val maxRating = 5.0
                val rating = maxRating * (count.toDouble() / totalBadgeCount)
                //Log.d("ratingAverage", "##rating =$rating ,##count = $count , ##totalBadgeCount =$totalBadgeCount")
                val sliderItem = SliderRecyclerAdapter.SliderItem(
                    imageResId,
                    badgeIcon,
                    title,
                    badgeIcon,
                    rating.toFloat(),
                    count
                )
                /*Log.d(
                    "newItem", "calculating average and setting to top recyclerview ---> " +
                            "imageResId :$imageResId   badgeIcon :$badgeIcon title :$title ratingfloat :${rating.toFloat()} count :$count"
                )*/
                sliderItems.add(sliderItem)
                initializeTabs(sliderItems.size.calculateTotalPages())
                if (sliderItems.isNotEmpty()) {
                    val halfSize = sliderItems.size / 2
                    val part1 = sliderItems.subList(halfSize, sliderItems.size)
                    val part2 = sliderItems.subList(0, halfSize)
                    sliderAdapter.submitData(part1, part2)
                    homeTabLayout.visibility = View.VISIBLE
                    SharedPrefManager.saveSliderItems(requireContext(), sliderItems)
                }
            }
        }
    }

    private fun secondLoadHelper() {
        with(binding) {
            val lastKnownSliderItems = SharedPrefManager.getSliderItems(requireContext())
            val lastKnownListItems = SharedPrefManager.getPraiseItems(requireContext())
            //Log.d("whatigotfromshared","$lastKnownListItems")

            if (lastKnownListItems.isNotEmpty()) {
               listAdapter.submitData(lastKnownListItems)
            }
            //Log.d("prefresult", lastKnownSliderItems.toString())
            if (lastKnownSliderItems.isNotEmpty()) {
                initializeTabs(lastKnownSliderItems.size.calculateTotalPages())
                val halfSize = lastKnownSliderItems.size / 2
                val part1 = lastKnownSliderItems.subList(halfSize, lastKnownSliderItems.size)
                val part2 = lastKnownSliderItems.subList(0, halfSize)
                sliderAdapter.submitData(part1, part2)
                homeTabLayout.visibility = View.VISIBLE
            }
        }
    }
}