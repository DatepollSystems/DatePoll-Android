package com.datepollsystems.datepoll.components.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.datepollsystems.datepoll.R
import com.datepollsystems.datepoll.databinding.ActivityQrCodeScanBinding
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class QrCodeScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrCodeScanBinding
    private lateinit var dbvScanner: DecoratedBarcodeView
    private val QR_CODE_DATA = "qrCodeData"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeScanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        dbvScanner = binding.dbvBarcode
        dbvScanner.setStatusText("")
        dbvScanner.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                val i = Intent()
                i.putExtra(QR_CODE_DATA, result.text)
                setResult(Activity.RESULT_OK, i)
                finish()
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })


    }

    override fun onStart() {
        super.onStart()
        requestPermission()
        resumeScanner()
    }

    override fun onRestart(){
        super.onRestart()
        resumeScanner()
    }

    override fun onResume() {
        super.onResume()
        resumeScanner()
    }

    override fun onPause() {
        super.onPause()
        pauseScanner()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults[0] < 0) {
            finish()
        } else {
            dbvScanner.resume()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun resumeScanner() {
        if (!dbvScanner.isActivated)
            dbvScanner.resume()
    }

    private fun pauseScanner() {
        dbvScanner.pause()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                0
            )
        }
    }

}
