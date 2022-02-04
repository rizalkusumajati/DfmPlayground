package com.dfmplay.dfminstallbackground

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dfmplay.dfminstallbackground.R
import com.google.android.play.core.splitcompat.SplitCompat

class BackgroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background)
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}