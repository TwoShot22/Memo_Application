package com.example.memo

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnDeleteListener {

    lateinit var db : MemoDatabase
    var memoList: List<MemoEntity> = listOf<MemoEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = MemoDatabase.getInstance(this)!!

        button_add.setOnClickListener {
            var memo = MemoEntity(null, edittext_memo.text.toString())
            edittext_memo.setText("")
            insertMemo(memo)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        getAllMemos()
    }

    @SuppressLint("StaticFieldLeak")
    fun insertMemo(memo : MemoEntity) {
        val insertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.memoDAO().insert(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }

        }

        insertTask.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun getAllMemos() {
        val getTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                memoList = db.memoDAO().getAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(memoList)
            }

        }

        getTask.execute()

    }

    @SuppressLint("StaticFieldLeak")
    fun deleteMemo(memo: MemoEntity) {
        val deleteTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                db.memoDAO().delete(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }

        }

        deleteTask.execute()
    }

    fun setRecyclerView(memoList : List<MemoEntity>) {
        recyclerView.adapter = MyAdapter(this, memoList, this)
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }
}