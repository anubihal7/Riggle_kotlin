package com.rigle.servicehub.data.local

import android.content.SharedPreferences
import android.util.Log
import com.rigle.servicehub.data.model.RigleConstantsResponse
import com.rigle.servicehub.data.model.User

import com.rigle.servicehub.utils.extension.getValue
import com.rigle.servicehub.utils.extension.saveValue
import com.google.gson.Gson
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    private val gson = Gson()

    companion object {
        const val USER = "user"
        const val SESSION = "session"
        const val BREARER = "baerer"
        const val POSTS = "posts"
        var constants: RigleConstantsResponse? = null
    }

    fun saveRigleConstants(response: RigleConstantsResponse) {
        constants = response
    }

    fun getRigleConstants(): RigleConstantsResponse? {
        return constants
    }

    fun saveUser(response: User?) {
        response?.let {
            sharedPreferences.saveValue(USER, gson.toJson(response))
        }
    }

    fun saveSession(session: String) {
        sharedPreferences.saveValue(SESSION, session)
    }

    fun getSession(): String? {
        return sharedPreferences.getValue(SESSION)
    }

    fun saveAuth(sessionId: String) {
        /*  var data = ByteArray(0)
          try {
              data = ("$username:$password").toByteArray(charset("UTF-8"))
          } catch (e: UnsupportedEncodingException) {
              e.printStackTrace()
              return
          }*/
        //  sharedPreferences.saveValue(BREARER, "Basic " + Base64.encodeToString(data, Base64.NO_WRAP))
        sharedPreferences.saveValue(BREARER, sessionId)
    }

    fun getAuth(): String {
        val token = sharedPreferences.getValue<String>(BREARER).toString()
        Log.e("Token", "" + token)
        return token
    }

    fun getCurrentUser(): User? {
        return try {
            val s: String? = sharedPreferences.getValue(USER, null);
            gson.fromJson(s, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        val userBean = getCurrentUser();
        /*  userBean?.let {
              headerMap["userid"] = it.userid.toString()
              headerMap["sessionkey"] = it.sessionKey.toString()
          }*/
        return headerMap.toMap()
    }

    fun clearUser() {
        val edit = sharedPreferences.edit().clear()
        edit.apply()
    }

/*  fun savePosts(it: List<PostModel>?) {
      var posts = gson.toJson(it)
      sharedPreferences.saveValue(POSTS, posts)
  }

  fun getPosts(): List<PostModel>? {
      var posts = sharedPreferences.getValue<String>(POSTS)
      val empMapType = object : TypeToken<List<PostModel>>() {}.type
      return gson.fromJson<List<PostModel>>(posts, empMapType)
  }
*/
}


