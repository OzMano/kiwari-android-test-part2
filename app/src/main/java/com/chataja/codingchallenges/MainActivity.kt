package com.chataja.codingchallenges

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_recyclerview.onClick { startActivity<RecyclerviewActivity>() }
        btn_timer.onClick { startActivity<TimerActivity>() }
        btn_splitmerge.onClick { startActivity<SplitMergeActivity>() }
        btn_bottomsheet.onClick { startActivity<BottomsheetActivity>() }
        btn_tukar.onClick { startActivity<TukarActivity>() }
        btn_assignvariable.onClick { startActivity<AssignVariableActivity>() }
    }
}