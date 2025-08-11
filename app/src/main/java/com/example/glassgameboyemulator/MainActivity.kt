package com.example.glassgameboyemulator

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.SurfaceView
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import com.google.android.glass.widget.CardBuilder
import com.google.android.glass.widget.CardScrollView
import java.io.File

class MainActivity : Activity() {
    private lateinit var surfaceView: SurfaceView
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val REQUEST_BLUETOOTH_PERMISSIONS = 1
    private val REQUEST_STORAGE_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        surfaceView = SurfaceView(this)
        setContentView(surfaceView)

        requestPermissions()
        initEmulator()

        val romPath = "${Environment.getExternalStorageDirectory().path}/roms/tetris.gb"
        val romFile = File(romPath)
        if (romFile.exists()) {
            loadRom(romPath)
        } else {
            Log.e("Emulator", "ROM not found at $romPath")
            showRomSelectionCard()
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.READ_EXTERNAL_STORAGE"
        )
        val grantResults = IntArray(permissions.size)
        var allGranted = true
        for (i in 0 until permissions.size) {
            if (checkCallingOrSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false
                grantResults[i] = PackageManager.PERMISSION_DENIED
            } else {
                grantResults[i] = PackageManager.PERMISSION_GRANTED
            }
        }
        if (!allGranted) {
            Log.e("Emulator", "Permissions denied")
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS || requestCode == REQUEST_STORAGE_PERMISSION) {
            var allGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false
                    break
                }
            }
            if (!allGranted) {
                Log.e("Emulator", "Permissions denied")
                finish()
            }
        }
    }

    private fun showRomSelectionCard() {
        val cardScrollView = CardScrollView(this)
        val card = CardBuilder(this, CardBuilder.Layout.TEXT)
            .setText("No ROM found. Place ROMs in /sdcard/roms/ and restart.")
        cardScrollView.setAdapter(object : CardBuilder.CardScrollAdapter() {
            override fun getCount(): Int = 1
            override fun getItem(position: Int): Any = card
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                return card.getView(convertView, parent)
            }
        })
        setContentView(cardScrollView)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_DPAD_UP -> setButtonState(0, true)
                KeyEvent.KEYCODE_DPAD_DOWN -> setButtonState(1, true)
                KeyEvent.KEYCODE_DPAD_LEFT -> setButtonState(2, true)
                KeyEvent.KEYCODE_DPAD_RIGHT -> setButtonState(3, true)
                KeyEvent.KEYCODE_BUTTON_A -> setButtonState(4, true)
                KeyEvent.KEYCODE_BUTTON_B -> setButtonState(5, true)
                KeyEvent.KEYCODE_BUTTON_START -> setButtonState(6, true)
                KeyEvent.KEYCODE_BUTTON_SELECT -> setButtonState(7, true)
            }
        } else if (event.action == KeyEvent.ACTION_UP) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_DPAD_UP -> setButtonState(0, false)
                KeyEvent.KEYCODE_DPAD_DOWN -> setButtonState(1, false)
                KeyEvent.KEYCODE_DPAD_LEFT -> setButtonState(2, false)
                KeyEvent.KEYCODE_DPAD_RIGHT -> setButtonState(3, false)
                KeyEvent.KEYCODE_BUTTON_A -> setButtonState(4, false)
                KeyEvent.KEYCODE_BUTTON_B -> setButtonState(5, false)
                KeyEvent.KEYCODE_BUTTON_START -> setButtonState(6, false)
                KeyEvent.KEYCODE_BUTTON_SELECT -> setButtonState(7, false)
            }
        }
        return super.dispatchKeyEvent(event)
    }

    // JNI functions
    external fun initEmulator()
    external fun loadRom(romPath: String)
    external fun runFrame()
    external fun setButtonState(button: Int, pressed: Boolean)

    companion object {
        init {
            System.loadLibrary("sameboy")
        }
    }
}