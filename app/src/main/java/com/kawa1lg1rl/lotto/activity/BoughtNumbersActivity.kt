package com.kawa1lg1rl.lotto.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.kawa1lg1rl.lotto.adapter.InputLottoNumbersAdapter
import com.kawa1lg1rl.lotto.data.MySharedPreferences
import com.kawa1lg1rl.lotto.network.RequestLottoResult
import kotlinx.android.synthetic.main.activity_bought_numbers.*
import kotlinx.android.synthetic.main.lottonumbers_view2.*
import java.text.SimpleDateFormat
import java.util.*

class BoughtNumbersActivity : AppCompatActivity() {

    var numbersList: List<Int> = listOf()
    var onClick: (Int) -> (View) -> Unit = {
        number -> {
            view ->
                inputNumber(number)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bought_numbers)
        init()

        var sp = MySharedPreferences(R.string.prefsBoughtNumbers)
        var spCount = MySharedPreferences(R.string.prefsBoughtNumbersCount)

        custom_save_button2.setOnClickListener {
            var time = System.currentTimeMillis()

            if ( numbers.size != 6) {
                Toast.makeText(applicationContext, "숫자가 6개 미만입니다.", Toast.LENGTH_SHORT).show()
            } else if ( input_bought_count.text.toString().length <= 0 ) {
                Toast.makeText(applicationContext, "회차를 입력하지 않아 현재 회차로 저장됩니다.", Toast.LENGTH_SHORT).show()

                sp.addStrings("${time}", numbers.toTypedArray())
                spCount.addString("${time}", RequestLottoResult.currentResult.count.toString() + "회")
            } else {
                Toast.makeText(applicationContext, "숫자가 정상적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()

                sp.addStrings("${time}", numbers.toTypedArray())
                spCount.addString("${time}", input_bought_count.text.toString() + "회")
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
            input_bought_count.setText("0")
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
        title.text = "구입 번호 입력"

        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        fun ImageView.loadImage(resourceId: Int) {
            Glide.with(this).load(resourceId).into(this)
        }

        var lottonumbersArray: List<ImageView> = listOf()
        var numbers: List<Int> = listOf()
        var requestManager = App.getGlideObject(App.context())

        fun init() {
            lottonumbersArray = listOf()
            numbers = listOf()
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

}
