package com.example.clienteflask

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val baseUrl = "http://54.80.129.46:5000"
    private val client = OkHttpClient()
    private val JSON = "application/json; charset=utf-8".toMediaType()

    private lateinit var tvNombre: TextView
    private lateinit var tvCorreo: TextView
    private lateinit var tvRut: TextView
    private lateinit var tvResultadoPost: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNombre = findViewById(R.id.tvNombre)
        tvCorreo = findViewById(R.id.tvCorreo)
        tvRut = findViewById(R.id.tvRut)
        tvResultadoPost = findViewById(R.id.tvResultadoPost)

        val btnObtener = findViewById<Button>(R.id.btnObtener)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnObtener.setOnClickListener {
            obtenerDatosUsuario()
        }

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString()
            val password = etPassword.text.toString()
            enviarLogin(login, password)
        }
    }

    private fun obtenerDatosUsuario() {
        val request = Request.Builder()
            .url("$baseUrl/usuario")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    tvNombre.text = "Error: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && responseData != null) {
                    val json = JSONObject(responseData)
                    val nombre = json.getString("nombre")
                    val email = json.getString("email")
                    val rut = json.getString("rut")

                    runOnUiThread {
                        tvNombre.text = "Nombre: $nombre"
                        tvCorreo.text = "Correo: $email"
                        tvRut.text = "RUT: $rut"
                    }
                } else {
                    runOnUiThread {
                        tvNombre.text = "Error HTTP: ${response.code}"
                    }
                }
            }
        })
    }

    private fun enviarLogin(login: String, pass: String) {
        val jsonObject = JSONObject()
        jsonObject.put("login", login)
        jsonObject.put("password", pass)

        val requestBody = jsonObject.toString().toRequestBody(JSON)

        val request = Request.Builder()
            .url("$baseUrl/login")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    tvResultadoPost.text = "Error de conexión: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    val json = JSONObject(responseData)
                    val mensaje = json.getString("mensaje")
                    runOnUiThread {
                        tvResultadoPost.text = mensaje
                    }
                }
            }
        })
    }
}