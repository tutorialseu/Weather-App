package eu.tutorials.simpleapicalldemo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
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

        lifecycleScope.launch{
         callApiLogin("Panjutorials","123456")
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
    suspend fun callApiLogin(username: String, password: String): String = withContext(Dispatchers.IO) {
        var result = ""
        runOnUiThread {
            showProgressDialog()
        }
        var connection: HttpURLConnection? = null
        try {
            val url = URL("https://run.mocky.io/v3/90f5b2e6-5ac3-4a59-ace7-c29d990c490a")
            connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.doOutput = true

            /**
             * Sets whether HTTP redirects should be automatically followed by this instance.
             * The default value comes from followRedirects, which defaults to true.
             */
            connection.instanceFollowRedirects = false

            /**
             * Set the method for the URL request, one of:
             *  GET
             *  POST
             *  HEAD
             *  OPTIONS
             *  PUT
             *  DELETE
             *  TRACE
             *  are legal, subject to protocol restrictions.  The default method is GET.
             */
            connection.requestMethod = "POST"

            /**
             * Sets the general request property. If a property with the key already
             * exists, overwrite its value with the new value.
             */
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("charset", "utf-8")
            connection.setRequestProperty("Accept", "application/json")

            /**
             * Some protocols do caching of documents.  Occasionally, it is important
             * to be able to "tunnel through" and ignore the caches (e.g., the
             * "reload" button in a browser).  If the UseCaches flag on a connection
             * is true, the connection is allowed to use whatever caches it can.
             *  If false, caches are to be ignored.
             *  The default value comes from DefaultUseCaches, which defaults to
             * true.
             */
            connection.useCaches = false

            /**
             * Creates a new data output stream to write data to the specified
             * underlying output stream. The counter written is set to zero.
             */
            val wr = DataOutputStream(connection.outputStream)

            // Create JSONObject Request
            val jsonRequest = JSONObject()
            jsonRequest.put("username", username) // Request Parameter 1
            jsonRequest.put("password", password) // Request Parameter 2

            /**
             * Writes out the string to the underlying output stream as a
             * sequence of bytes. Each character in the string is written out, in
             * sequence, by discarding its high eight bits. If no exception is
             * thrown, the counter written is incremented by the
             * length of s.
             */
            wr.writeBytes(jsonRequest.toString())
            wr.flush() // Flushes this data output stream.
            wr.close() // Closes this output stream and releases any system resources associated with the stream

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