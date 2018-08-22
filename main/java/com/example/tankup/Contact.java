package com.example.tankup;

/**
 * Created by HP on 11-10-2017.
 */

public class Contact
{

    String name;
    String email;
    String pass;

    String vehicle_name;
    int number;
    double mileage;

    String fuel,F_email, F_vname;

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getF_email() {
        return F_email;
    }

    public void setF_email(String f_email) {
        F_email = f_email;
    }

    public String getF_vname() {
        return F_vname;
    }

    public void setF_vname(String f_vname) {
        F_vname = f_vname;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
