package com.kawa1lg1rl.lotto.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 * Created by kawa1lg1rl on 2019-10-29
 */

class StatNumberFragment2 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = UI {
        verticalLayout {
            textView {
                text = "fragment test 22222"
            }
        }
    }.view
}