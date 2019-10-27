package com.kawa1lg1rl.lotto.network

import android.os.AsyncTask
import android.util.Log
import com.kawa1lg1rl.lotto.data.BoughtLottoNumbers
import com.kawa1lg1rl.lotto.data.LottoResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lottonumbers_view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.launch

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class RequestLottoResult {
    var numbers = arrayOfNulls<Int>(7)

    var baseUrl:String = "https://dhlottery.co.kr"
    var defaultUrl:String = "https://dhlottery.co.kr/gameResult.do?method=byWin&wiselog=H_C_1_1"

    var contents:String = ""
    var lotto_debug: Boolean = false
    lateinit var splitedContents : String

    // 싱글톤을 위한
    companion object {
        var instance = RequestLottoResult()
        lateinit var currentResult: LottoResult

    }

    data class Winning(var myWinningNumbers: Array<Int>, var winningNumbers: Array<Int>, var rank: Int)

    fun isWinning(lottoNumbers : BoughtLottoNumbers) : Winning {
        var count = lottoNumbers.count
        lateinit var tempNumbers : LottoResult
        GlobalScope.launch(coroutineContext) {

        }

        var lottoResultAsyncTask = object : AsyncTask<Unit, Unit, Winning>() {
            override fun doInBackground(vararg p0: Unit?): Winning {
                tempNumbers = requestResult(count)

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

            override fun onPostExecute(result: Winning?) {
                super.onPostExecute(result)
            }
        }

        return lottoResultAsyncTask.execute().get()
    }


    fun requestCurrentLottoResult() : LottoResult {
        var httpClient:OkHttpClient.Builder = OkHttpClient.Builder()

        var retro: Retrofit = Retrofit.Builder().baseUrl(baseUrl).
            addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build()).build()

        var service: LottoRetrofitService = retro.create(LottoRetrofitService::class.java)

        contents = service.getCurrentLottoResult().execute().body().toString()
        splitedContents = contents.split("<meta id=\"desc\" name=\"description\" content=\"동행복권 ")[1].split(".\">")[0]


//        lotto_debug = checkLottoDebug()
//        if(lotto_debug) return
        currentResult = LottoResult(parseLottoNumbers(), parseCount(), parseDate(), parseFirstPrize())
        return currentResult
    }


    fun requestResult(count: Int) : LottoResult {
        var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        var retro: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build())
            .build()

        var service: LottoRetrofitService = retro.create(LottoRetrofitService::class.java)

        var body = service.getLottoResultUsingCount(count.toString()).execute().body()
        contents = body.toString()
        splitedContents = contents.split("<meta id=\"desc\" name=\"description\" content=\"동행복권 ")[1].split(".\">")[0]

//        lotto_debug = checkLottoDebug()
//        if(lotto_debug == false) return

        return LottoResult(parseLottoNumbers(), parseCount(), parseDate(), parseFirstPrize())
    }

//    fun checkLottoDebug():Boolean {
//        if(contents.indexOf("win_result") > 0 ) {
//            return false
//        }
//        else {
//            return true
//        }
//    }

    // 100회 당첨번호 1,7,11,23,37,42+6. 1등 총 4명, 1인당 당첨금액 3,315,315,525원
    fun parseLottoNumbers() : Array<Int> {
        return splitedContents.split(".")[0].split("당첨번호 ")[1].
            replace("+", ",").split(",").map {
            it.toInt()
        }.toTypedArray()
    }

    fun parseCount() : Int {
        return splitedContents.split("회 당첨번호")[0].toInt()
    }

    fun parseDate() : String {
        return contents.split("당첨결과</h4>")[1].split("</p>")[0].split("desc\">")[1]
    }

    fun parseFirstPrize() : Long {
        //tbl_data tbl_data_col
        return splitedContents.split("1인당 당첨금액 ")[1].replace("," ,"").replace("원", "").toLong()
    }

    fun testNumbers() {
        numbers.map {
            Log.d("kawa1lg1rl_tag2", "!!!! number test !!!! : " + it)
        }
    }
}