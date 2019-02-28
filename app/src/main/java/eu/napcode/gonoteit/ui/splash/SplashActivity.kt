package eu.napcode.gonoteit.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import eu.napcode.gonoteit.app.utils.isHostValid
import eu.napcode.gonoteit.auth.StoreAuth
import eu.napcode.gonoteit.ui.login.LoginActivity

import eu.napcode.gonoteit.ui.main.MainActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var storeAuth: StoreAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        if(isHostValid(storeAuth.host)) {
            startActivity(Intent(this, MainActivity::class.java))

            return
        }

        startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }
}
