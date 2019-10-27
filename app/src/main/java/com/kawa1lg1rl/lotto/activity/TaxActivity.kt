package com.kawa1lg1rl.lotto.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.kawa1lg1rl.lotto.R
import kotlinx.android.synthetic.main.activity_tax.*

class TaxActivity : AppCompatActivity() {

    fun calculateTax(prize: Long) : Long{
        var calculatedTax:Long
        if(prize> 300000000) {
            calculatedTax = ((299999999 - 50000) * 0.22 + (prize- 299999999) * 0.33).toLong()
        } else if(prize > 50000) {
            calculatedTax = ((prize - 50000)* 0.22).toLong()
        } else {
            calculatedTax = 0
        }

        return calculatedTax
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tax)

        inputed_prize.text = "0원"
        calculated_prize.text = "당첨금 : 0원"
        calculated_tax.text = "세금 : 0원"

        tax_explanation_button.setOnClickListener {
            tax_explanation_button.visibility = View.GONE
            tax_calculator.visibility = View.VISIBLE
        }

        input_prize.addTextChangedListener( object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if(text.toString().length > 0) {
                    inputed_prize.text = String.format("%,d", text.toString().toLong()) + "원"
                    calculated_tax.text = "세금 : " + String.format("%,d", calculateTax(text.toString().toLong())) + "원"
                    calculated_prize.text = "당첨금 : " + String.format("%,d", text.toString().toLong() - calculateTax(text.toString().toLong())) + "원"

                } else {
                    inputed_prize.text = "0원"
                    calculated_tax.text = "세금 : 0원"
                    calculated_prize.text = "당첨금 : 0원"
                }
            }

            override fun beforeTextChanged(chr: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        var firstPrize = intent.getLongExtra("firstPrize", 0)

        calculated_first_prize.text = "이번 회차 1등 당첨금 : ${String.format("%,d", firstPrize)}\n" + "1등 당첨금 세금계산 : ${String.format("%,d", calculateTax(firstPrize))}"
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
        title.text = "세금 계산기"

        return super.onCreateOptionsMenu(menu)
    }
}
