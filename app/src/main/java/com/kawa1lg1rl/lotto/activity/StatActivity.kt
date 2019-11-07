package com.kawa1lg1rl.lotto.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.activity.fragment.StatNumberFragment
import com.kawa1lg1rl.lotto.activity.fragment.StatNumberFragment2
import com.kawa1lg1rl.lotto.network.RequestLottoResult
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by kawa1lg1rl on 2019-10-28
 */

class StatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentLayoutId = View.generateViewId()

        verticalLayout {
            weightSum = 10f

            val statNumber = StatNumberFragment()
            val testView = StatNumberFragment2()

            linearLayout {
                lparams(weight = 1f, width = matchParent, height = 0)
                button {
                    text = "번호별 출현 횟수"
                }.lparams(weight = 5f, height = matchParent, width = 0).onClick {
                    // commit할때마다 beginTransaction 만들어줘야함.
                    supportFragmentManager.beginTransaction().replace(fragmentLayoutId, statNumber).commit()
                }


                button {
                    text = "통계 추가 예정"
                }.lparams(weight = 5f, height = matchParent, width = 0).onClick {

                }
            }

            verticalLayout {
                lparams(weight = 9f, width = matchParent, height = 0)
                id = fragmentLayoutId
                supportFragmentManager.beginTransaction().replace(id, statNumber).commit()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var actionBar : ActionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(false)  //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false)  //홈 아이콘을 숨김처리합니다.

        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var actionBarView = inflater.inflate(R.layout.custom_title_bar_inmenu, null)

        actionBar.setCustomView(actionBarView)

        var backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        var title = findViewById<TextView>(R.id.title)
        title.text = "통계"
        title.textColor = Color.WHITE

        return super.onCreateOptionsMenu(menu)
    }

}
