package com.fbexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var todoList: MutableList<Todo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = Firebase.database("https://fbexample-33304-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
        todoList = mutableListOf()
        val adapter = TodoAdapter(todoList)
        rvTodoList.adapter = adapter
        rvTodoList.layoutManager = LinearLayoutManager(this)

        val ref = FirebaseDatabase.getInstance("https://fbexample-33304-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("todos")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (todoSnapshot in snapshot.children) {
                    val todoitem = todoSnapshot.getValue<Todo>()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        btn_save.setOnClickListener(){
            val todoName = et_nama.text.toString()

            val todoId = ref.push().key.toString()
            Log.d("debug", "${todoId} ${todoName}")
            val mhs = Todo(todoName)
            ref.child(todoId).setValue(mhs).addOnCompleteListener{
                Toast.makeText(applicationContext, "Data Berhasil Di Input", Toast.LENGTH_SHORT).show()
            }
        }
    }
}