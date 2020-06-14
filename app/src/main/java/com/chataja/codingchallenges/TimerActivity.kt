package com.chataja.codingchallenges

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_countdown.*

class TimerActivity: AppCompatActivity() {

    private val mHandler = Handler()
    private var time = 60
    private val delay = 1000
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        tvTime.text = "Total Count = $time"
        tvDelay.text = "Delay = ${(delay/1000)} Sec"

        btnStart.setOnClickListener {
            if (isRunning){
                btnStart.text = "START"
                isRunning = false
                mHandler.removeCallbacks(mToastRunnable)
            }else{
                btnStart.text = "STOP"
                isRunning = true
                mToastRunnable.run()
            }
        }
    }

    private val mToastRunnable: Runnable = object : Runnable {
        override fun run() {
            time--
            tvCount.text = time.toString()
            mHandler.postDelayed(this, delay.toLong())
        }
    }
}