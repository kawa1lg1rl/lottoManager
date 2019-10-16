package com.kawa1lg1rl.lotto.adapter

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.activity.CreateNumbersActivity

class InputLottoNumbersAdapter(val context: Context, var itemList:List<Int>, var onClickListener: Function<Unit>) :
    RecyclerView.Adapter<com.kawa1lg1rl.lotto.adapter.InputLottoNumbersAdapter.ImageHolder>() {

    // 확장함수
    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        var item = itemList[position]


        // 이미지 설정
        holder.imageView.loadImage(context.resources.getIdentifier("number_$item", "drawable", context.packageName))
        holder.imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE

        holder.imageView.setOnClickListener(View.OnClickListener {
            onClickListener.
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {c
        return ImageHolder(LayoutInflater.from(context).inflate(R.layout.one_number_item, parent, false))
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.one_number_image) as ImageView

    }

}