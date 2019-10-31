package com.kawa1lg1rl.lotto.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    companion object {
        val instance = LottoResultUtils()

        val spUserInfo = MySharedPreferences(R.string.prefsUserInfo)
        val spBought = MySharedPreferences(R.string.prefsBoughtNumbers)

        fun isFirstLaunch() : Boolean{
            return !spUserInfo.getAllKey().contains("isLaunched")
        }
    }

    fun initUserInfo() {
        spUserInfo.addString("isLaunched", "true")
        spUserInfo.addString("currentLottoResult", gson.toJson(RequestLottoResult.instance.requestCurrentLottoResult()))
    }

    fun changeCurrentLottoResult() {
        var lottoResultString = spUserInfo.getString("currentLottoResult")

        var lottoResult = gson.fromJson(lottoResultString, LottoResult::class.java)
        var tempResult = RequestLottoResult.instance.requestCurrentLottoResult()

        if( lottoResult.count != tempResult.count ) {
            spUserInfo.addString("currentLottoResult", gson.toJson(tempResult))
        }
    }

    fun getCurrentLottoResult() : LottoResult {
        return gson.fromJson(spUserInfo.getString("currentLottoResult"), LottoResult::class.java).also {
            Log.d("kawa1lg1rl_debug", "LottoResultUtils : $it")
        }
    }

    fun saveBoughtNumbers(lottoNumbers : BoughtLottoNumbers) {

        var savedNumbers = spBought.getString("numbers")

        if(savedNumbers == "") {
            spBought.addString("numbers", gson.toJson(JSONObject()))
            savedNumbers = spBought.getString("numbers")
        }

//        var tempType = object : TypeToken<Array<HashMap<String, BoughtLottoNumbers>>>() {}.type
        var parsedNumbers = gson.fromJson(savedNumbers, JSONObject::class.java)

        val random = Random()
        val key = Date().time.toString() + "_" + random.nextInt().toString()

        parsedNumbers.put(key, gson.toJson(lottoNumbers))
        spBought.addString("numbers", gson.toJson(parsedNumbers))
    }

    fun getBoughtNumbers() : HashMap<String, BoughtLottoNumbers> {
        var boughtNumbers : HashMap<String, BoughtLottoNumbers> = HashMap()
        val savedNumbers = gson.fromJson(spBought.getString("numbers"), JSONObject::class.java)

        debugLog(savedNumbers)

        for(i in savedNumbers.keys()) {
            var tempBoughtNumbers = gson.fromJson(savedNumbers.getString(i), BoughtLottoNumbers::class.java)
            boughtNumbers.put(i, tempBoughtNumbers)
        }

        return boughtNumbers
    }

    fun removeBoughtNumbers(key: String) {
        var all = spBought.getString("numbers")

        var jsonObject = gson.fromJson(all, JSONObject::class.java)
        jsonObject.remove(key)

        spBought.addString("numbers", gson.toJson(jsonObject))
    }

    fun debugLog(json : JSONObject) {
        Log.d("kawa1lg1rl____" , " ==================== ")
        for( key in json.keys() ) {
            Log.d("kawa1lg1rl_parsedNum", (gson.fromJson(json.getString(key) , BoughtLottoNumbers::class.java)).lottoNumbers.joinToString(", ") )
        }
        Log.d("kawa1lg1rl____" , " ==================== ")

    }
}