package multialert.android.imagepick

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var imagePickingTXT: TextView? = null
    val REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagePickingTXT = findViewById(R.id.img_picking_txt)

        imagePickingTXT!!.setOnClickListener(View.OnClickListener {
            openGalleryForImages()
        })

    }

    private fun openGalleryForImages() {
        if (Build.VERSION.SDK_INT < 19){
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose Pictures"),REQUEST_CODE)
        }else{
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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