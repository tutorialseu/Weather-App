package eu.tutorials.jetpackdatastoredemo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import eu.tutorials.jetpackdatastoredemo.databinding.ActivityDashBoardBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DashBoardActivity : AppCompatActivity() {

    ///Todo 14:We initialize preference datastore
    private val dataStore:PreferenceDataStore by lazy {
        PreferenceDataStore(this)
    }
    private val binding:ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchEmail()
        fetchUserName()

        binding.logout.setOnClickListener {
            lifecycleScope.launch {
                dataStore.clearUserNameAndEmail()
                this@DashBoardActivity.finish()
                startActivity(Intent(this@DashBoardActivity,MainActivity::class.java))
            }
        }

    }

    //Todo 15: create a method,launch a coroutine scope, fetch username and display on the textview
    private fun fetchUserName(){
        lifecycleScope.launch {
            dataStore.userNameFlow.collect { userName->
                binding.userName.text = "Welcome $userName "
                Log.d("username", "$userName")
            }
        }
    }

    //Todo 16: create a method,launch a coroutine scope, fetch email and display on the textview
    private fun fetchEmail(){
        lifecycleScope.launch {
        dataStore.userEmailFlow.collect { email->
            binding.email.text = "Your Email is $email"
            Log.d("useremail", "$email")

        }
    }
}
    //Todo 10 :create a starter for opening the dashboard activity
    companion object {
        fun StartDashBoardActivity(context: Context){
            context.startActivity(Intent(context,DashBoardActivity::class.java))
        }
    }
}