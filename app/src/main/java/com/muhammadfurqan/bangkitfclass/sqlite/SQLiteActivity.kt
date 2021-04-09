package com.muhammadfurqan.bangkitfclass.sqlite

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammadfurqan.bangkitfclass.R
import com.muhammadfurqan.bangkitfclass.databinding.ActivitySqliteBinding
import com.muhammadfurqan.bangkitfclass.sqlite.adapter.BookAdapter
import com.muhammadfurqan.bangkitfclass.sqlite.db.BookDatabaseManager
import com.muhammadfurqan.bangkitfclass.sqlite.db.BookDatabaseOpenHelper
import kotlinx.coroutines.launch

/**
 *
 * Contact : 081375496583
 *
 * Step :
 * 1. Fork our Repository (https://github.com/fueerqan/Bangkit-F-Class)
 *
 * CHALLENGE :
 * 1. Recycler View to show all of the data, previously we only show them in toast
 * 2. Add Function to edit the books data for each item in your Recycler View Items
 * 3. Add Function to delete the books data for each item in your Recycler View Items
 * 4. Notify Data Changes for you Recycler View
 *
 * Reward : Rp20.000 Go-Pay / OVO;w"
 * :S
 * Limit : No Limit Person
 * Dateline : 23.00
 *
 * Submit to https://forms.gle/CytSQSyQDJeivpkd7
 *
 */

class SQLiteActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySqliteBinding
    private lateinit var etBookName: AppCompatEditText
    private lateinit var btnAdd: AppCompatButton
    private lateinit var btnRead: AppCompatButton
    private lateinit var bookAdapter: BookAdapter

    private val bookDb: BookDatabaseManager by lazy {
        BookDatabaseManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySqliteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etBookName = findViewById(R.id.et_book_name)
        btnAdd = findViewById(R.id.btn_add)
        btnRead = findViewById(R.id.btn_read)

        binding.rvBookList.setHasFixedSize(true)

        onRead()

        btnAdd.setOnClickListener {
            onAdd()
        }
    }

    private fun onAdd() {
        val bookName = etBookName.text.toString()
        if (bookName.isNotEmpty()) {
            lifecycleScope.launch {
                bookDb.saveData(bookName)
            }
            onRead()
            etBookName.setText("")
        } else {
            Toast.makeText(this, "Please fill in the book name", Toast.LENGTH_SHORT).show()
        }
    }

    fun onRead() {
        val bookList = bookDb.getData()
        Log.d("Book", "$bookList")
        showRecyclerView(bookList)
    }

    private fun showRecyclerView(bookList: List<BookModel>) {
        binding.rvBookList.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(bookList)
        binding.rvBookList.adapter = bookAdapter
    }

    fun updateDialog(books: BookModel) {
        val updateDialog = Dialog(this, R.style.Theme_AppCompat_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.edit_dialog)

        val dialogId = updateDialog.findViewById(R.id.tv_id) as TextView
        val dialogTitle = updateDialog.findViewById(R.id.tv_book_title) as EditText
        val tvUpdate = updateDialog.findViewById(R.id.tvUpdate) as TextView
        val tvCancel = updateDialog.findViewById(R.id.tvCancel) as TextView

        dialogId.text = books.id.toString()
        dialogTitle.setText(books.name)

        tvUpdate.setOnClickListener {

            val title = dialogTitle.text.toString()

            val databaseManager = BookDatabaseManager(this)

            if(!title.isEmpty()) {
                val status = databaseManager.updateData(BookModel(books.id, title))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Record Updated.", Toast.LENGTH_LONG).show()
                    onRead()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or Email cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

}