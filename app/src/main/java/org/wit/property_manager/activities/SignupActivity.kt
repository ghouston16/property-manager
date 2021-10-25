package org.wit.property_manager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.property_manager.databinding.ActivitySignupBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.UserMemStore
import org.wit.property_manager.models.UserModel
import timber.log.Timber
import timber.log.Timber.i

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var loginIntent: Intent
    var user = UserModel()
    var candidate = UserModel()

    //   val users = UserMemStore()
    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        Timber.plant(Timber.DebugTree())

        i("Signup Activity started...")

        binding.btnSignup.setOnClickListener() {
            user.email = binding.userEmail.text.toString()
            user.password = binding.userPassword.text.toString()
            if (user.email.isNotEmpty() && user.password.isNotEmpty()) {
                i("User Added: $user.email")
                app.users.create(user.copy())
                Snackbar
                    .make(it, "Registration successful - Please log in..", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                Snackbar
                    .make(it, "Please Enter an Email and Password", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        // Login Method and validation
        binding.btnLogin.setOnClickListener() {
            candidate.email = binding.userEmail.text.toString()
            candidate.password = binding.userPassword.text.toString()
            val userList = app.users.findAll()
            if (candidate.email.isNotEmpty() && candidate.password.isNotEmpty()) {
                for (person in userList) {
                    if (candidate.email == person.email && candidate.password == person.password) {
                        i("User Logged In $user")
                        val launcherIntent = Intent(this, PropertyListActivity::class.java)
                        startActivityForResult(launcherIntent, 0)
                    } else {
                        Snackbar
                            .make(
                                it,
                                "Please Enter a valid Email and Password",
                                Snackbar.LENGTH_LONG
                            )
                            .show()
                    }
                }
            } else {
                Snackbar
                    .make(it, "Please Enter Both an Email and Password", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
