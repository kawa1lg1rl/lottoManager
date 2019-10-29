package com.kawa1lg1rl.lotto.network

import retrofit2.Call
import retrofit2.http.*

interface LottoRetrofitService {

    @GET("/gameResult.do?method=byWin&wiselog=H_C_1_1")
    fun getCurrentLottoResult(): Call<String>

    @GET("/gameResult.do?method=byWin")
    fun getLottoResultUsingCount(@Query("drwNo") number:String): Call<String>

    @FormUrlEncoded
    @POST("/gameResult.do?method=statByNumber")
    fun getNumbersStat(@FieldMap params:HashMap<String, String>) : Call<String>

}