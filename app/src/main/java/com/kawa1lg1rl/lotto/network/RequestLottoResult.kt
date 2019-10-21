package com.kawa1lg1rl.lotto.network

import android.util.Log

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class RequestLottoResult {
    var numbers = arrayOfNulls<Int>(7)
    var count:String? = null
    var date:String? = null
    var firstPrize:String? = ""

    var baseUrl:String = "https://dhlottery.co.kr"
    var defaultUrl:String = "https://dhlottery.co.kr/gameResult.do?method=byWin&wiselog=H_C_1_1"

    var contents:String = ""
    var lotto_debug: Boolean = false


    // 싱글톤을 위한
    companion object {
        var instance = RequestLottoResult()
    }


    fun requestCurrentLottoResult() {
        var httpClient:OkHttpClient.Builder = OkHttpClient.Builder()

        var retro: Retrofit = Retrofit.Builder().baseUrl(baseUrl).
            addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build()).build()

        var service: LottoRetrofitService = retro.create(LottoRetrofitService::class.java)

        contents = service.getCurrentLottoResult().execute().body().toString()

        lotto_debug = checkLottoDebug()
        if(lotto_debug) return

        parseLottoNumbers()
        parseCount()
        parseDate()
        parseFirstPrize()
    }


    fun requestResult(count: Int) {
        var httpClient:OkHttpClient.Builder = OkHttpClient.Builder()

        var retro: Retrofit = Retrofit.Builder().baseUrl(baseUrl).
            addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build()).build()

        var service: LottoRetrofitService = retro.create(LottoRetrofitService::class.java)

        var body = service.getLottoResultUsingCount(count.toString()).execute().body()
        contents = body.toString()

        lotto_debug = checkLottoDebug()
        if(lotto_debug == false) return

        parseLottoNumbers()
        parseCount()
        parseDate()
        parseFirstPrize()

    }

    fun checkLottoDebug():Boolean {
        if(contents.indexOf("win_result") > 0 ) {
            return false
        }
        else {
            return true
        }
    }

    fun parseLottoNumbers() {
        // ball_645 lrg

        var tempArray:List<String>? =  contents.split("ball_645 lrg")
        var count:Int = 0


        tempArray?.map {
            if(count > 0) {
                 numbers[count-1] = it.split("\">")[1].split("</span>")[0].toInt()
            }
            count++
        }
    }

    fun parseCount() {
        count = contents.split("win_result")[1].split("</strong>")[0].split("<strong>")[1]
    }

    fun parseDate() {
        date = contents.split("당첨결과</h4>")[1].split("</p>")[0].split("desc\">")[1]
    }

    fun parseFirstPrize() {
        //tbl_data tbl_data_col
        firstPrize = "1인당 당첨금액 : " + contents.split("<td class=\"tar\">")[2].split("</td>")[0]
    }

    fun testNumbers() {
        numbers.map {
            Log.d("kawa1lg1rl_tag2", "!!!! number test !!!! : " + it)
        }
    }

}