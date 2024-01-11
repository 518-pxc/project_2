package com.example.myapplication


import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import org.florescu.android.rangeseekbar.RangeSeekBar

class MainActivity : AppCompatActivity() {
    private lateinit var switch1: SwitchCompat
    private lateinit var switch2: SwitchCompat
    private lateinit var switch3: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val progressBar = findViewById<DoubleThumbProgressBar>(R.id.progressBar)
        progressBar.progress = 50F // 设置第一个thumb的位置
        progressBar.secondaryProgress = 75F // 设置第二个thumb的位置



        switch1 = findViewById(R.id.switch1)
        switch2= findViewById(R.id.switch2)
        switch3 = findViewById(R.id.switch3)

        //保存点击事件
        val textView2 = findViewById<TextView>(R.id.textView0)
        textView2.setOnClickListener {
            // 处理点击事件
        }

        //返回按钮点击事件
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // 编写点击事件的逻辑
        }
    }
}