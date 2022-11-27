package com.app.transitbookingsystem.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import com.app.transitbookingsystem.models.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class LoginActivity : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var progressLayout: ConstraintLayout
    private  val sharedFileName = "users"
    lateinit var database: DatabaseReference

    lateinit var sp: SharedPreferences

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    private val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sp = this.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)

        val isLoggedIn = sp.getBoolean("isLoggedIn", false)
        val role = sp.getString("role", "0")

        if(isLoggedIn){
            if(role == "0"){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else {
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        database = Firebase.database.reference

        loginButton = findViewById(R.id.loginBtn)
        progressLayout = findViewById(R.id.progressLayout)


        loginButton.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            signInGoogle()
        }
    }
    private fun signInGoogle(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            val user = User(
                account.displayName!!,
                "0",
                account.email!!,
                "hostel"
            )

            val domain= account.email!!.split('@')[1]
            if(domain!="sliet.ac.in"){
                Toast.makeText(this, "Sign in with sliet email", Toast.LENGTH_LONG).show()
                mGoogleSignInClient.signOut()
            }
            else{
                var list: HashMap<String, HashMap<String, String>>
                database.child("users").get().addOnSuccessListener {
                    if(it.value == null){
                        addUser(user, this)
                        return@addOnSuccessListener
                    }
                    list= it.value as HashMap<String, HashMap<String, String>>
                    if(list.isEmpty()){
                        addUser(user, this)
                        return@addOnSuccessListener
                    }
                    else{
                        for(s in list.keys){
                            val temp = list[s]
                            if(temp?.get("email") == account.email){
                                user.role = temp?.get("role")
                                user.hostel = temp?.get("hostel")
                                success(user, this)
                                return@addOnSuccessListener
                            }
                        }
                        addUser(user, this)
                    }
                }.addOnFailureListener {
                    it.printStackTrace()
                    progressLayout.visibility = View.INVISIBLE
                    Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show()
                }
            }
            progressLayout.visibility = View.INVISIBLE
        } catch (e: ApiException) {
            progressLayout.visibility = View.INVISIBLE
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun addUser(user: User, context: Context){
        val id = user.email?.split('@')?.get(0)!!
        database.child("users").child(id).setValue(user).addOnFailureListener {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show()
            return@addOnFailureListener
        }.addOnSuccessListener {
            success(user, context)
        }
    }

    private fun success(user: User, context: Context){
        val sp = this.getSharedPreferences(sharedFileName, MODE_PRIVATE)
        val editor = sp.edit()
        user.role?.let { editor.putString("role", it) }
        editor.putString("name", user.name)
        editor.putString("email", user.email)
        editor.putBoolean("isLoggedIn", true)
        editor.putString("hostel", user.hostel)
        editor.apply()
        editor.commit()
        startMyActivity(user, context)
        finish()
    }

    private fun startMyActivity(user: User, context: Context){
        if(user.role == "0"){
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        }
        else{
            startActivity(Intent(context, AdminActivity::class.java))
            finish()
        }
    }
}