package com.king.myandroidacceleratorappthree;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {
    EditText mEdtName, mEdtPhone, mEdtMail;
    Button mBtnReg, mBtnView, mBtnDel;

    //To work with the sqlite database, Instantiate the database
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEdtName = findViewById(R.id.edt_name);
        mEdtPhone = findViewById(R.id.edt_phone);
        mEdtMail = findViewById(R.id.edt_mail);

        mBtnReg = findViewById(R.id.btn_reg);
        mBtnView = findViewById(R.id.btn_view);
        mBtnDel = findViewById(R.id.btn_del);

        //Create a database
        db = openOrCreateDatabase("accelerator_db",MODE_PRIVATE,null);

        //Creating a table
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, simu VARCHAR, arafa VARCHAR)");

        //Set the onclick listeners
        mBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implement the saving here
                //Receive the data from the user
                String name = mEdtName.getText().toString();
                String phone = mEdtPhone.getText().toString();
                String email = mEdtMail.getText().toString();

                //Validate the data
                if (name.isEmpty()){
                    mEdtName.setError("Please enter name");
                }else if (phone.isEmpty()){
                    mEdtPhone.setError("Please enter phone");
                }else if (email.isEmpty()){
                    mEdtMail.setError("Please enter email");
                }else {
                    //Save the data and display a success message
                    db.execSQL("INSERT INTO users VALUES('"+name+"','"+phone+"','"+email+"')");
                    message("SUCCESS!!!","User registered successfully");
                }
            }
        });

        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start fetching data from the db and display on the message alert
                Cursor cursor = db.rawQuery("SELECT * FROM users",null);
                if (cursor.getCount() == 0){
                    message("NO RECORDS","Sorry, we didn't find any records in the db");
                }else {
                    StringBuffer buffer = new StringBuffer();
                    while (cursor.moveToNext()){
                        buffer.append("Name: "+cursor.getString(0)+"\n");
                        buffer.append("Phone: "+cursor.getString(1)+"\n");
                        buffer.append("Email: "+cursor.getString(2)+"\n\n");
                    }
                    message("CURRENT USERS",buffer.toString());
                }
            }
        });

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Receive a phone number and use it to delete a record
                String phone = mEdtPhone.getText().toString();
                if (phone.isEmpty()){
                    mEdtPhone.setError("Please enter phone");
                }else {
                    Cursor cursor = db.rawQuery("SELECT * FROM users WHERE simu='"+phone+"'",null);
                    if (cursor.getCount()==0){
                        message("NO SUCH RECORD!!!","Sorry, we didn't find such record on our database");
                    }else {
                        db.execSQL("DELETE FROM users WHERE simu='"+phone+"'");
                        message("SUCCESS!!!","Record deleted successfully!!!");
                    }
                }
            }
        });

    }

    public void message(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistrationActivity.this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.create().show();
    }
}