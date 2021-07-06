package com.hefny.hady.dailyforecast.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hefny.hady.dailyforecast.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val TAG = "AppDebug"
    lateinit var uiCommunicationListener: UICommunicationListener
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn.setOnClickListener {
            viewModel.setCityName("cairo")
        }
        viewModel.currentWeatherData.observe(viewLifecycleOwner, { dataResource ->
            // handle loading
            Log.d(TAG, "onViewCreated: loading: ${dataResource.loading}")
            // handle success (loading data from remote data source and cache)
            dataResource.data?.peekContent()?.let {
                Log.d(TAG, "onViewCreated: success: ${it}")
            }
            // handle loading data from cache only
            dataResource.message?.let {
                Log.d(TAG, "onViewCreated: success cache: $it")
            }
            // handle error
            dataResource.error?.peekContent()?.let {
                Log.d(TAG, "onViewCreated: error: $it")
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: $context must implement UICommunicationListener")
        }
    }
}