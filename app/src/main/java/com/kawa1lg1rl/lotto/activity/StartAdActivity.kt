package com.kawa1lg1rl.lotto.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kawa1lg1rl.lotto.R
import kotlinx.android.synthetic.main.activity_start_ad.*

class StartAdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_ad)

        ad_screen.setOnClickListener {
            inflateMainActivity()
        }
    }

        fun inflateMainActivity() {
            var nextIntent : Intent = Intent(this, MainActivity::class.java)
            nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(nextIntent)
    }
}
