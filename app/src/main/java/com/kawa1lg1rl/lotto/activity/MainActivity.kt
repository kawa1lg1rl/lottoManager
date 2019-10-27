package com.kawa1lg1rl.lotto.activity

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lottonumbers_view.*
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.network.RequestLottoResult
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {
    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }

    var lottonumbersArray: List<ImageView> = listOf()
    var requestLottoResult:RequestLottoResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAllIntent()

        updateButton.setOnClickListener {
            setCurrentLottoView()
        }

        lottonumbersArray += lottonumbers_number_1
        lottonumbersArray += lottonumbers_number_2
        lottonumbersArray += lottonumbers_number_3
        lottonumbersArray += lottonumbers_number_4
        lottonumbersArray += lottonumbers_number_5
        lottonumbersArray += lottonumbers_number_6
        lottonumbersArray += lottonumbers_number_7


        setCurrentLottoView()
    }


    var isOpenMenuBar: Boolean = false
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var actionBar : ActionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(false)  //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false) //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false)  //홈 아이콘을 숨김처리합니다.

        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var actionBarView = inflater.inflate(R.layout.custom_title_bar, null)

        actionBar.setCustomView(actionBarView)


        var btnMenu = findViewById<ImageButton>(R.id.btnMenu)

        btnMenu.setOnClickListener {
            if(isOpenMenuBar == false) {
                main_drawer_layout.openDrawer(left_menu_bar)
                isOpenMenuBar = !isOpenMenuBar
            } else {
                main_drawer_layout.closeDrawer(left_menu_bar)
                isOpenMenuBar = !isOpenMenuBar
            }
        }

        var title = findViewById<TextView>(R.id.title)
        title.text = "로또 매니저"

        return super.onCreateOptionsMenu(menu)
    }

    fun setAllIntent() {
        button_create_numbers_view_in_menu.setOnClickListener{
            startActivity<CreateNumbersActivity>()
        }

        button_generated_random_numbers_in_menu.setOnClickListener{
            startActivity<GenerateNumbersActivity>()
        }

        button_saved_numbers_in_menu.setOnClickListener {
            startActivity<ViewLottonumbersActivity>()
        }

        button_tax_in_menu.setOnClickListener {
            startActivity<TaxActivity>("firstPrize" to RequestLottoResult.currentResult.firstPrize)
        }

        button_bought_number_in_menu.setOnClickListener {
            startActivity<BoughtNumbersActivity>()
        }

        button_qrcode_scan_in_menu.setOnClickListener {
            startActivity<QrScanActivity>()
        }

        button_view_bought_number_in_menu.setOnClickListener{
            startActivity<ViewBoughtNumbersActivity>()
        }

        button_generated_random_numbers.setOnClickListener {
            startActivity<GenerateNumbersActivity>()
        }

        button_saved_numbers.setOnClickListener {
            startActivity<ViewLottonumbersActivity>()
        }
    }

    fun setCurrentLottoView() {

        requestLottoResult = RequestLottoResult.instance
        var lottoResultAsyncTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                requestLottoResult!!.requestCurrentLottoResult()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)

                current_lotto_count.setText( RequestLottoResult.currentResult.count.toString() + "회")
                current_lotto_date.setText( RequestLottoResult.currentResult.date )
                current_lotto_prize.setText( String.format("%,d원", RequestLottoResult.currentResult.firstPrize ))

                var count = 0
                RequestLottoResult.currentResult.numbers.map {
                    lottonumbersArray[count].loadImage(resources.getIdentifier("number_$it", "drawable", packageName))
                    count ++
                }

                lottonumbers_number_plus.loadImage(resources.getIdentifier("plus", "drawable", packageName))
            }
        }

        lottoResultAsyncTask.execute()
    }

}
