package com.example.tankup;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CanNotDrive extends AppCompatActivity {

    public MediaPlayer mp;
    UserVehicleData helper=new UserVehicleData(this);
    TextView EF,textView_SF;
    String avaFuel,calDist,em,veh;
    Double requiredFuel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_not_drive);
        EF=(TextView)findViewById(R.id.textView_extraFuel);
        mp=MediaPlayer.create(this,R.raw.beep);

        Intent in=getIntent();
        requiredFuel=in.getDoubleExtra("calFuel", 0);
        em=in.getStringExtra("paM");
        veh=in.getStringExtra("vnm");


        textView_SF=(TextView)findViewById(R.id.textView_setRF);
        textView_SF.setText(String.valueOf(Math.round(requiredFuel)));
        Contact c=new Contact();

        Cursor data1=helper.getDisplay1();
        while(data1.moveToNext())
        {
            String vname=data1.getString(1);
            if(vname.equals(veh) && data1.getString(2).equals(em))
            {
                avaFuel=data1.getString(0);
                break;
            }
        }
        double ExtraFuel=requiredFuel-Double.parseDouble(avaFuel);   //display extr fuel rquire to journey
        EF.setText(String.valueOf(Math.round(ExtraFuel)));


    }

    void fillExtraFuel(View v)    //call AddFuel activity
    {
        mp.start();
        Intent in2 = new Intent(this, AvailFuel.class);
        in2.putExtra("VEH", veh);
        in2.putExtra("mail",em);
        in2.setClass(this, AvailFuel.class);
        startActivity(in2);
    }
}
