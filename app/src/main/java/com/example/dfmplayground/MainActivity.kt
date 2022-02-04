package com.example.dfmplayground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.play.core.ktx.requestDeferredInstall
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManager
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManagerFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonForeground = findViewById<Button>(R.id.button)
        val buttonBackground = findViewById<Button>(R.id.buttonbackground)

        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val installedModules: Set<String> = splitInstallManager.installedModules

        var mySessionId = 1

        // Creates a listener for request status updates.
        val listener = SplitInstallStateUpdatedListener { state ->
            if (state.sessionId() == mySessionId) {
                // Read the status of the request to handle the state update.
                when(state.status()){
                    SplitInstallSessionStatus.DOWNLOADING -> {
                        Toast.makeText(this, "DOWNLOADING DFM", Toast.LENGTH_LONG).show()
                        Log.d("DFM","DOWNLOADING")
                    }
                    SplitInstallSessionStatus.DOWNLOADED -> {
                        Log.d("DFM","DOWNLOADED")
                    }
                    SplitInstallSessionStatus.INSTALLING -> {
                        Log.d("DFM","INSTALLING")
                    }
                    SplitInstallSessionStatus.INSTALLED -> {
                        Log.d("DFM","INSTALLED")
                        Log.d("DFM", splitInstallManager.installedModules.toString())
                        startActivity(Intent().setClassName("com.example.dfmplayground","com.example.dfminstallforeground.ForegroundActivity"))
                    }
                    SplitInstallSessionStatus.PENDING -> {
                        Log.d("DFM","PENDING")
                    }
                }

            }
        }

        // Registers the listener.

        val request =
            SplitInstallRequest
                .newBuilder()
                // You can download multiple on demand modules per
                // request by invoking the following method for each
                // module you want to install.
                .addModule("dfminstallforeground")
                .build()

        splitInstallManager.deferredInstall(listOf("dfminstallbackground")).addOnFailureListener { e -> Log.d("DFM", "Failed Deferred Install ${e.localizedMessage}") }.addOnSuccessListener { Log.d("DFM", "SUCCESS Deferred Install") }

        Log.d("DFM", splitInstallManager.installedModules.toString())
        splitInstallManager.registerListener(listener)

        buttonForeground.setOnClickListener {
            if (splitInstallManager.installedModules.contains("dfminstallforeground")){
                startActivity(Intent().setClassName("com.example.dfmplayground","com.example.dfminstallforeground.ForegroundActivity"))
            }else{
                splitInstallManager
                    // Submits the request to install the module through the
                    // asynchronous startInstall() task. Your app needs to be
                    // in the foreground to submit the request.
                    .startInstall(request)
                    // You should also be able to gracefully handle
                    // request state changes and errors. To learn more, go to
                    // the section about how to Monitor the request state.
                    .addOnSuccessListener { sessionId ->
                        if(sessionId == mySessionId) {
                            Log.d("DFM", "Success Install : $sessionId")
                            Log.d("DFM", installedModules.toString())
                        }
//
                    }
                    .addOnFailureListener { exception ->  Log.d("DFM", "Failed Install : ${exception.localizedMessage}") }

            }


        }

        buttonBackground.setOnClickListener {
            startActivity(Intent().setClassName("com.example.dfmplayground", "com.example.dfminstallbackground.BackgroundActivity"))
        }

    }


}