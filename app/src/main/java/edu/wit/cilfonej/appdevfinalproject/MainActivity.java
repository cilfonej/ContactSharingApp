package edu.wit.cilfonej.appdevfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab_main, fab1_card;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textView_card;
    private RecyclerView recList;
    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_main = findViewById(R.id.fab);
        fab1_card = findViewById(R.id.fab2);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_anticlock);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_clockwise);

        textView_card = (TextView) findViewById(R.id.textview_addcard);

        recList = (RecyclerView) findViewById(R.id.cardList);

        recList.setHasFixedSize(true);

        LinearLayoutManager wow = new LinearLayoutManager(this);

        wow.setOrientation(LinearLayoutManager.VERTICAL);

        recList.setLayoutManager(wow);

        ContactAdapter ca = new ContactAdapter(createList(30));
        recList.setAdapter(ca);


    fab_main.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isOpen){

                textView_card.setVisibility(View.INVISIBLE);
                fab1_card.startAnimation(fab_close);
                fab_main.startAnimation(fab_anticlock);
                fab1_card.setClickable(false);
                isOpen = false;
            }

            else {

                textView_card.setVisibility(View.VISIBLE);
                fab1_card.startAnimation(fab_open);
                fab_main.startAnimation(fab_clock);
                fab1_card.setClickable(true);
                isOpen = true;

            }


        }
    });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private List<ContactInfo> createList(int size) {

        List<ContactInfo> result = new ArrayList<ContactInfo>();
        for (int i=1; i <= size; i++) {
            ContactInfo ci = new ContactInfo();
            ci.name = ContactInfo.NAME_PREFIX + i;
            ci.surname = ContactInfo.SURNAME_PREFIX + i;
            ci.email = ContactInfo.EMAIL_PREFIX + i + "@test.com";

            result.add(ci);

        }

        return result;
    }


}
