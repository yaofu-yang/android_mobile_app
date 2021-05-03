package edu.neu.mad_sea.yaofuyang.dual_tic_tac_toe_classes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import edu.neu.mad_sea.yaofuyang.R
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class TicFCMActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_f_c_m)
    }


    fun sendMessageToDevice(type: View?) {
        val username = (findViewById<View>(R.id.tokenEditText) as EditText).text.toString()
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users/$username")
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userToken = snapshot.value.toString()
                Thread { sendMessageToDevice(userToken) }.start()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Failed Listener", Toast.LENGTH_SHORT).show()
            }
        }
        userRef.addListenerForSingleValueEvent(userListener)
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private fun sendMessageToDevice(targetToken: String) {
        val jPayload = JSONObject()
        val jNotification = JSONObject()
        try {
            jNotification.put("title", "Tic Tac Toe Challenge Request")
            jNotification.put("body", "Come Play Tic Tac Toe With Me~")
            jNotification.put("sound", "default")
            jNotification.put("badge", "1")
            /***
             * The Notification object is now populated.
             * Next, build the Payload that we send to the server.
             */

            // If sending to a single client
            jPayload.put("to", targetToken) // CLIENT_REGISTRATION_TOKEN);

            jPayload.put("priority", "high")
            jPayload.put("notification", jNotification)
            /***
             * The Payload object is now populated.
             * Send it to Firebase to send the message to the appropriate recipient.
             */
            val url = URL("https://fcm.googleapis.com/fcm/send")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", SERVER_KEY)
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            // Send FCM message content.
            val outputStream = conn.outputStream
            outputStream.write(jPayload.toString().toByteArray())
            outputStream.close()

            // Read FCM response.
            val inputStream = conn.inputStream
            val resp = convertStreamToString(inputStream)
            val h = Handler(Looper.getMainLooper())
            h.post {
                Log.e(TAG, "run: $resp")
                Toast.makeText(this@TicFCMActivity, resp, Toast.LENGTH_LONG).show()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun convertStreamToString(`is`: InputStream): String {
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next().replace(",", ",\n") else ""
    }

    companion object {
        private val TAG = TicFCMActivity::class.java.simpleName
        private const val SERVER_KEY = "key=AAAAS62dQx0:APA91bGHVF_r1mtKRO6c_O-b5YuH17qpqYPR206rQ3fQRfyeQyrJmLZzBf4cQMDomqtxTiNkFRO4KkvoglH8pOxA6rhuG4F6tEm35v8FnSyr35WcsCHxp77Srz47NwWh1pxLHgPOV-aA"
    }
}