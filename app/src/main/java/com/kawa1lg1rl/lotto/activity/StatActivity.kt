package com.kawa1lg1rl.lotto.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kawa1lg1rl.lotto.activity.fragment.StatNumberFragment
import com.kawa1lg1rl.lotto.activity.fragment.StatNumberFragment2
import com.kawa1lg1rl.lotto.network.RequestLottoResult
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by kawa1lg1rl on 2019-10-28
 */

class StatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentLayoutId = View.generateViewId()

        verticalLayout {
            weightSum = 10f

            val statNumber = StatNumberFragment()
            val testView = StatNumberFragment2()

            linearLayout {
                lparams(weight = 1f, width = matchParent, height = 0)
                button {
                    text = "번호별 출현 횟수"
                }.lparams(weight = 5f, height = matchParent, width = 0).onClick {
                    // commit할때마다 beginTransaction 만들어줘야함.
                    supportFragmentManager.beginTransaction().replace(fragmentLayoutId, statNumber).commit()
                }


                button {
                    text = "테스트용"
                }.lparams(weight = 5f, height = matchParent, width = 0).onClick {
                    supportFragmentManager.beginTransaction().replace(fragmentLayoutId, testView).commit()
                }
            }

            verticalLayout {
                lparams(weight = 9f, width = matchParent, height = 0)
                id = fragmentLayoutId
                supportFragmentManager.beginTransaction().replace(id, statNumber).commit()
            }
        }
    }

}
