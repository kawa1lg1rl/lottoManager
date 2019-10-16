package com.kawa1lg1rl.lotto.item

import android.R.id.edit
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.data.MySharedPreferences


class LottoNumbersItem {
    var numbers: Array<Int>? = null
    var name: String? = null

    fun saveNumbers() {
        MySharedPreferences(R.string.prefsGeneratedNumbers).addStrings(name!!, numbers!!)
    }

    fun removeNumbers() {
        MySharedPreferences(R.string.prefsGeneratedNumbers).removeStrings(name!!)
    }


}