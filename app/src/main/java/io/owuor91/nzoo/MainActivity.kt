package io.owuor91.nzoo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import io.owuor91.nzooimport.ListRvAdapter
import kotlinx.android.synthetic.main.activity_main.fabAddPhoto
import kotlinx.android.synthetic.main.activity_main.rvText

class MainActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
  
  override fun onResume() {
    super.onResume()
    fabAddPhoto.setOnClickListener {
      //var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
      var cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
      startActivityForResult(cameraIntent, 234)
    }
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode==234 && resultCode==Activity.RESULT_OK){
      var bitmap = getBitmapFromUri(data?.data as Uri)
      runTextRecognition(bitmap)
    }
  }
  
  private fun getBitmapFromUri(filePath: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
  }
  
  fun runTextRecognition(selectedImage: Bitmap) {
    val image = FirebaseVisionImage.fromBitmap(selectedImage)
    val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
    
    detector.processImage(image)
      .addOnSuccessListener { firebaseVisionText-> processExtractedText(firebaseVisionText) }
      .addOnFailureListener { exception-> Toast.makeText(baseContext, exception.message as String, Toast
        .LENGTH_LONG).show()}
  }
  
  private fun processExtractedText(firebaseVisionText: FirebaseVisionText) {
    var blocks = firebaseVisionText.textBlocks
    
    if (blocks.size==0){
      Toast.makeText(baseContext, "No text found", Toast.LENGTH_LONG).show()
      return
    }
    
    var lines:MutableList<String> = ArrayList<String>()
    
    blocks.forEach { block->
      block.lines.forEach { line -> lines.add(line.text) }
    }
    
    Toast.makeText(baseContext, lines.size.toString(), Toast.LENGTH_LONG).show()
    
    var adapter = ListRvAdapter()
    adapter.data = lines
    
    rvText.layoutManager = LinearLayoutManager(baseContext)
    rvText.adapter = adapter
  }
  
}
