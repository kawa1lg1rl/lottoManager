package com.kawa1lg1rl.lotto.utils

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.data.BoughtLottoNumbers
import com.kawa1lg1rl.lotto.data.LottoResult
import com.kawa1lg1rl.lotto.data.MySharedPreferences
import com.kawa1lg1rl.lotto.network.RequestLottoResult
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by kawa1lg1rl on 2019-10-30

 */

class LottoResultUtils {
    val gson = Gson()
    val realmUtils = RealmUtils()

    companion object {
        val instance = LottoResultUtils()

        val spUserInfo = MySharedPreferences(R.string.prefsUserInfo)
        val spBought = MySharedPreferences(R.string.prefsBoughtNumbers)

        fun isFirstLaunch() : Boolean{
            return !spUserInfo.getString("isLaunched")?.toBoolean()
        }
    }

    fun initUserInfo() {
        spUserInfo.addString("isLaunched", "true")
        spUserInfo.addString("currentLottoResult", gson.toJson(RequestLottoResult.instance.requestCurrentLottoResult()))
    }

    fun changeCurrentLottoResult() : Boolean {
        var result = false
        App.isConnected().also {
            if( it == true ) {
                var lottoResultString = spUserInfo.getString("currentLottoResult")

                var lottoResult = gson.fromJson(lottoResultString, LottoResult::class.java)
                var tempResult = RequestLottoResult.instance.requestCurrentLottoResult()

                if( lottoResult.count != tempResult.count ) {
                    spUserInfo.addString("currentLottoResult", gson.toJson(tempResult))
                    result = true
                }
            } else {
                Log.e("kawa1lg1rl_isConn", "changeCurrentLottoResult isConnected : $it")
            }
        }


        Log.d("kawa1lg1rl_", "changeCurrentLottoResult : isChanged $result")

        return result
    }

    fun getCurrentLottoResult() : LottoResult {
        return gson.fromJson(spUserInfo.getString("currentLottoResult"), LottoResult::class.java).also {
            Log.d("kawa1lg1rl_debug", "LottoResultUtils : $it")
        }
    }

    fun saveBoughtNumbers(lottoNumbers : BoughtLottoNumbers) {
        realmUtils.insertBoughtNumbers(lottoNumbers, {key -> Log.d("kawa1lg1rl_realm", "insert $key")})
    }

    fun getBoughtNumbers() : HashMap<Int, BoughtLottoNumbers> {
        val boughtNumbers : HashMap<Int, BoughtLottoNumbers> = HashMap()
        realmUtils.readBoughtNumbers().map {
            boughtNumbers.put(it.key, BoughtLottoNumbers(it.numbers.toTypedArray(), it.count))
        }

        return boughtNumbers
    }

    fun removeBoughtNumbers(key: Int) {
        realmUtils.deleteMyBarcodeRealm(key, {isSuccess -> Log.d("kawa1lg1rl_realm", "deleted : $isSuccess")})
    }

    fun getAllWinning() : Array<Winning> {
        var winnings : Array<Winning> = arrayOf()

        for(entry in getBoughtNumbers().entries) {
            var boughtNumbers = entry.value

            changeCurrentLottoResult()
            var currentResult = getCurrentLottoResult()

            if(boughtNumbers.count == currentResult.count) {
                winnings += isWinning(boughtNumbers)
            }
        }

        return winnings
    }

    data class Winning(var myWinningNumbers: Array<Int>, var winningNumbers: Array<Int>, var rank: Int)

    fun isWinning(lottoNumbers : BoughtLottoNumbers) : Winning {

        var count = lottoNumbers.count
        var tempNumbers : LottoResult = RequestLottoResult.instance.requestResult(count)

        var slicedNumbers = tempNumbers.numbers.sliceArray(0..5)

        var winningNumbers : Array<Int> = arrayOf()

        slicedNumbers.sort()
        lottoNumbers.lottoNumbers.sort()

        lottoNumbers.lottoNumbers.mapIndexed { index, i ->
            if( slicedNumbers.contains(i)) {
                winningNumbers = winningNumbers.plus( slicedNumbers[index] )
            }
        }

        var winning: Int
        winning = when(winningNumbers.size) {
            6 -> 1
            5 -> if( lottoNumbers.lottoNumbers.contains(tempNumbers.numbers[6]) ) 2 else 3
            4 -> 4
            3 -> 5
            else -> 0
        }

        return Winning(winningNumbers, tempNumbers.numbers, winning)
    }

    fun debugLog(json : JSONObject) {
        Log.d("kawa1lg1rl____" , " ==================== ")
        for( key in json.keys() ) {
            Log.d("kawa1lg1rl_parsedNum", (gson.fromJson(json.getString(key) , BoughtLottoNumbers::class.java)).lottoNumbers.joinToString(", ") )
        }
        Log.d("kawa1lg1rl____" , " ==================== ")


    }
}