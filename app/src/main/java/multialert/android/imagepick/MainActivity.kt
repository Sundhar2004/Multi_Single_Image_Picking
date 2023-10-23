package multialert.android.imagepick

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var imagePickingTXT: TextView? = null
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagePickingTXT = findViewById(R.id.img_picking_txt)
        imagePickingTXT!!.setOnClickListener(View.OnClickListener {
            openGalleryForImages()
        })
    }

    private fun openGalleryForImages() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_REQUEST_CODE)
        } else {
            if (Build.VERSION.SDK_INT < 19){
                var intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                someActivityResultLauncher.launch(intent)
            }else{
                var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                someActivityResultLauncher.launch(intent)
            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT < 19){
                        var intent = Intent()
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        intent.action = Intent.ACTION_GET_CONTENT
                        someActivityResultLauncher.launch(intent)
                    }else{
                        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                        intent.type = "image/*"
                        someActivityResultLauncher.launch(intent)
                    }

                } else {
                    // Permission has been denied
                    // You may want to handle this situation accordingly
                }
                return
            }
        }
    }



    private val someActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Here you can handle the result
                val data: Intent? = result.data
                if (data!!.clipData != null){
                    var count = data.clipData?.itemCount
                    for (i in 0 until count!!){
                        var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri

                        Log.e("Multiple_img",imageUri.toString())
                    }
                }else if(data.data != null){
                    var imageUri: Uri = data.data!!
                    Log.e("single_img",imageUri.toString())
                }
            }
        }
}