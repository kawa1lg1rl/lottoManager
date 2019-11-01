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
import kotlin.Exception

class RequestLottoResult {
    var numbers = arrayOfNulls<Int>(7)

    var baseUrl:String = "https://dhlottery.co.kr"
    var secondUrl:String = "https://search.naver.com"

    var contents:String = ""
    lateinit var splitedContents : String

    var httpClient:OkHttpClient.Builder = OkHttpClient.Builder()

    var service: LottoRetrofitService = Retrofit.Builder().baseUrl(baseUrl).
        addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build()).build()
        .create(LottoRetrofitService::class.java)

    var naverService = Retrofit.Builder().baseUrl(secondUrl).
        addConverterFactory(ScalarsConverterFactory.create()).client(httpClient.build()).build()
        .create(LottoRetrofitService::class.java)

    companion object {
        var instance = RequestLottoResult()
        lateinit var currentResult: LottoResult
    }

    fun requestCurrentLottoResult() : LottoResult {

        return object : AsyncTask<Unit, Unit, LottoResult>() {
            override fun doInBackground(vararg p0: Unit?): LottoResult {

                contents = service.getCurrentLottoResult().execute().body().toString()
                if(contents.contains("점검")) {
                    contents = naverService.getCurrentLottoResultN().execute().body().toString()

                    var count = contents.split("</em>차 당첨번호")[0].split("<em>").last().replace("회", "").toInt()
                    var date = contents.split("</em>차 당첨번호")[1].split("</span>")[0].split("<span>")[1]
                    var numbers : Array<Int> = arrayOf()

                    contents.split("num_box")[1].split("당첨조회")[0].split("class=\"num").mapIndexed { index, s ->
                        if(index != 0) {
                            numbers += s.split("</span>")[0].split(">")[1].toInt()
                        }
                    }

                    currentResult = LottoResult(numbers, count, date, 0)
                } else {
                    splitedContents = contents.split("<meta id=\"desc\" name=\"description\" content=\"동행복권 ")[1].split(".\">")[0]

                    currentResult = LottoResult(parseLottoNumbers(), parseCount(), parseDate(), parseFirstPrize())
                }

                return currentResult
            }
        }.execute().get()

    }

    fun requestResult(count: Int) : LottoResult {
        return object : AsyncTask<Unit, Unit, LottoResult>() {
            override fun doInBackground(vararg p0: Unit?): LottoResult {
                var body = service.getLottoResultUsingCount(count.toString()).execute().body()

                contents = body.toString()

                var result : LottoResult
                if(contents.contains("점검")) {
                    contents = naverService.getLottoResultUsingCountN(count.toString() + "회로또").execute().body().toString()

                    var count = contents.split("</em>차 당첨번호")[0].split("<em>").last().replace("회", "").toInt()
                    var date = contents.split("</em>차 당첨번호")[1].split("</span>")[0].split("<span>")[1]
                    var numbers : Array<Int> = arrayOf()

                    contents.split("num_box")[1].split("당첨조회")[0].split("class=\"num").mapIndexed { index, s ->
                        if(index != 0) {
                            numbers += s.split("</span>")[0].split(">")[1].toInt()
                        }
                    }

                    result = LottoResult(numbers, count, date, 0)
                } else {

                    splitedContents =
                        contents.split("<meta id=\"desc\" name=\"description\" content=\"동행복권 ")[1].split(
                            ".\">"
                        )[0]

                    //        lotto_debug = checkLottoDebug()
                    //        if(lotto_debug == false) return

                    result = LottoResult(
                        parseLottoNumbers(),
                        parseCount(),
                        parseDate(),
                        parseFirstPrize()
                    )
                }

                return result
            }
        }.execute().get()
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
        var tempNumbers : LottoResult = requestResult(count)

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