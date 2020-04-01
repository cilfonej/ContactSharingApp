package edu.wit.cilfonej.appdevfinalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab_main, fab1_card, fab1_disp;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private TextView textView_card, textView_show;
    private RecyclerView recList;
    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_main = findViewById(R.id.fab);
        fab1_card = findViewById(R.id.fab_scan);
        fab1_disp = findViewById(R.id.fab_disp);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_anticlock);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_clockwise);

        textView_card = (TextView) findViewById(R.id.textview_addcard);
        textView_show = (TextView) findViewById(R.id.textview_showCard);

        recList = (RecyclerView) findViewById(R.id.cardList);

        recList.setHasFixedSize(true);

        LinearLayoutManager wow = new LinearLayoutManager(this);

        wow.setOrientation(LinearLayoutManager.VERTICAL);

        recList.setLayoutManager(wow);

        ContactAdapter ca = new ContactAdapter(Contact.getAllContacts(this));
        recList.setAdapter(ca);

        SharedPreferences preferences = getSharedPreferences("edu.wit.cilfonej.appdevfinalproject", Context.MODE_PRIVATE);
        // if missing personal info, open input-screen
        if(preferences.getString("my_firstname", "").isEmpty()) {
            Intent intent = new Intent(MainActivity.this, User_input.class);
            startActivity(intent);
        }

        fab1_disp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, User_input.class);
                startActivity(intent);
            }
        });

        fab1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerationActivity.class);
                startActivity(intent);
            }
        });

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isOpen){

                    textView_card.setVisibility(View.INVISIBLE);
                    fab1_card.startAnimation(fab_close);
                    fab1_card.setClickable(false);

                    textView_show.setVisibility(View.INVISIBLE);
                    fab1_disp.startAnimation(fab_close);
                    fab1_disp.setClickable(false);

                    fab_main.startAnimation(fab_anticlock);
                    isOpen = false;
                }

                else {

                    textView_card.setVisibility(View.VISIBLE);
                    fab1_card.startAnimation(fab_open);
                    fab1_card.setClickable(true);


                    textView_show.setVisibility(View.VISIBLE);
                    fab1_disp.startAnimation(fab_open);
                    fab1_disp.setClickable(true);

                    fab_main.startAnimation(fab_clock);
                    isOpen = true;

                }


            }
        });

    }

    protected void onResume() {
        super.onResume();

        ContactAdapter ca = new ContactAdapter(Contact.getAllContacts(this));
        recList.setAdapter(ca);
    }
}
