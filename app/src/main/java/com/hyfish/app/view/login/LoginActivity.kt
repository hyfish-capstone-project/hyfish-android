package com.hyfish.app.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hyfish.app.R
import com.hyfish.app.data.api.LoginRequest
import com.hyfish.app.databinding.ActivityLoginBinding
import com.hyfish.app.view.ViewModelFactory
import com.hyfish.app.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        binding.textView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.loading.observe(this) {
            binding.btSubmit.isEnabled = !it
        }

        viewModel.error.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                showError(message)
            }
        }
        setupAction()
    }

    private fun setupAction() {
        binding.btSubmit.setOnClickListener {
            val username = binding.inUsername
            val password = binding.inPassword
            if (username.error !== null || password.error !== null) {
                Toast.makeText(
                    this, "Username and Password are not valid", Toast.LENGTH_SHORT
                ).show()
            } else if (username.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                when {
                    username.text.toString().isEmpty() -> username.error =
                        "Username cannot be empty"

                    password.text.toString().isEmpty() -> password.error =
                        "Password cannot be empty"
                }
            } else {
                val loginRequest = LoginRequest(username.text.toString(), password.text.toString())
                viewModel.login(loginRequest)
            }
        }
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.error))
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { _, _ -> }
            create()
            show()
        }
    }
}