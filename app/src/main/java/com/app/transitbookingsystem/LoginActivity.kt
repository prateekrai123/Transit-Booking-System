package com.app.transitbookingsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sliet.transitbookingsystem.R

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginBtn)

        auth = Firebase.auth

        loginButton.setOnClickListener {
            val req = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.app_name))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build()
        }
    }
}