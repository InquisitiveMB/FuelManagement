package com.example.tankup;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AvailFuel extends AppCompatActivity {

    public MediaPlayer mp;
    Contact c=new Contact();
    TextView FuelAL,DBNo,DBMil;
    EditText FTD;
    String mg=null;
    UserVehicleData helper=new UserVehicleData(this);
    Cursor data;

    Button bt;
    String veh1,em;
    int chnge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail_fuel);
        mp= MediaPlayer.create(this,R.raw.beep);
        FuelAL=(TextView)findViewById(R.id.textView_FuelAva);
        DBMil=(TextView)findViewById(R.id.textView_DBMileage);
        DBNo=(TextView)findViewById(R.id.textView_DBNumber);
        FTD=(EditText)findViewById(R.id.editText_fromUserFuel);
        bt=(Button)findViewById(R.id.button_GoToMap);
        bt.setTextColor(Color.BLUE);
        Intent in=getIntent();
        veh1=in.getStringExtra("VEH");
        em=in.getStringExtra("mail");
        setTitle(veh1);

        Cursor DBNM=helper.getDisplay();   //get vehicles table data
        while(DBNM.moveToNext())
        {
            String VN=DBNM.getString(0);
            if(VN.equals(veh1) && DBNM.getString(3).equals(em))
            {
                DBNo.setText(String.valueOf(DBNM.getInt(1)));
                DBMil.setText(String.valueOf(DBNM.getDouble(2)));
                break;
            }
        }

        data=helper.getDisplay1();  //get fuels table data

        if(data.getCount()==0)
            Toast.makeText(AvailFuel.this, "Database empty", Toast.LENGTH_SHORT).show();
        else
        {
            while(data.moveToNext())
            {
                String vname=data.getString(1);
                if(vname.equals(veh1) && data.getString(2).equals(em))
                {
                    chnge=1;
                    mg=data.getString(0);                 //mg=get fuel in vehicle which is stored in database
                    break;
                }
            }
        }

        if(mg!=null)
        {
            double form=Double.parseDouble(mg);
            FuelAL.setText(String.valueOf(Math.round(form))+" L");    //set text to TextView of Fuel available
        }
        else
            FuelAL.setText(mg+" L");

    }
    void AddFuelToDB(View v)    //Add fuel in the vehicle
    {
        mp.start();

        if(mg==null)     //user not added fuel in the vehicle before
        {
            String FL1=FTD.getText().toString();
            boolean ch=true;
            c.setF_vname(veh1);
            c.setFuel(FL1);
            c.setF_email(em);
            ch=helper.addFuel_EN(c);   //add fuel in database of particular vehicle
            if(ch==true)
            {
                Toast.makeText(this, "Fuel Added", Toast.LENGTH_SHORT).show();
                FuelAL.setText(FL1+" L");
                FTD.setText("");
            }

            else
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        else       //update the fuel in vehicle
        {
            String FL=FTD.getText().toString();
            double intFL=Double.parseDouble(FL);

            intFL=intFL+Double.parseDouble(mg);

            boolean ch=true;
            c.setF_vname(veh1);
            c.setFuel(String.valueOf(intFL));
            c.setF_email(em);
            ch=helper.addFuel_E(c);   //update the fuel table

            if(ch==true)
            {
                Toast.makeText(this, "Fuel Added", Toast.LENGTH_SHORT).show();
                FuelAL.setText(String.valueOf(Math.round(intFL))+" L");
                FTD.setText("");
            }

            else
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }


    }

    void StartJourney(View v)    //start travelling
    {
        mp.start();
        Intent in2 = new Intent(this, MapsActivity.class);
        in2.putExtra("VEH", veh1);
        in2.putExtra("mail",em);
        in2.setClass(this, MapsActivity.class);
        startActivity(in2);
    }

}
