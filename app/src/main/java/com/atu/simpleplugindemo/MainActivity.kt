package com.atu.simpleplugindemo

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.reflect.Array.newInstance

class MainActivity : AppCompatActivity() {
    private val path = Environment.getExternalStorageDirectory().toString() + "/output.dex"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btn_load.setOnClickListener {
            Utils.loadClass(this.applicationContext)
            val file = File(path)
            if (!file.exists()) {
                Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                val loadClass = Class.forName("com.atu.plugin.Test")
                val method = loadClass.getMethod("print")
                method.invoke(null)
            } catch (e: Exception) {
                Log.e("====", e.message)
            }
        }

        btn_copy.setOnClickListener {
            val input = assets.open("output.dex")
            var fos: OutputStream? = null
            try {
                val file = File(path)
                if (!file.exists())
                    file.createNewFile()
                fos = FileOutputStream(file)
                val b = ByteArray(1024)
                var len = 0
                while ({ len = input.read(b);len }() != -1) {
                    fos.write(b, 0, len)
                }

            } catch (e: Exception) {
                Log.e("===", "文件写入${e.message}")
            } finally {
                fos?.flush()
                fos?.close()
                input.close()
            }
        }
    }
}