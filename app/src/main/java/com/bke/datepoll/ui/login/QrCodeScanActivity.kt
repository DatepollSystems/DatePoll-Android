package com.bke.datepoll.ui.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bke.datepoll.R
import com.bke.datepoll.vm.ServerInputViewModel
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.android.synthetic.main.activity_qr_code_scan.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrCodeScanActivity : AppCompatActivity() {

    private lateinit var dbvScanner: DecoratedBarcodeView
    private val QR_CODE_DATA = "qrCodeData"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scan)

        dbvScanner = dbv_barcode
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
        if (requestCode == 0 && grantResults.isEmpty()) {
            requestPermission()
        } else {
            dbvScanner.resume()
        }
    }

    private fun resumeScanner() {
        if (!dbvScanner.isActivated)
            dbvScanner.resume()
    }

    private fun pauseScanner() {
        dbvScanner.pause()
    }

    fun requestPermission() {
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
