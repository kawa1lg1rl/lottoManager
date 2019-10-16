package com.kawa1lg1rl.lotto.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kawa1lg1rl.lotto.ImageHelper
import com.kawa1lg1rl.lotto.item.LottoNumbersItem
import com.kawa1lg1rl.lotto.R


class GenerateLottoNumbersAdapter(val context: Context, val itemList:List<LottoNumbersItem>) : BaseAdapter() {

    // 확장함수
    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view : View
        var holder : NumbersList


        // itemList에서 item을 가져옴
        var item = itemList[position]
        // 뷰 처음 생성시
        if( convertView == null ) {
            view = LayoutInflater.from(context).inflate(R.layout.lottonumbers_view2, null)
            // 홀더를 사용하면 findViewById를 여러번 하지 않아도 됨.
            holder = NumbersList()

            // ImageView와 Button을 찾아옴
            for( i in 0..5 ) {
                holder.numbers += view.findViewById(context.resources.getIdentifier("lottonumbers2_number_" + (i + 1), "id", context.packageName)) as ImageView
            }
            holder.saveButton = view.findViewById(R.id.lottonumbers_button)

            holder.saveButton!!.text = "저장하기"

            view.tag = holder
        } else {
            holder = convertView.tag as NumbersList
            view = convertView
        }

        // 버튼에 clickListener 설정
        holder.saveButton!!.setOnClickListener {
            Log.d("kawa1lg1rl_tag!!", " = saved data = ")
            item.numbers!!.map {
                Log.d("kawa1lg1rl_tag!!", " - $it")
            }
            item.saveNumbers()
        }



        // 이미지 설정
        var i = 0
        for( number in item.numbers!!) {
            holder.numbers[i].loadImage(context.resources.getIdentifier("number_$number", "drawable", context.packageName))
            i++
        }

        return view
    }


    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    private class NumbersList {
        var numbers: List<ImageView> = listOf()
        var saveButton: Button? = null
    }



}