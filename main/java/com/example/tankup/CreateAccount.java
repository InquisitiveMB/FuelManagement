package com.example.tankup;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    public MediaPlayer mp;

    UserVehicleData helper=new UserVehicleData(this);
    EditText name;
    EditText email;
    EditText pass;
    EditText confPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mp= MediaPlayer.create(this,R.raw.beep);
        name=(EditText)findViewById(R.id.editText_name);
        email=(EditText)findViewById(R.id.editText_email);
        pass=(EditText)findViewById(R.id.editText_password);
        confPass=(EditText)findViewById(R.id.editText_confPassword);
        name.requestFocus();      //focus marker on name EditText
        setTitle("Create Account");    //set title of activity
    }


    public void onSignUpClick(View v)    //user want to sign up
    {
        mp.start();
        if(v.getId()==R.id.button_signUp)
        {
            //take data from EditText
            String namestr=name.getText().toString();
            String emailstr=email.getText().toString();
            String passstr=pass.getText().toString();
            String confPassstr=confPass.getText().toString();

            if(!passstr.equals(confPassstr))  //check user enter equl pass or not
            {
                Toast passToast=Toast.makeText(CreateAccount.this, "Password Not Match!", Toast.LENGTH_SHORT);
                passToast.show();
            }
            else
            {

                Contact c=new Contact();
                c.setName(namestr);
                c.setEmail(emailstr);
                c.setPass(passstr);

                boolean correct= helper.insertUsers(c);   //insert details in database

                if(correct==false)
                    Toast.makeText(this, "Error...Username should be unique", Toast.LENGTH_LONG).show();
                else   //again user should log in
                {
                    Toast.makeText(this, "You are Successfully Sign Up!!", Toast.LENGTH_SHORT).show();
                    Intent log=new Intent();
                    log.setClass(this, LoginActivity.class);
                    startActivity(log);
                }

            }
        }
    }

}
