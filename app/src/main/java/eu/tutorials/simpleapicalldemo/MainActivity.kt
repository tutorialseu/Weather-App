package eu.tutorials.simpleapicalldemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch{
            Log.i("JSON", callApiLogin())
        }
    }

    private lateinit var customProgressDialog: Dialog

    //Todo 5 initialize and show dialog
    private fun showProgressDialog() {
        customProgressDialog = Dialog(this)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.show()
    }

    //Todo 6 cancel dialog
    private fun cancelDialog() {
        customProgressDialog.dismiss()
    }
    //Todo 7 create a suspend function for the network call
    suspend fun callApiLogin(): String = withContext(Dispatchers.IO) {
        var result = ""
        //Todo 8: show the dialog on ui thread
        runOnUiThread {
            showProgressDialog()
        }
        var connection: HttpURLConnection? = null
        try {
            val url = URL("https://run.mocky.io/v3/90f5b2e6-5ac3-4a59-ace7-c29d990c490a")
            connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.doOutput = true
            val httpResult: Int = connection.responseCode
            if (httpResult == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                try {
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line + "\n")

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                result = stringBuilder.toString()
            } else {
                result = connection.responseMessage
            }
            //Todo 9: cancel the Dialog on ui thread
            runOnUiThread {
                cancelDialog()
            }

        } catch (e: SocketTimeoutException) {
            result = "Connection Timeout"
        } catch (e: IOException) {
            result = "Error: ${e.message}"
        } finally {
            connection?.disconnect()
        }
        result
    }.toString()

}