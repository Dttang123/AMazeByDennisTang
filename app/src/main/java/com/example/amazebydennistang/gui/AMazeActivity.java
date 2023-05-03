package com.example.amazebydennistang.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazebydennistang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Random;

public class AMazeActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView myTV;
    Spinner Gen_Spinner;
    Spinner Room_Spinner;
    Button Explore_Button;
    Button Revisit_Button;
    BottomNavigationView nav;
    int skill_level;
    int seed;
    String generation_selection;
    String room_selection;

    //Names to be passed into Generating
    String EXTRA_SEED = "com.example.mazewidgetpractice.EXTRA_SEED" ;
    String EXTRA_SKILL_LEVEL = "com.example.mazewidgetpractice.EXTRA_SKILL_LEVEL";
    String EXTRA_ROOMS = "com.example.mazewidgetpractice.EXTRA_ROOMS" ;
    String EXTRA_GENERATION = "com.example.mazewidgetpractice.EXTRA_GENERATION" ;
    private static final String TAG = "AMazeActivity: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mazeactivity);

        //FOR BOTTOM HOME BAR
        nav = findViewById(R.id.bottomNavigationView);

        /*
         * If home icon is clicked, take the user to the title screen
         */
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){ //Possibly add more options later
                    case R.id.home:
                        Toast.makeText(AMazeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "Home button is clicked.");
                        openHome();
                }
                return true;
            }
        });

        //FOR TOOLBAR
        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Dennis's Lost Woods");

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AMazeActivity.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Back button is clicked.");
                openHome();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AMazeActivity.this, "You clicked settings icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Settings button is clicked.");
            }
        });


        //Explore Button, takes
        Explore_Button = findViewById(R.id.Explore_Button);
        Explore_Button.setOnClickListener(new View.OnClickListener() {
            Random random = new Random();
            int seed = random.nextInt();

            @Override
            public void onClick(View view)
            {
                Toast.makeText(AMazeActivity.this, "You clicked EXPLORE", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Explore button is clicked.");
                openGenerating();
            }
        });

        //Revisit Button functions the same as Explore button for P6
        Revisit_Button = findViewById(R.id.Revisit_Button);
        Revisit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(AMazeActivity.this, "You clicked REVISIT", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Revisit button is clicked.");
                openGenerating();
            }
        });


        //FOR SEEKBAR
        seekBar = findViewById(R.id.seekBar); //Initializes uses IDs
        myTV = findViewById(R.id.myTV); //Initializes uses IDs


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                myTV.setVisibility(View.VISIBLE); //Changes visibility of text only while scraping
                myTV.setText(progress + "/100"); //Visualizes progress compared to max value
                skill_level = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //Shows set level in toast and log
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(AMazeActivity.this, "Selected Skill Level: " + skill_level, Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Skill level seekbar set to: " + skill_level);
            }
        });

        //FOR SPINNER

        Gen_Spinner = findViewById(R.id.Generation_Spinner);
        Gen_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                generation_selection  = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(AMazeActivity.this, "Selected Generator: " + generation_selection, Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Selected Generator: " + generation_selection );


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> generation_arrayList = new ArrayList<>();
        generation_arrayList.add("DFS");
        generation_arrayList.add("Prim's");
        generation_arrayList.add("Broruvka's");
        ArrayAdapter<String> generation_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generation_arrayList);
        generation_adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        Gen_Spinner.setAdapter(generation_adapter);

        //ROOM SPINNER
        Room_Spinner = findViewById(R.id.Rooms_Spinner);
        Room_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                room_selection = adapterView.getItemAtPosition(position).toString();

                if(adapterView.getItemAtPosition(position).equals("Yes"))
                {
                    Toast.makeText(AMazeActivity.this, "The maze will have rooms", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Maze Rooms: " + room_selection);

                }
                else if(adapterView.getItemAtPosition(position).equals("No"))
                {
                    Toast.makeText(AMazeActivity.this, "The maze will not have rooms", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Maze Rooms: " + room_selection);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> room_arrayList = new ArrayList<>();
        room_arrayList.add("Yes");
        room_arrayList.add("No");
        ArrayAdapter<String> room_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, room_arrayList);
        room_adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        Room_Spinner.setAdapter(room_adapter);

    }

    public void openHome() {
        Intent homeIntent = new Intent(this, AMazeActivity.class);
        startActivity(homeIntent);
    }

    //Opens generation activity with parameters passed in
    public void openGenerating() {
        Intent generatingIntent = new Intent(this, Generating.class);
        generatingIntent.putExtra(EXTRA_SEED, seed);
        generatingIntent.putExtra(EXTRA_SKILL_LEVEL, skill_level);
        generatingIntent.putExtra(EXTRA_GENERATION, generation_selection);
        generatingIntent.putExtra(EXTRA_ROOMS, room_selection);
        Log.v(TAG, "Seed: " + seed + ",  Skill Level: " + skill_level + ",  Generator: " + generation_selection + ",  Rooms: " + room_selection);
        startActivity(generatingIntent);
    }

}