package com.kawa1lg1rl.lotto.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.service.NotifyService
import com.kawa1lg1rl.lotto.utils.LottoResultUtils
import kotlinx.android.synthetic.main.activity_start_ad.*
import org.jetbrains.anko.*

class StartAdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_ad)

        if(LottoResultUtils.isFirstLaunch()) {
            if(App.isConnected()) {
                ad_screen.setOnClickListener {
                    inflateMainActivity()
                }

                if(LottoResultUtils.isFirstLaunch()) {
                    LottoResultUtils.instance.initUserInfo()
                    LottoResultUtils.instance.changeCurrentLottoResult()
                } else {
                    LottoResultUtils.instance.changeCurrentLottoResult()
                }

                startService(Intent(this, NotifyService::class.java))
            } else {
                longToast("인터넷 연결을 확인해주세요.")

                plain_text.setText("인터넷 연결을 확인해주세요. \n터치하시면 앱이 종료됩니다.")
                ad_screen.setOnClickListener {
                    finish()
                }
            }
        } else {

            ad_screen.setOnClickListener {
                inflateMainActivity()
            }

            if(LottoResultUtils.isFirstLaunch()) {
                LottoResultUtils.instance.initUserInfo()
            } else {
                LottoResultUtils.instance.changeCurrentLottoResult()
            }

            startService(Intent(this, NotifyService::class.java))
        }
    }

    fun inflateMainActivity() {
        var nextIntent : Intent = Intent(this, MainActivity::class.java)
        nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        startActivity(nextIntent)
    }
}
