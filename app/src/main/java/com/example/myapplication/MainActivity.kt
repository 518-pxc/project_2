package com.example.myapplication


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var switchStateOne = false
    private var switchStateTwo = false
    private var switchStateThree = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT
        }
        // 加载自定义的XML布局文件
        val customStatusBar = LayoutInflater.from(this).inflate(R.layout.status_bar_layout, null)
        // 将自定义的XML布局文件设置为沉浸式状态栏的内容
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.TOP
        val content = findViewById<FrameLayout>(android.R.id.content)
        content.addView(customStatusBar, params)

        // 获取年龄双滑块直线进度条和两个 TextView 控件
        val progressBarAge = binding.pbAge
        val tvAgeFirst = binding.tvAgeFirst
        val tvAgeSecond = binding.tvAgeSecond

        progressBarAge.setOnRangeChangedListener(object : DoubleThumbProgressBar.OnRangeChangedListener {
            override fun onRangeChanged(minValue: Int, maxValue: Int) {
                // 更新两个 TextView 控件的值
                tvAgeFirst.text = minValue.toString()
                tvAgeSecond.text = maxValue.toString()
            }
        })

        // 获取身高双滑块直线进度条和两个 TextView 控件
        val progressBarHeight = binding.pbHeight
        val tvHeightFirst = binding.tvHeightFirst
        val tvHeightSecond = binding.tvHeightSecond

        progressBarHeight.setOnRangeChangedListener(object : DoubleThumbProgressBar.OnRangeChangedListener {
            override fun onRangeChanged(minValue: Int, maxValue: Int) {
                // 更新两个 TextView 控件的值
                tvHeightFirst.text = minValue.toString()
                tvHeightSecond.text = maxValue.toString()
            }
        })

        // 获取体重双滑块直线进度条和两个 TextView 控件
        val progressBarWeight = binding.pbWeight
        val tvWeightFirst = binding.tvWeightFirst
        val tvWeightSecond = binding.tvWeightSecond

        progressBarWeight.setOnRangeChangedListener(object : DoubleThumbProgressBar.OnRangeChangedListener {
            override fun onRangeChanged(minValue: Int, maxValue: Int) {
                // 更新两个 TextView 控件的值
                tvWeightFirst.text = minValue.toString()
                tvWeightSecond.text = maxValue.toString()
            }
        })

        // 初始化开关状态
        updateSwitchStateOne()

        // 设置开关点击事件
        binding.ivClose.setOnClickListener {
            switchStateOne = !switchStateOne
            updateSwitchStateOne()
        }

        // 初始化开关状态
        updateSwitchStateTwo()

        // 设置开关点击事件
        binding.ivNew.setOnClickListener {
            switchStateTwo = !switchStateTwo
            updateSwitchStateTwo()
        }

        // 初始化开关状态
        updateSwitchStateThree()

        // 设置开关点击事件
        binding.ivTrue.setOnClickListener {
            switchStateThree = !switchStateThree
            updateSwitchStateThree()
        }
    }
    private fun updateSwitchStateOne() {
        if (switchStateOne) {
            binding.ivClose.setImageResource(R.drawable.ic_switch_open)
        } else {
            binding.ivClose.setImageResource(R.drawable.ic_switch_close)
        }
    }

    private fun updateSwitchStateTwo() {
        if (switchStateTwo) {
            binding.ivNew.setImageResource(R.drawable.ic_switch_open)
        } else {
            binding.ivNew.setImageResource(R.drawable.ic_switch_close)
        }
    }

    private fun updateSwitchStateThree() {
        if (switchStateThree) {
            binding.ivTrue.setImageResource(R.drawable.ic_switch_open)
        } else {
            binding.ivTrue.setImageResource(R.drawable.ic_switch_close)
        }
    }
}