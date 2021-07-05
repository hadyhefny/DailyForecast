package com.hefny.hady.dailyforecast.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.hefny.hady.dailyforecast.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UICommunicationListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, MainFragment())
                .commit()
        }
    }

    override fun showProgressBar(isLoading: Boolean) {
        progressDialog.isVisible = isLoading
    }

    override fun showError(errorMessage: String) {
    }

    override fun hideKeyboard() {
        main_constraint_layout.requestFocus()
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(main_constraint_layout.rootView.windowToken, 0)
    }
}