package com.kawa1lg1rl.lotto.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.kawa1lg1rl.lotto.R
import org.jetbrains.anko.*

/**
 * Created by kawa1lg1rl on 2019-11-07
 */


class IntroActivity : AppCompatActivity() {
    lateinit var introText : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            gravity = Gravity.CENTER
            lparams(width = matchParent, height = matchParent)
            space {

            }.lparams(weight = 0.1f, height = 0, width = matchParent)

            linearLayout {
                lparams(weight = 1f, height = 0, width = matchParent)

                space {

                }.lparams(weight = 0.1f, height = matchParent, width = 0)

                introText = textView {


                }.lparams(weight = 1f, height = matchParent, width = 0)

                space {

                }.lparams(weight = 0.1f, height = matchParent, width = 0)
            }

            space {

            }.lparams(weight = 0.1f, height = 0, width = matchParent)

        }

        introText.text = """ 이 앱은 로또 구매 및 당첨확인 관련하여 편의성을 위해 제작된 앱입니다.
 
 기능들은 추가할 예정이며, 시작화면의 우측상단 카카오톡 모양을 눌러 연락주시면 원하시는 기능을 만들어 드리겠습니다.
 
 에러가 발생하는 경우, 기기 정보 및 자세한 상황을 카카오톡으로 말씀해주시면 빠른 시일 내에 고치겠습니다.
 
 혼자 만들다 보니 부족한 점이 많습니다. 디자인의 경우에도 제안해주시면 참고하겠습니다.
        """
        introText.textColor = Color.BLACK
        introText.textSize = 20f

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
        title.text = "안내"
        title.textColor = Color.WHITE

        return super.onCreateOptionsMenu(menu)
    }
}