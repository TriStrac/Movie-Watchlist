package com.busal.finals.moviewatchlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.busal.finals.moviewatchlist.databinding.ActivitySignInMainBinding

class SignInMainActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignInMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        proceedToMain()


    }
    private fun proceedToMain(){
        val intent = Intent(this, WatchListActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun isSignedIn():Boolean{
        return true
    }

}