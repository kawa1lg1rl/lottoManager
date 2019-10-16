package com.kawa1lg1rl.lotto.adapter

import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.item.LottoNumbersItem
import com.kawa1lg1rl.lotto.R


class SavedLottoNumbersAdapter(val context: Context, var itemList:List<LottoNumbersItem>) : RecyclerView.Adapter<SavedLottoNumbersAdapter.NumbersHolder>() {

    // 확장함수
    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumbersHolder {
        return NumbersHolder(LayoutInflater.from(context).inflate(R.layout.lottonumbers_view2, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: NumbersHolder, position: Int) {
        var item = itemList[position]

        // 이미지 설정
        var i = 0
        for( number in item.numbers!!) {
            holder.numbers[i].loadImage(context.resources.getIdentifier("number_$number", "drawable", context.packageName))
            i++
        }

        holder.removeButton.text = "지우기"
        holder.removeButton!!.setOnClickListener {
            item.removeNumbers()
            itemList = itemList.minus(item)
            this.notifyDataSetChanged()
        }
    }



    class NumbersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var temp: Array<Int> = arrayOf(0,1,2,3,4,5)
        var context = App.context()
        var numbers: List<ImageView> = temp.map {
            itemView.findViewById(context.resources.getIdentifier("lottonumbers2_number_" + (it + 1), "id", context.packageName)) as ImageView
        }
        var removeButton: Button = itemView.findViewById(R.id.lottonumbers_button)
    }

}