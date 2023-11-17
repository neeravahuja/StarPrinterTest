package com.neerav.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var pdfFileList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.bttn)
        button.setOnClickListener { clicked() }
    }

    private fun clicked(){
        val builder = Uri.Builder()
        builder.scheme("starpassprnt")
        builder.authority("v1")
        builder.path("/print/nopreview")
        builder.appendQueryParameter("back", "startest://")


        try {
            val strList = resources.assets.list("")
            val stringArrayList = ArrayList<String>()
            stringArrayList.add("none")
            val stringPdfArrayList = ArrayList<String>()
            stringPdfArrayList.add("none")
            for (i in strList!!.indices) {
                if (strList[i].contains(".pdf")) {
                    stringPdfArrayList.add(strList[i])
                }
            }
            pdfFileList = stringPdfArrayList.toTypedArray<String>()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d("",pdfFileList.size.toString())
        val encode = Base64.encode(generatePrintData(pdfFileList[1]), Base64.DEFAULT)
        builder.appendQueryParameter("pdf", String(encode))


        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = builder.build()
        startActivity(intent)
    }

    private fun generatePrintData(file: String): ByteArray? {
        var `is`: InputStream? = null
        var readBytes: ByteArray? = null
        try {
            try {
                // open a file on assets folder
                `is` = this.assets.open(file)
                readBytes = ByteArray(`is`.available())
                `is`.read(readBytes)
            } finally {
                `is`?.close()
            }
        } catch (e: Exception) {
            // Do nothing
        }
        return readBytes
    }
}