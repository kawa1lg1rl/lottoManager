package com.kawa1lg1rl.lotto.utils

import android.util.Log
import com.google.gson.Gson
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.data.LottoResult
import com.kawa1lg1rl.lotto.data.MySharedPreferences
import com.kawa1lg1rl.lotto.network.RequestLottoResult

/**
 * Created by kawa1lg1rl on 2019-10-30

 */

class LottoResultUtils {

    companion object {
        val instance = LottoResultUtils()

        val sp = MySharedPreferences(R.string.prefsUserInfo)

        fun isFirstLaunch() : Boolean{
            return !sp.getAllKey().contains("isLaunched")
        }
    }

    fun initUserInfo() {
        var gson : Gson = Gson()

        sp.addString("isLaunched", "true")
        sp.addString("currentLottoResult", gson.toJson(RequestLottoResult.instance.requestCurrentLottoResult()))
    }

    fun changeCurrentLottoResult() {
        var lottoResultString = sp.getString("currentLottoResult")
        var gson = Gson()

        var lottoResult = gson.fromJson(lottoResultString, LottoResult::class.java)
        var tempResult = RequestLottoResult.instance.requestCurrentLottoResult()

        if( lottoResult.count != tempResult.count ) {
            sp.addString("currentLottoResult", gson.toJson(tempResult))
        }
    }

    fun getCurrentLottoResult() : LottoResult {
        val gson = Gson()
        return gson.fromJson(sp.getString("currentLottoResult"), LottoResult::class.java).also {
            Log.d("kawa1lg1rl_debug", "LottoResultUtils : $it")
        }
    }
}