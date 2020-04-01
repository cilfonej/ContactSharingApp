package edu.wit.cilfonej.appdevfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        SharedPreferences preferences = getSharedPreferences("edu.wit.cilfonej.appdevfinalproject", Context.MODE_PRIVATE);

        Button mybutton = (Button)findViewById(R.id.button_done);
        EditText name = (EditText)findViewById(R.id.edit_name);
        EditText phone = (EditText)findViewById(R.id.edit_phone);
        EditText address = (EditText)findViewById(R.id.edit_address);
        EditText email = (EditText)findViewById(R.id.edit_email);

        if(!preferences.getString("my_firstname", "").isEmpty()) {
            name.setText(preferences.getString("my_firstname", "") + " " + preferences.getString("my_lastname", ""));
            phone.setText(preferences.getString("my_phone", ""));
            address.setText(preferences.getString("my_address", ""));
            email.setText(preferences.getString("my_email", ""));
        }

        mybutton.setOnClickListener(v -> {

            String strname = name.getText().toString();
            String strphone = phone.getText().toString();
            String straddress = address.getText().toString();
            String stremail = email.getText().toString();

            if (TextUtils.isEmpty(strname) || TextUtils.isEmpty(strphone) || TextUtils.isEmpty(straddress) || TextUtils.isEmpty(stremail)) {

                Toast.makeText(this, "You Have an Empty Field", Toast.LENGTH_SHORT).show();

            }

            else {
                if(!strname.matches("\\w+ \\w+")) {
                    Toast.makeText(this, "Name is invalid! Use: Fistname Lastname", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] name_parts = strname.split(" ");

                preferences.edit()
                        .putString("my_firstname", name_parts[0])
                        .putString("my_lastname", name_parts[1])
                        .putString("my_email", stremail)
                        .putString("my_address", straddress)
                        .putString("my_phone", strphone)
                    .commit();

                Intent intent = new Intent(User_input.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
