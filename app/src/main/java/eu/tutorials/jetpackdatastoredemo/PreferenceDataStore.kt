package eu.tutorials.jetpackdatastoredemo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

//Todo 5: create a name for the datastore file and initialize
private const val STORE_USER = "store_user"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_USER)

//Todo 4:create a class for the datastore operations
class PreferenceDataStore(private val context: Context) {

    //Todo 6:create a key each for username and email
    val USER_NAME_KEY = stringPreferencesKey("user_name")
    val USER_EMAIL_KEY = stringPreferencesKey("user_email")

    /**
     * Todo 7: We create a flow with type string to read each data
     * if there is an exception it emits an empty preference or throw the exception
     * if it succedes it returns the value by its key
     */
    //start
    val userNameFlow: Flow<String> = context.dataStore.data.catch { exception ->

            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
    }.map { preferences ->
            preferences[USER_NAME_KEY]?:""
        }

    val userEmailFlow: Flow<String> = context.dataStore.data.catch { exception ->

        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
            preferences[USER_EMAIL_KEY]?:""
        }
//end

    /**
     * Todo 8: We create suspend functions to save each data
     * We edit the file and save to the key
     * */
    //start
    suspend fun saveUserName(userName:String) {
        context.dataStore.edit { user_name ->
             user_name[USER_NAME_KEY]  = userName
        }
    }
    suspend fun saveUserEmail(userEmail : String) {
        context.dataStore.edit { user_email ->
            user_email[USER_EMAIL_KEY]  = userEmail
        }
    }
    suspend fun clearUserNameAndEmail(){
        context.dataStore.edit {
            it.clear()
        }
    }
    //end
}