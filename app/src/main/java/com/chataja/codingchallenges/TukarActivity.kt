package com.chataja.codingchallenges

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tukar.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk27.coroutines.onClick

class TukarActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tukar)

        btnTukar.onClick {
            if (etA.text.isNotEmpty() && etB.text.isNotEmpty()){
                tuker(etA.text.toString(), etB.text.toString())
            }else{
                layout_tukar.snackbar("Isi dulu variable pertama dan kedua!")
            }
        }
    }

    private fun tuker(varA: String, varB: String){
        var satu = varA.toInt()
        var dua = varB.toInt()

        satu = satu + dua
        dua = satu - dua
        satu = satu - dua

        etA.setText(satu.toString())
        etB.setText(dua.toString())
    }
}