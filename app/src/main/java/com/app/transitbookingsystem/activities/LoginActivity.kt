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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R

class LoginActivity : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var progressLayout: ConstraintLayout
    private  val sharedFileName = "users"

    lateinit var sp: SharedPreferences

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions
    private val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sp = this.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE)

        val isLoggedIn = sp.getBoolean("isLoggedIn", false)

        if(isLoggedIn){
            startActivity(Intent(this, MainActivity::class.java))
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



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
                0,
                account.email!!
            )

            val domain= account.email!!.split('@')[1]
            if(domain!="sliet.ac.in"){
                Toast.makeText(this, "Sign in with sliet email", Toast.LENGTH_LONG).show()
            }
            else{
                val sp = this.getSharedPreferences(sharedFileName, MODE_PRIVATE)
                val editor = sp.edit()
                user.role?.let { editor.putInt("role", it) }
                editor.putString("name", user.name)
                editor.putString("email", user.email)
                editor.putBoolean("isLoggedIn", true)
                editor.apply()
                editor.commit()
                startActivity(Intent(this, MainActivity::class.java))
            }
            progressLayout.visibility = View.INVISIBLE
        } catch (e: ApiException) {
            progressLayout.visibility = View.INVISIBLE
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}