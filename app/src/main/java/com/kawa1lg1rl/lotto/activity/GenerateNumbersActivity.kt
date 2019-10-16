package com.kawa1lg1rl.lotto.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.kawa1lg1rl.lotto.adapter.GenerateLottoNumbersAdapter
import com.kawa1lg1rl.lotto.item.LottoNumbersItem
import com.kawa1lg1rl.lotto.R
import kotlinx.android.synthetic.main.activity_generate_numbers.*
import kotlinx.android.synthetic.main.activity_main.*

class GenerateNumbersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_numbers)

        generateRandomNumbersButton.setOnClickListener {
            var countString = countForGenerate.text.toString()
            var count = if(countString == "") 0 else countString.toInt() - 1

            if(count > 10 - 1) {
                Toast.makeText(applicationContext, "숫자를 10이하로 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var items : List<LottoNumbersItem> = listOf()

            var time = System.currentTimeMillis()

            for(i in 0..count) {
                var item = LottoNumbersItem()
                item.numbers = getRandomNumbers()
                item.name = time.toString() + "_" + i.toString()

                items += item
            }

            val lottoAdapter = GenerateLottoNumbersAdapter(this, items)
            findViewById<ListView>(R.id.generate_numbers_list).adapter = lottoAdapter
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
        title.text = "번호 랜덤 생성"

        return super.onCreateOptionsMenu(menu)
    }


    fun getRandomNumbers() : Array<Int> {
        var randomNumbers: Array<Int> = arrayOf()

        var random = java.util.Random()

        var i = 0
        while (i < 6) {
            var randomNumber = random.nextInt(45) + 1
            var check = false

            for (j in randomNumbers) {
                if (j == randomNumber) {
                    check = true
                    break
                }
            }
            if (check == true) {
                continue
            }
            randomNumbers = randomNumbers.plus(randomNumber)
            i++
        }


        randomNumbers.sort()

        return randomNumbers
    }
}

