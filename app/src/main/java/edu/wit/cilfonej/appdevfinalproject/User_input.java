package edu.wit.cilfonej.appdevfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class User_input extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

         Button mybutton = (Button)findViewById(R.id.button_done);
        EditText name = (EditText)findViewById(R.id.edit_name);
        EditText phone = (EditText)findViewById(R.id.edit_phone);
        EditText address = (EditText)findViewById(R.id.edit_address);
        EditText email = (EditText)findViewById(R.id.edit_email);


        mybutton.setOnClickListener(v -> {

            String strname = name.getText().toString();
            String strphone = phone.getText().toString();
            String straddress = address.getText().toString();
            String stremail = email.getText().toString();

            if (TextUtils.isEmpty(strname) || TextUtils.isEmpty(strphone) || TextUtils.isEmpty(straddress) || TextUtils.isEmpty(stremail)){

                Toast.makeText(this, "You Have an Empty Field", Toast.LENGTH_SHORT).show();

            }

            else {



            }


        });


    }
}
