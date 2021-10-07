package eu.tutorials.jetpackdatastoredemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import eu.tutorials.jetpackdatastoredemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //Todo 9: We initialize preference data store
    private val dataStore:PreferenceDataStore by lazy {
        PreferenceDataStore(this)
    }

    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /** Todo 13: We fetch the value and check if there is an existing  username we navigate straight
         * into the DashBoard page
         */

        lifecycleScope.launch {
       dataStore.userNameFlow.collect {
         if (it.isNotEmpty()){
             DashBoardActivity.StartDashBoardActivity(this@MainActivity)
         }
       }
     }

        /**Todo 12: We set onclickListener on the login button, call saveUserNameWithEmail
         * and pass the value from user and email edit text
         */
        binding.login.setOnClickListener {
            saveUserNameWithEmail(binding.userName.text.toString(),binding.email.text.toString())

        }
    }

    /** Todo 11: We create string parameters for username and
     * if the string is not empty we launch a lifecyclescope and save the data
     * and then navigate to the DashBoardActivity
     */
    private fun saveUserNameWithEmail(userName:String,email:String){
        if(userName.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Username cannot be empty",Toast.LENGTH_SHORT).show()
        }else{
      lifecycleScope.launch {
          dataStore.saveUserName(userName)
          dataStore.saveUserEmail(email)

      }
            DashBoardActivity.StartDashBoardActivity(this@MainActivity)
        }
    }
}