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

    var contents:String = ""
    lateinit var splitedContents : String

    companion object {
        var instance = RequestLottoResult()
        lateinit var currentResult: LottoResult
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

    fun getNumbersStat(start : Int = 0, end : Int = 0, bonus: Int = 1) : Array<String> {
        var temp = if(start == 0) 1 else start
        val startCount = if( temp > currentResult.count ) 1 else start
        temp = if(end == 0) currentResult.count else end
        val endCount : Int = if( temp > currentResult.count ) currentResult.count else temp

        val pattern = "drwtNoPop\\[[^\n]+'".toRegex()

        var params = HashMap<String, String>()

        params.put("sortOrder", "DESC")
        params.put("srchType", "list")
        params.put("sltBonus", bonus.toString()) // 0이면 보너스 포함 안함
        params.put("sttDrwNo", startCount.toString())
        params.put("edDrwNo", endCount.toString())

        return object : AsyncTask<Unit, Unit, Array<String>> () {
            override fun doInBackground(vararg p0: Unit?): Array<String> {
                var httpClient:OkHttpClient.Builder = OkHttpClient.Builder()

                var retro: Retrofit = Retrofit.Builder().baseUrl(baseUrl).
                    addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build()).build()

                var service: LottoRetrofitService = retro.create(LottoRetrofitService::class.java)

                contents = service.getNumbersStat(params).execute().body().toString()

                var allStatsSeq = pattern.findAll(contents)

                return allStatsSeq.map {
                    it.value.split("'")[1]
                }.toList().toTypedArray()
            }
        }.execute().get()
    }
}