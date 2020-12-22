package com.example.memo

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memo.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnDeleteListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db : MemoDatabase

    var memoList: List<MemoEntity> = listOf<MemoEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        db = MemoDatabase.getInstance(this)!!

        button_add.setOnClickListener {
            var memo = MemoEntity(null, edittext_memo.text.toString())
            edittext_memo.setText("")

            if(memo.content == "") {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                insertMemo(memo)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        getAllMemos()
    }

    fun showEmptyView(msg: String? = null) {

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
        if(memoList.isNullOrEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE

            recyclerView.adapter = MyAdapter(this, memoList, this)
        }
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }
}