package com.androdevsatyam.bimafcm

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var spinner: AppCompatSpinner
    lateinit var sendNotificationButton: Button
    lateinit var receiverFdmId: TextInputEditText
    lateinit var title: TextInputEditText
    lateinit var message: TextInputEditText
    lateinit var image_data: TextInputEditText
    lateinit var key: TextInputEditText
    lateinit var value: TextInputEditText
    var topic: String = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        initViews()
        sendNotificationButton.setOnClickListener { view ->
            if (validateInput()) {
                val payload: JsonObject = buildNotificationPayload()
                ApiClient.getApiService().sendNotification(payload).enqueue(
                    object : Callback<JsonObject?> {
                        override fun onResponse(
                            call: Call<JsonObject?>?,
                            response: Response<JsonObject?>
                        ) {
                            if (response.isSuccessful()) {
                                Toast.makeText(
                                    this@MainActivity, "Notification send successful",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<JsonObject?>?, t: Throwable?) {
                            Log.d("TAG", "onFailure: ${t?.message}")
                        }
                    })
            }
        }
    }

    private fun buildNotificationPayload(): JsonObject {
        // compose notification json payload
        val payload = JsonObject()
//        if (receiverFdmId.text.toString().length > 20)
//            payload.addProperty("to", receiverFdmId.getText().toString())
//        else
        payload.addProperty("to", "/topics/" + topic)
        // compose data payload here
        val data = JsonObject()
        data.addProperty("title", title.text.toString())
        data.addProperty("body", message.text.toString())
        data.addProperty("image", image_data.text.toString())

        val notification=JsonObject()
        notification.addProperty("icon", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRweRVhFaoNU0Gfm5zf6dhnhqI8k1w9DyW8o8vx2fKmSw&s")

//        data.addProperty(key.getText().toString(), value.getText().toString())
        // add data payload
        payload.add("notification", data)
//        payload.add("data", notification)
        return payload
    }

    private fun validateInput(): Boolean {
        if (title.text.toString().isEmpty()
            || message.getText().toString().isEmpty()
//            || key.getText().toString().isEmpty()
//            || value.getText().toString().isEmpty()
        ) {
            Toast.makeText(this, "Please fill all field correctly", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun initViews() {
        sendNotificationButton = findViewById(R.id.buttonSendNotification)
        receiverFdmId = findViewById(R.id.textReceiverToken)
        title = findViewById(R.id.textTitle)
        message = findViewById(R.id.textMessage)
        image_data = findViewById(R.id.image_data)
        key = findViewById(R.id.textKey)
        value = findViewById(R.id.textValue)
        spinner = findViewById(R.id.spinner)

        spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                Log.d("TAG", "onItemSelected: $selectedItemView")
                var view=selectedItemView as TextView
                topic=view.text.toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
    }
}