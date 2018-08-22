package com.example.tankup;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
//appcompactActivityis the base class for activities that use support lib action bar features
    public MediaPlayer mp;
    UserVehicleData helper=new UserVehicleData(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mp=MediaPlayer.create(this,R.raw.beep);
    }

    void Click(View v)
    {

        if (v.getId() == R.id.button_newaccount)      //user want to sign up
        {
            mp.start();
            Intent inA = new Intent();
            inA.setClass(this, CreateAccount.class);   //start CreateActivity
            startActivity(inA);
        }

        else if (v.getId() == R.id.button_signIn)    //user want to sign in
        {
            mp.start();
            EditText unm = (EditText) findViewById(R.id.editText_email);
            String email = unm.getText().toString();
            EditText ps = (EditText) findViewById(R.id.editPassword);
            String pass = ps.getText().toString();

            String passSer = helper.searchPass(email);    //search for password in database
            if(passSer==null)      //if user not sign up
            {
                Toast.makeText(LoginActivity.this, "Your are not sign up!", Toast.LENGTH_SHORT).show();
                unm.setText("");
                ps.setText("");
            }
            else if (passSer.equals(pass))   //else call to vehicleAdd module
            {
                unm.setText("");
                ps.setText("");
                Intent in = new Intent(this, VehicleAdd.class);
                in.putExtra("mail", email);
                in.setClass(this, VehicleAdd.class);
                startActivity(in);
            }
            else      //if password not match to database password
            {
                Toast passToast = Toast.makeText(LoginActivity.this, "Username and password don't match!", Toast.LENGTH_SHORT);
                passToast.show();
            }
        }
    }
}