package com.example.badgecase.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.badgecase.R

/**
 * Created by AralBenli on 8.12.2023.
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    lateinit var binding: VB
    private lateinit var progressDialog : Dialog

    abstract fun getViewBinding(): VB
    abstract fun initViews()

    private var savedInstanceView: View? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceView == null) {
            binding = getViewBinding()
            savedInstanceView = binding.root
            savedInstanceView
        } else {
            savedInstanceView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    fun showLoadingProgress() {
        if (!::progressDialog.isInitialized) {
            progressDialog = Dialog(requireContext())
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressDialog.setContentView(R.layout.loading_progress)
            progressDialog.setCancelable(false)
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.show()
    }
    fun dismissLoadingProgress() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}
