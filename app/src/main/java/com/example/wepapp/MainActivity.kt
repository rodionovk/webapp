package com.example.wepapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.wepapp.ui.theme.WepappTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WepappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AndroidView(
                        modifier = Modifier.padding(innerPadding),
                        factory = { context ->
                            WebView(context).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                settings.javaScriptEnabled = true
                                addJavascriptInterface(WebAppInterface(context), "Android")
                                // Читаем содержимое файла или можно напрямую из assets
                                val fileContent = readAssetFile(context, "page.html")
                                loadDataWithBaseURL(null, fileContent, "text/html", "UTF-8", null);
                                // loadUrl("file:///android_asset/page.html");
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun readAssetFile(context: Context, fileName: String): String {
    val assetManager: AssetManager = context.assets
    val stringBuilder = StringBuilder()
    try {
        assetManager.open(fileName).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return stringBuilder.toString()
}

class WebAppInterface(private val context: Context) {

    @JavascriptInterface
    fun onClick(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}