package com.rigle.servicehub.ui.scanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.View
import androidx.activity.viewModels
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rigle.servicehub.R
import com.rigle.servicehub.databinding.ActivityScannerBarcodeBinding
import com.rigle.servicehub.ui.base.BaseActivity
import com.rigle.servicehub.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
@AndroidEntryPoint
class ScannerBarcodeActivity : BaseActivity<ActivityScannerBarcodeBinding>() {
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
     val viewmodel: ScannerBarcodeViewModel by viewModels()

    override fun onCreateView() {
        binding.toolbar.tvTitle.text="Scan QR code"
        viewmodel.onClick.observe(this, Observer<View> { v ->
            if (v.id == R.id.iv_back) {
                finish()
            }
        })
        initialiseDetectorsAndSources()
    }

    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                openCamera()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
            }

            override fun receiveDetections(detections: Detections<Barcode>) {
                val barCode = detections.detectedItems
                if (barCode.size() > 0) {
                    setBarCode(barCode)
                }
            }
        })
    }

    private fun openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this@ScannerBarcodeActivity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                cameraSource.start(binding.surfaceView.holder)
            } else {
                ActivityCompat.requestPermissions(
                    this@ScannerBarcodeActivity,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setBarCode(barCode: SparseArray<Barcode>) {
        val text = barCode.valueAt(0).displayValue
        val intent = Intent()
        intent.putExtra("code", text)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }

    override fun onPause() {
        super.onPause()
        cameraSource.release()
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) finish() else openCamera()
        } else finish()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 201
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_scanner_barcode
    }

    override fun getViewModel(): BaseViewModel {
        return viewmodel
    }

}