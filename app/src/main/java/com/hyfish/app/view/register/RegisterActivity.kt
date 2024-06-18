package com.hyfish.app.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hyfish.app.R
import com.hyfish.app.data.api.auth.RegisterRequest
import com.hyfish.app.databinding.ActivityRegisterBinding
import com.hyfish.app.view.ViewModelFactory
import com.hyfish.app.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        viewModel.error.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                showError(message)
            }
        }

        showDialog()

        setupLogin()

        showLoading()

        setupRegisterButton()
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

    private fun setupLogin() {
        binding.textView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading() {
        viewModel.loading.observe(this) {
            binding.btSubmit.isEnabled = !it
            binding.progressIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupRegisterButton() {
        binding.btSubmit.setOnClickListener {
            if (!it.isEnabled) return@setOnClickListener

            if (binding.inPassword.error != null || binding.inConfirm.error != null) {
                return@setOnClickListener
            }

            val username = binding.inUsername.text.toString()
            val email = binding.inEmail.text.toString()
            val password = binding.inPassword.text.toString()
            val confirm = binding.inConfirm.text.toString()

            if (password != confirm) {
                showError(getString(R.string.register_confirm))
                return@setOnClickListener
            }

            viewModel.register(RegisterRequest(username, email, password, confirm))
        }
    }

    private fun showDialog() {
        viewModel.user.observe(this) {
            if (it != null) {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.success))
                    setMessage(getString(R.string.register_success))
                    setPositiveButton(getString(R.string.ok)) { _, _ ->
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }
}