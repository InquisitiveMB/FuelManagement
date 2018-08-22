package com.example.tankup;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class VehicleAdd extends AppCompatActivity {

    public MediaPlayer mp;
    UserVehicleData helper=new UserVehicleData(this);
    ListView lv;
    Button btu;
    EditText vehicle,vehicle_No,vehicle_mileage;
    String email,title_name;

    AdapterView.OnItemClickListener onItemClickListener;

    Contact c = new Contact();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_add);
        mp= MediaPlayer.create(this,R.raw.beep);

        lv = (ListView)findViewById(R.id.list);
        btu = (Button)findViewById(R.id.Add_butt);
        vehicle = (EditText)findViewById(R.id.editText_vehicle);
        vehicle_No = (EditText)findViewById(R.id.editText_number);
        vehicle_mileage = (EditText)findViewById(R.id.editText2);

        vehicle.requestFocus();
        Intent in=getIntent();
        email=in.getStringExtra("mail");

        Cursor dataU=helper.getDisplayU();  //get users table row
        while(dataU.moveToNext())
        {
            String checkEm=dataU.getString(0);
            if(checkEm.equals(email))
            {
                title_name=dataU.getString(1);
                break;
            }
        }
        setTitle(title_name+"'s Vehicles");

        final ArrayList<String> arList=new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arList);
        lv.setAdapter(listAdapter);
        Cursor data=helper.getDisplay();     //get vehicles table data
        if(data.getCount()==0)
            Toast.makeText(VehicleAdd.this, "No Vehicle Added!! Add vehicle!!", Toast.LENGTH_SHORT).show();
        else
        {
            while(data.moveToNext())
            {
                String checkEmail=data.getString(3);
                if(checkEmail.equals(email))
                {
                    arList.add(data.getString(0));     //dd vehicle to ArrayList
                }
            }
        }
        btu.setOnClickListener(new View.OnClickListener()    //to add new vehicle
        {
            @Override
            public void onClick(View v) {

                mp.start();
                String new_vehicle_name=vehicle.getText().toString();
                String number=vehicle_No.getText().toString();
                String mileage=vehicle_mileage.getText().toString();


                //adapter.notifyDataSetChanged();
                vehicle.setText("");
                vehicle_No.setText("");
                vehicle_mileage.setText("");
                vehicle.requestFocus();
                //set variables
                c.setVehicle_name(new_vehicle_name);
                c.setNumber(Integer.parseInt(number));
                c.setMileage(Double.parseDouble(mileage));

                boolean created=helper.insertVehicle(c, email);    //add vehicle to database
                if(created==false)
                    Toast.makeText(VehicleAdd.this, "Error...Vehicle Name should not be REPEATED", Toast.LENGTH_LONG).show();
                else
                {
                    arList.add(new_vehicle_name);
                    Toast.makeText(VehicleAdd.this, "New Vehicle Added", Toast.LENGTH_LONG).show();
                }
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()    //click on one of the list field
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                String s=(String)parent.getItemAtPosition(position);
                Intent in2 = new Intent(view.getContext(), AvailFuel.class);
                in2.putExtra("VEH", s);
                in2.putExtra("mail",email);
                in2.setClass(view.getContext(), AvailFuel.class);    //go to AvailFuel Activity
                startActivity(in2);
            }
        });
    }
}