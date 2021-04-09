package com.muhammadfurqan.bangkitfclass.sqlite.adapter

import android.content.Context
import android.media.browse.MediaBrowser
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.muhammadfurqan.bangkitfclass.MainActivity
import com.muhammadfurqan.bangkitfclass.databinding.ItemBookBinding
import com.muhammadfurqan.bangkitfclass.sqlite.BookModel
import com.muhammadfurqan.bangkitfclass.sqlite.SQLiteActivity
import com.muhammadfurqan.bangkitfclass.sqlite.db.BookDatabaseManager

class BookAdapter(private val bookList: List<BookModel>): RecyclerView.Adapter<BookAdapter.ListViewHolder>() {
    private var onItemClickCallBack: OnItemClickCallBack? = null
    private lateinit var mContext: Context

    private val bookDb: BookDatabaseManager by lazy {
        BookDatabaseManager(mContext)
    }

    inner class ListViewHolder(private val itemBinding: ItemBookBinding): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(books: BookModel) {
            mContext = itemView.context
            itemBinding.number.text = books.id.toString()
            itemBinding.bookTitle.text = books.name
            itemBinding.delete.setOnClickListener {
                bookDb.deleteData(itemBinding.number.text.toString())
                (mContext as SQLiteActivity).onRead()
                Toast.makeText(mContext, "${itemBinding.bookTitle.text} has been deleted", Toast.LENGTH_SHORT).show()
            }
            itemBinding.edit.setOnClickListener {
                (mContext as SQLiteActivity).updateDialog(books)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.ListViewHolder {
        val itemBinding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BookAdapter.ListViewHolder, position: Int) {
        val books = bookList[position]
        holder.bind(books)
        holder.itemView.setOnClickListener {
            onItemClickCallBack?.onItemClicked(books)
        }

    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    interface OnItemClickCallBack{
        fun onItemClicked(book: BookModel)
    }
}