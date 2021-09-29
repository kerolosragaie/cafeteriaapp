package com.evapharma.cafeteriaapp.helpers

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast

object UserHelper {

    /**
     *To validate input data
     * */
    fun validateData(_context: Context?, _email:EditText?, _password: EditText?):Boolean{
        when{
            _email?.text!!.trim().isEmpty() && _password?.text!!.isEmpty() -> {
                _email.error="Email cannot be empty!"
                _password.error="Password cannot be empty!"
                Toast.makeText(_context, "Please fill all the fields!", Toast.LENGTH_SHORT).show()
            }
            _email.text!!.trim().isEmpty() -> {
                _email.error="Email cannot be empty!"
            }
            !Patterns.EMAIL_ADDRESS.matcher(_email.text).matches()->{
                _email.error = "Enter valid email."
            }
            _password?.text!!.isEmpty()->{
                _password.error="Password cannot be empty!"
            }
            _password.text!!.length < 8 ->{
                _password.error="Password cannot be less than 8 characters."
            }
            else->{
                _email.error=null
                _password.error = null
                return true
            }
        }
        return false
    }

}