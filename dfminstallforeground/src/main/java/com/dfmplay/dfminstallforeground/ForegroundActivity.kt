package com.dfmplay.dfminstallforeground

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dfmplay.dfminstallforeground.R
import com.google.android.play.core.splitcompat.SplitCompat

class ForegroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foreground)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)

    }
}