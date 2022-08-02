package com.sanjeet.innobuz

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.sanjeet.innobuz.ui.HomeFragment

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, HomeFragment()).commit()
        }
    }
}