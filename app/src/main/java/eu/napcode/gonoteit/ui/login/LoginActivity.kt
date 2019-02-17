package eu.napcode.gonoteit.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextWatcher
import android.transition.Explode
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

import javax.inject.Inject

import dagger.android.AndroidInjection
import eu.napcode.gonoteit.AuthenticateMutation
import eu.napcode.gonoteit.R
import eu.napcode.gonoteit.app.utils.SimpleTextWatcher
import eu.napcode.gonoteit.di.modules.viewmodel.ViewModelFactory
import eu.napcode.gonoteit.repository.Resource
import eu.napcode.gonoteit.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var viewModel: LoginViewModel? = null

    var inputWatcher: TextWatcher = object : SimpleTextWatcher() {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            viewModel!!.setLogin(loginEditText.text.toString())
            viewModel!!.setPassword(passwordEditText.text.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupInputFields()
        setupAnimations()

        AndroidInjection.inject(this)
        setupViewModel()
    }

    private fun setupInputFields() {
        passwordEditText.addTextChangedListener(inputWatcher)
        loginEditText.addTextChangedListener(inputWatcher)

        passwordEditText.setOnEditorActionListener { view, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEND) {
                login()

                true
            }

            false
        }

        loginButton.setOnClickListener { login() }
    }

    private fun setupAnimations() {
        val explode = Explode()
        explode.duration = resources.getInteger(R.integer.anim_duration_medium).toLong()

        window.enterTransition = explode
    }

    private fun login() {
        this.viewModel!!.login().observe(this, Observer { processLogin(it!!) })
        hideKeyboard()
    }

    private fun processLogin(resource: Resource<AuthenticateMutation.Data>) {

        if (resource.status == Resource.Status.LOADING) {
            progressBar.visibility = View.VISIBLE
            loginButton.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            loginButton.isEnabled = true
        }

        if (resource.status == Resource.Status.SUCCESS) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

            return
        }

        if (resource.status == Resource.Status.ERROR) {
            var message = resource.message

            if (resource.message == null) {
                message = getString(R.string.login_error)
            }

            Snackbar.make(constraintLayout, message!!, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus

        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setupViewModel() {
        this.viewModel = ViewModelProviders
                .of(this, this.viewModelFactory)
                .get(LoginViewModel::class.java)

        this.viewModel!!.areInputsValid().observe(this,
                Observer{  valid -> loginButton.isEnabled = valid!! })
    }
}
