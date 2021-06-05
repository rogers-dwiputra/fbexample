package com.fbexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        btn_register.setOnClickListener(){
            this.testCreateUser()
        }

        btn_signin.setOnClickListener(){
            this.testSignIn()
        }

        btn_logout.setOnClickListener(){
            Firebase.auth.signOut()
            Log.d("debug", "Logout");
        }

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
                todoList.clear()
                for (todoSnapshot in snapshot.children) {
                    val todoitem = todoSnapshot.getValue<Todo>()
                    if (todoitem != null) {
                        Log.d("debug", todoitem.todoName)
                        todoList.add(todoitem)
                        adapter.notifyItemInserted(todoList.size -1)
                    }
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

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Log.d("debug", currentUser.toString())
            Log.d("debug", "User Sudah Login, arahkan ke halaman utama")
        }
        else {
            Log.d("debug", "User Belum Login, arahkan ke halaman Login")
        }
    }

    fun testCreateUser() {
        auth.createUserWithEmailAndPassword("rogers@basicteknologi.co.id", "123456")
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("debug", "createUserWithEmail:success")
                val user = auth.currentUser
                Log.d("debug", user.toString())
            } else {
                // If sign in fails, display a message to the user.
                Log.w("debug", "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun testSignIn(){
        auth.signInWithEmailAndPassword("rogers@basicteknologi.co.id", "123456")
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("debug", "signInWithEmail:success")
                val user = auth.currentUser
                Log.d("debug", user.toString());
            } else {
                // If sign in fails, display a message to the user.
                Log.w("debug", "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }
}