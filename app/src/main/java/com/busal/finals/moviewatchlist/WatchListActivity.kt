package com.busal.finals.moviewatchlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.busal.finals.moviewatchlist.databinding.ActivityWatchListBinding

class WatchListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWatchListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWatchListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeMenu-> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.filterSearchMenu -> {
                    replaceFragment(FilterFragment())
                    true
                }
                R.id.watchedMenu -> {
                    replaceFragment(WatchedFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager  = this.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayout.id,fragment)
        fragmentTransaction.commit()

    }
}