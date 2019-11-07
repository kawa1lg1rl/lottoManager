package com.kawa1lg1rl.lotto.activity

import android.app.NotificationChannel
import android.app.NotificationManager
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
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.data.LottoResult
import com.kawa1lg1rl.lotto.utils.LottoResultUtils
import kotlinx.android.synthetic.main.custom_title_bar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import kotlin.coroutines.coroutineContext


class MainActivity : AppCompatActivity() {
    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }

    var lottonumbersArray: List<ImageView> = listOf()
    var requestLottoResult:RequestLottoResult? = null


    companion object {
        lateinit var ad : InterstitialAd
        var isShowed = false
    }

    fun viewAds() {

        var adRequest = AdRequest.Builder()
//            .addTestDevice("B722EDC53A549C919F47DE9600254A28")
            .build()
        MobileAds.initialize(this, getString(R.string.app_id))

        ad = InterstitialAd(this)
        ad.adUnitId = getString(R.string.ad_id)

        ad.loadAd(adRequest)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(isShowed == false) {
            viewAds()
            CoroutineScope(Dispatchers.Main).async {
                while(true) {
                    Log.d("kawa1lg1rl_debug", "ad async")

                    delay(500)
                    if(isShowed == true) {
                        break
                    } else {
                        if(ad.isLoaded) {
                            ad.show()
                            isShowed = true
                        } else {
                            Log.d("kawa1lg1rl_debug", "isLoaded ${ad.isLoaded}")
                        }
                    }
                }
            }
        }

        setAllIntent()

        updateButton.setOnClickListener {
            LottoResultUtils.instance.changeCurrentLottoResult()
            setCurrentLottoView()
        }

        lottonumbersArray += lottonumbers_number_1
        lottonumbersArray += lottonumbers_number_2
        lottonumbersArray += lottonumbers_number_3
        lottonumbersArray += lottonumbers_number_4
        lottonumbersArray += lottonumbers_number_5
        lottonumbersArray += lottonumbers_number_6
        lottonumbersArray += lottonumbers_number_7

        setCurrentLottoView(LottoResultUtils.instance.getCurrentLottoResult())
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

        button_talk.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/sHMeLREb")))
        }

        var title = findViewById<TextView>(R.id.title)
        title.text = "로또 매니저"
        title.textColor = Color.WHITE


        return super.onCreateOptionsMenu(menu)
    }

    fun setAllIntent() {

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

        button_stat_in_menu.setOnClickListener {
            startActivity<StatActivity>()
        }
        button_create_numbers_view.setOnClickListener {
            startActivity<CreateNumbersActivity>()
        }

        button_intro.setOnClickListener {
            startActivity<IntroActivity>()
        }


    }

    fun setCurrentLottoView(lottoResult : LottoResult? = null) {
        var _lottoResult : LottoResult

        if(lottoResult != null) {
            _lottoResult = lottoResult
        } else {
            _lottoResult = LottoResultUtils.instance.getCurrentLottoResult()
        }

        current_lotto_count.setText( _lottoResult.count.toString() + "회")
        current_lotto_date.setText( _lottoResult.date )
        current_lotto_prize.setText( String.format("%,d원", _lottoResult.firstPrize ))

        var count = 0
        _lottoResult.numbers.map {
            lottonumbersArray[count].loadImage(resources.getIdentifier("number_$it", "drawable", packageName))
            count ++
        }



        lottonumbers_number_plus.loadImage(resources.getIdentifier("plus", "drawable", packageName))
    }

}
