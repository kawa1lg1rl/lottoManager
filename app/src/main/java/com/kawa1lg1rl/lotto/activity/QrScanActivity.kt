package com.kawa1lg1rl.lotto.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import com.kawa1lg1rl.lotto.R
import android.widget.Toast
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Menu
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import org.jetbrains.anko.*

class QrScanActivity : AppCompatActivity() {
    lateinit var resultUrl:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val qr = IntentIntegrator(this)
        qr.setOrientationLocked(false)
        qr.setPrompt("구매하신 로또의 우측 상단을 화면에 들어오게 찍어주세요")
        qr.initiateScan()
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
        title.text = "QR코드 당첨 확인"
        title.textColor = Color.WHITE


        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                this.finish()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                resultUrl = result.contents

                verticalLayout {
                    lparams(width = matchParent, height = matchParent)

                    webView {
                        webViewClient = WebViewClient()
                    }.apply {
                        settings.javaScriptEnabled = true
                    }.loadUrl(resultUrl)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
