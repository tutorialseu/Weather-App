package eu.tutorials.simpleapicalldemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val result = callApiLogin()
            //Todo 3 read data from json using gson
            val responseData = Gson().fromJson(result, ResponseData::class.java)
            Log.i("Message", "${responseData.message}")
            Log.i("Email", "${responseData.email}")
            Log.i("Mobile", "${responseData.mobile}")
            Log.i("Is Profile Completed", "${responseData.profile_details.is_profile_completed}")
            Log.i("Rating", "${responseData.profile_details.rating}")
            Log.i("Data List Size", "${responseData.data_list.size}")
            for (item in responseData.data_list.indices) {
                Log.i("Value $item", "${responseData.data_list[item]}")
                Log.i("id", "${responseData.data_list[item].id}")
                Log.i("Value ", "${responseData.data_list[item].value}")
            }
        }
    }

    private lateinit var customProgressDialog: Dialog

    private fun showProgressDialog() {
        customProgressDialog = Dialog(this)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.show()
    }

    private fun cancelDialog() {
        customProgressDialog.dismiss()
    }

    suspend fun callApiLogin(): String = withContext(Dispatchers.IO) {
        var result = ""
        runOnUiThread {
            showProgressDialog()
        }
        var connection: HttpURLConnection? = null
        try {
            val url = URL("https://run.mocky.io/v3/e1b48409-8bbc-459c-973d-1b72c56da455")
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