package com.chataja.codingchallenges

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class BottomsheetActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomsheet)

        val sheetBehavior = from(bottom_sheet)
        sheetBehavior.state = STATE_EXPANDED

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        sheetBehavior.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_HIDDEN -> {
                    }
                    STATE_EXPANDED -> {
                        btn_toggle.text = "Show Keyboard"
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    }
                    STATE_COLLAPSED -> {
                        btn_toggle.text = "Show BottomSheet"
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    }
                    STATE_DRAGGING -> {
                    }
                    STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        btn_toggle.onClick {
            if (sheetBehavior.state == STATE_EXPANDED){
                sheetBehavior.state = STATE_COLLAPSED
            }else{
                sheetBehavior.state = STATE_EXPANDED
            }
        }
    }
}