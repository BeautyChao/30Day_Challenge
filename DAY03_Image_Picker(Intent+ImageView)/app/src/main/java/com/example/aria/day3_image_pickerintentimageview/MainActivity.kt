package com.example.aria.day3_image_pickerintentimageview

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private companion object {
        val PHOTO_FROM_GALLERY = 0
        val PHOTO_FROM_CAMERA = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permission()

        toAlbum.setOnClickListener {
            toAlbum()
        }

        toCamera.setOnClickListener {
            toCamera()
        }
    }

    lateinit var saveUri: Uri

    fun permission() {
            ActivityCompat.requestPermissions(this, arrayOf( android.Manifest.permission.CAMERA), 0)
    }

    fun toAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, PHOTO_FROM_GALLERY)
    }

    fun toCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val tmpFile = File(Environment.getExternalStorageDirectory().toString(), System.currentTimeMillis().toString() + ".jpg")
        val uriForCamera = FileProvider.getUriForFile(this, "com.example.aria.day3_image_pickerintentimageview.fileprovider", tmpFile)

        saveUri = uriForCamera
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForCamera)
        startActivityForResult(intent, PHOTO_FROM_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        if (requestCode == 0) {

            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "相機將無法正常使用", Toast.LENGTH_SHORT).show()
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                    Log.wtf("aaaaa","0 : CAMERA Results DENIED to 1" )
                }
                Log.wtf("aaaaa","0 : CAMERA Results DENIED" )
            }

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                    Log.wtf("aaaaa","0 : CAMERA Results GRANTED" )
                }
            }

        }

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "存取將無法正常使用", Toast.LENGTH_SHORT).show()
                Log.wtf("aaaaa","1 :WRITE Results DENIED" )

            }

            if (grantResults[1] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "存取將無法正常使用", Toast.LENGTH_SHORT).show()
                Log.wtf("aaaaa","1 :READ Results DENIED" )

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PHOTO_FROM_GALLERY -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val uri = data!!.data
                        imageView.setImageURI(uri)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }

            PHOTO_FROM_CAMERA -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Glide.with(this).load(saveUri).into(imageView)
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }

            }
        }
    }

    fun dailog(){
        AlertDialog.Builder(this)
                .setTitle("提醒")
                .setMessage("相機功能將無法使用")
    }
}
