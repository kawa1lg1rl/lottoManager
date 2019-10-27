package com.kawa1lg1rl.lotto.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.adapter.SavedLottoNumbersAdapter
import com.kawa1lg1rl.lotto.adapter.SavedLottoNumbersAdapterAnko
import com.kawa1lg1rl.lotto.data.MySharedPreferences
import com.kawa1lg1rl.lotto.item.LottoNumbersItem

class ViewBoughtNumbersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_lottonumbers)

        var items : List<LottoNumbersItem> = listOf()
        var countItems : List<String> = listOf()

        var sp = MySharedPreferences(R.string.prefsBoughtNumbers)
        var spCount = MySharedPreferences(R.string.prefsBoughtNumbersCount)
        var totalCount = spCount.getAllSet()

        for (entry in sp.getAllSet().entries) {
            var currentCouont = totalCount.get(entry.key)

            var item = LottoNumbersItem()
            item.numbers = (entry.value as MutableSet<String>).map { it.toInt() }.toTypedArray()
            item.numbers!!.sort()
            item.name =  entry.key

            countItems += currentCouont.toString()
            items += item
        }

        val lottoAdapter = SavedLottoNumbersAdapterAnko(this, items, countItems)
        findViewById<RecyclerView>(R.id.saved_numbers_list).adapter = lottoAdapter
        findViewById<RecyclerView>(R.id.saved_numbers_list).layoutManager = LinearLayoutManager(this)
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
        title.text = "구입한 번호 보기"

        return super.onCreateOptionsMenu(menu)
    }
}
