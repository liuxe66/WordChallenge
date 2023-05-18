package com.liuxe.wordchallenge.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liuxe.wordchallenge.data.bean.BookBean
import com.liuxe.wordchallenge.data.entity.BookEntity
import com.liuxe.wordchallenge.databinding.ItemBookBinding
import com.liuxe.wordchallenge.ext.invisible
import com.liuxe.wordchallenge.ext.visible
import com.liuxe.wordchallenge.utils.LogUtils
import com.liuxe.wordchallenge.widget.FontCustom

/**
 *  author : liuxe
 *  date : 2023/5/16 11:35
 *  description :
 */
class BookAdapter: BaseQuickAdapter<BookEntity, BookAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: ItemBookBinding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: BookEntity?) {
        LogUtils.e("book:"+item?.name)
        holder.binding.apply {
            tvBook.typeface = FontCustom.setFont(context!!)
            tvBook.text = item?.name
            if (item?.isLock == true){
                llLock.visible()
            } else {
                llLock.invisible()
            }
        }
    }


    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

}