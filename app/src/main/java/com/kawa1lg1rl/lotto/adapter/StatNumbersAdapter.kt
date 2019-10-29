package com.kawa1lg1rl.lotto.adapter

import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.view.Gravity
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.jetbrains.anko.*

class StatNumbersAdapter(val context: Context, var itemList:Array<String>) :
    RecyclerView.Adapter<StatNumbersAdapter.StatNumbersHolder>() {

    // 확장함수
    fun ImageView.loadImage(resourceId: Int) {
        Glide.with(this).load(resourceId).into(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatNumbersHolder {
        return StatNumbersHolder(
            StatNumbersView().createView(AnkoContext.create(parent.context, parent))
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: StatNumbersHolder, position: Int) {
        var item = itemList[position]

        holder.count.setText((position + 1).toString())
        holder.stat.setText(item + "회")

    }

    inner class StatNumbersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val count : TextView = itemView.findViewById(countView)
        val stat : TextView = itemView.findViewById(statView)
    }

    val countView = View.generateViewId()
    val statView = View.generateViewId()

    // Anko를 이용한 Recycler View
    inner class StatNumbersView : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            linearLayout {
                lparams(height = dip(60), width = matchParent)

                textView {
                    id = countView
                    gravity = Gravity.CENTER
                }.lparams(weight = 1f, height = matchParent, width = 0)

                textView {
                    id = statView
                    gravity = Gravity.CENTER
                }.lparams(weight = 1f, height = matchParent, width = 0)
            }
        }
    }

}