package com.universal.sdktest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //hidden input key.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, UniversalApisFragment())
                .commit()
        }
    }
}