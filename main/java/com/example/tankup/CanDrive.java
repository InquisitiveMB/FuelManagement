package com.example.tankup;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CanDrive extends AppCompatActivity {

    public MediaPlayer mp;
    ImageView image;
    UserVehicleData helper=new UserVehicleData(this);
    TextView textView_D, textView_E, textView_V, textView_FM,textView_SF;
    UserVehicleData userVehicleData=new UserVehicleData(this);
    double mg;
    String avaFuel,calDist,em,veh;
    Double requiredFuel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_drive);
        mp= MediaPlayer.create(this,R.raw.beep);
        Intent in=getIntent();
        requiredFuel=in.getDoubleExtra("calFuel", 0);
         em=in.getStringExtra("paM");
         veh=in.getStringExtra("vnm");
        textView_SF=(TextView)findViewById(R.id.textView_setRF);
        textView_SF.setText(String.valueOf(Math.round(requiredFuel)));
    }
    void DriveToDestination(View v)   //if user want to drive
    {
        mp.start();
        Contact c=new Contact();
        Cursor data1=helper.getDisplay1();
        while(data1.moveToNext())
        {
            String vname=data1.getString(1);
            if(vname.equals(veh) && data1.getString(2).equals(em))
            {
                avaFuel=data1.getString(0);                   //take available fuel in vehicle from database
                break;
            }
        }
        double finalFuel=Double.parseDouble(avaFuel)-requiredFuel;  //if he/she drives to distination fuel will be deducted from database
        boolean ch=true;
        c.setF_vname(veh);
        c.setFuel(String.valueOf(finalFuel));
        c.setF_email(em);
        ch=helper.addFuel_E(c); //update fuel table data

        if(ch==true)
        {
            Toast.makeText(this, "Fuel Updated", Toast.LENGTH_SHORT).show();
        }

        else
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        Intent in2 = new Intent(this, AvailFuel.class);
        in2.putExtra("VEH", veh);
        in2.putExtra("mail",em);
        in2.setClass(this, AvailFuel.class);
        startActivity(in2);
    }
}
