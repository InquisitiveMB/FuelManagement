package com.example.tankup;

/**
 * Created by HP on 11-10-2017.
 */

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    public class UserVehicleData extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="DatabaseOne";

    private static final String TABLE_UNAME="users";
    private static final String TABLE_VNAME="vehicles";
    private static final String TABLE_FNAME_E="fuelse";
    //users
    private static final String COLUMN_EMAIL="email";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_PASS="pass";

    //vehicles
    private static final String COLUMN_VEHICLE_NAME="vname";
    private static final String COLUMN_NUMBER="number";
    private static final String COLUMN_MILEAGE="mileage";
    private static final String COLUMN_VEMAIL="email";

    //fuelse
    private static final String COLUMN_FUELE="availFuelE";
    private static final String COLUMN_VEHICLE_NAMEF_E="vnamefE";
    private static final String COLUMN_E_EMAIL="emailF";



    SQLiteDatabase db;

    private static final String TABLE_CREATE ="create table if not exists "+TABLE_UNAME+"("+COLUMN_EMAIL+" text primary key not null,"+COLUMN_NAME+" text not null,"+COLUMN_PASS+" text not null);";

    private static final String TABLE_CREATE_V ="create table if not exists "+TABLE_VNAME+"("+COLUMN_VEHICLE_NAME+" text not null,"+COLUMN_NUMBER+" int not null,"+COLUMN_MILEAGE+" double(7,4) not null,"+COLUMN_VEMAIL+" text not null,primary key("+COLUMN_VEHICLE_NAME+","+COLUMN_VEMAIL+"));";

    private static final String TABLE_CREATE_F ="create table if not exists "+TABLE_FNAME_E+"("+COLUMN_FUELE+" text not null,"+COLUMN_VEHICLE_NAMEF_E+" text not null,"+COLUMN_E_EMAIL+" text not null);";

    public UserVehicleData(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE_V);
        db.execSQL(TABLE_CREATE_F);
        this.db=db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query="DROP TABLE IF EXISTS"+TABLE_UNAME;
        String query1="DROP TABLE IF EXISTS"+TABLE_VNAME;
        String query2="DROP TABLE IF EXISTS"+TABLE_FNAME_E;

        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
        this.onCreate(db);
    }

    public boolean insertUsers(Contact c)
    {
        db = this.getWritableDatabase();
        ContentValues values= new ContentValues();

        values.put(COLUMN_NAME, c.getName());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_PASS, c.getPass());

        long num=db.insert(TABLE_UNAME, null, values);
        if(num==-1)
        {
            db.close();
            return false;
        }
        else
        {
            db.close();
            return true;
        }
    }
    public boolean insertVehicle(Contact c, String email)
    {
        db = this.getWritableDatabase();
        ContentValues values1= new ContentValues();

        values1.put(COLUMN_VEHICLE_NAME, c.getVehicle_name());
        values1.put(COLUMN_MILEAGE, c.getMileage());
        values1.put(COLUMN_NUMBER, c.getNumber());
        values1.put(COLUMN_VEMAIL, email);

        long num=db.insert(TABLE_VNAME, null, values1);
        if(num==-1)
        {
            db.close();
            return  false;
        }
        else
        {
            db.close();
            return true;
        }
    }

    public boolean addFuel_E(Contact c)
    {
        db = this.getWritableDatabase();

            ContentValues val= new ContentValues();
            val.put(COLUMN_FUELE, c.getFuel());

            String where = "vnamefE = ? and emailF = ?";
            String[] whereArgs={c.getF_vname(), c.getF_email()};
            int ch=db.update(TABLE_FNAME_E, val, where, whereArgs);
            if(ch==-1)
                return false;
            else
                return true;
        }


        public boolean addFuel_EN(Contact c)
        {
            db = this.getWritableDatabase();
            ContentValues valN= new ContentValues();

            valN.put(COLUMN_FUELE, c.getFuel());
            valN.put(COLUMN_VEHICLE_NAMEF_E, c.getF_vname());
            valN.put(COLUMN_E_EMAIL, c.getF_email());

            long num=db.insert(TABLE_FNAME_E, null, valN);
            if(num==-1)
            {
                db.close();
                return  false;
            }
            else
            {
                db.close();
                return true;
            }
        }


    public String searchPass(String uname)
    {
        db = getReadableDatabase();
        String query= "select email, pass from "+ TABLE_UNAME;
        Cursor cursor= db.rawQuery(query, null);
        String a,b=null;
        if(cursor.moveToFirst())
        {
            do {
                a=cursor.getString(0);
                if(a.equals(uname))
                {
                    b=cursor.getString(1);
                    break;
                }
            }while (cursor.moveToNext());
        }
        return b;
    }

    public Cursor getDisplayU()
    {
        db=getWritableDatabase();
        Cursor data= db.rawQuery("select * from users", null);
        return data;
    }
    public Cursor getDisplay()
    {
        db=getWritableDatabase();
        Cursor data= db.rawQuery("select * from vehicles", null);
        return data;
    }
    public Cursor getDisplay1()
    {
        db=getWritableDatabase();
        Cursor data= db.rawQuery("select * from fuelse", null);
        return data;
    }
}



