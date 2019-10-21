package com.kawa1lg1rl.lotto.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.adapter.GenerateLottoNumbersAdapter
import com.kawa1lg1rl.lotto.adapter.InputLottoNumbersAdapter
import com.kawa1lg1rl.lotto.data.MySharedPreferences
import kotlinx.android.synthetic.main.lottonumbers_view2.*
import kotlinx.android.synthetic.main.activity_create_numbers.*

class CreateNumbersActivity : AppCompatActivity() {

    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }

    var numbersList: List<Int> = listOf()
    var onClick: (Int) -> (View) -> Unit = {
        number -> {
            view -> inputNumber(number)
        }
    }

    var lottonumbersArray: List<ImageView> = listOf()
    var numbers: List<Int> = listOf()
    var requestManager = App.getGlideObject(App.context())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_numbers)

        var sp = MySharedPreferences(R.string.prefsGeneratedNumbers)

        custom_save_button1.setOnClickListener {
            var time = System.currentTimeMillis()

            if(numbers.size != 6) {
                Toast.makeText(applicationContext, "숫자가 6개 미만입니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "숫자가 정상적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
                sp.addStrings("${time}_0", numbers.toTypedArray())
            }

        }

        lottonumbersArray += lottonumbers2_number_1
        lottonumbersArray += lottonumbers2_number_2
        lottonumbersArray += lottonumbers2_number_3
        lottonumbersArray += lottonumbers2_number_4
        lottonumbersArray += lottonumbers2_number_5
        lottonumbersArray += lottonumbers2_number_6

        lottonumbers_button.text = "초기화"
        lottonumbers_button.setOnClickListener {
            numbers = listOf()
            setNumbersView()
        }

        for(i in 1..45) {
            numbersList += i
        }

        val lottoAdapter = InputLottoNumbersAdapter(this, numbersList, onClick)
        lottonumbers_grid_layout.adapter = lottoAdapter
        lottonumbers_grid_layout.layoutManager = GridLayoutManager(this, 6)
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
        title.text = "번호 직접 생성"

        return super.onCreateOptionsMenu(menu)
    }


    fun inputNumber(number:Int) {
        if(numbers.size < 6) {
            if(number in numbers) {
                //
            } else {
                numbers += number
                numbers = numbers.sorted()
                setNumbersView()
            }
        } else {
            setNumbersView()
        }
    }

    fun setNumbersView() {
        if(numbers.size == 0) {
            for(imageView in lottonumbersArray) {
                imageView.setImageResource(0)
            }
        } else {
            var i = 0
            for(number in numbers){
                requestManager.load(App.context().resources.getIdentifier("number_$number", "drawable" , App.context().packageName)).into(
                    lottonumbersArray[i])
//                    lottonumbersArray[i].loadImage(App.context().resources.getIdentifier("number_$number", "drawable" , App.context().packageName))
                i = i + 1
            }
        }
    }

}
