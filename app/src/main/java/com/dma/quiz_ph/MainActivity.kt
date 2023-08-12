package com.dma.quiz_ph

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.dma.quiz_ph.ViewModel.QnAViewModel
import com.dma.quiz_ph.databinding.ActivityMainBinding
import com.dma.quiz_ph.databinding.ActivityQuizDetailsBinding
import com.dma.quiz_ph.utils.Constant
import com.dma.quiz_ph.utils.DataStatus
import com.dma.quiz_ph.utils.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.apply {
            btnStart.setOnClickListener {
                startActivity(Intent(this@MainActivity, QuizDetailsActivity::class.java))
            }

            txtPoint.text = PreferenceHelper.retriveData(this@MainActivity, Constant.HIGHEST_SCORE)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}