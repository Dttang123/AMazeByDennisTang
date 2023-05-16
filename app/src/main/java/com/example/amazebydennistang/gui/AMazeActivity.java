package com.example.amazebydennistang.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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

    private SeekBar seekBar;
    private TextView myTV;
    private Spinner Gen_Spinner;
    private Spinner Room_Spinner;
    private Button Explore_Button;
    private Button Revisit_Button;
    private BottomNavigationView nav;
    private int skill_level;
    private int seed = 10000;
    private String generation_selection;
    private String room_selection;
    private MediaPlayer player;
    //Determines if the maze was revisited
    private String revisited;

    //Used to store previously generated mazes for use again
    SharedPreferences sharedPreferences;
    //The array will have dimensions of 3 by 15,
    //where the first dimension represents the three generating methods available (0 for DFS, 1 for Prim, and 2 for Boruvka's),
    //while the second dimension represents the difficulty levels 0 to 15.
    //The array will also store the key for a seed.
    public static String [][] keys;

    //Names to be passed into Generating
    String EXTRA_SEED = "com.example.mazewidgetpractice.EXTRA_SEED" ;
    String EXTRA_SKILL_LEVEL = "com.example.mazewidgetpractice.EXTRA_SKILL_LEVEL";
    String EXTRA_ROOMS = "com.example.mazewidgetpractice.EXTRA_ROOMS" ;
    String EXTRA_GENERATION = "com.example.mazewidgetpractice.EXTRA_GENERATION" ;
    String EXTRA_REVISIT = "com.example.mazewidgetpractice.EXTRA_REVISIT" ;
    private static final String TAG = "AMazeActivity: ";


    /**
     * This method initializes the "keys" array, which is a 2-dimensional
     * String array with dimensions [3][15].
     */
    private void initKeys(){
        keys = new String[3][15];
        int count = 0;
        for (int i = 0; i < 3; i ++)
        {
            for (int j = 0; j < 15; j++)
            {
                keys[i][j] = "String" + (count);
                count++;
            }
        }
    }

    /**
     * This method searches through the "keys" String array to find the
     * key that corresponds to the inputted parameters.
     */
    public static String findSeedKey(String generation_selection, int skill_level)
    {
        int generatorNum = generatorStringToInt(generation_selection);
        return keys[generatorNum][skill_level];
    }

    /**
     * This method converts the string representation of a generator into
     * the corresponding integer value, which can be used to find its
     * corresponding key in the "keys" String array.
     */
    public static int generatorStringToInt(String generation_selection){
        int gen = -1;

        switch (generation_selection)
        {
            case "DFS":
                gen = 0;
                break;
            case "Prim":
                gen = 1;
                break;
            case "Boruvka's":
                gen = 2;
                break;
        }
        return gen;
    }

    public AMazeActivity()
    {
        generation_selection = "DFS";
        room_selection = "Yes";
        skill_level = 0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mazeactivity);
        sharedPreferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        startOwl();
        initKeys();

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
                        playClickSound();
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
                playClickSound();
                Toast.makeText(AMazeActivity.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Back button is clicked.");
                openHome();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                Toast.makeText(AMazeActivity.this, "You clicked settings icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Settings button is clicked.");

            }
        });

        //Explore Button, takes
        Explore_Button = findViewById(R.id.Explore_Button);
        Explore_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Toast.makeText(AMazeActivity.this, "You clicked EXPLORE", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Explore button is clicked.");
                playClickSound();
                //openGenerating(seed);
                switchStatesExplore();
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
                playClickSound();
                //openGenerating(seed);
                switchStatesRevisit();
            }
        });


        //FOR SEEKBAR
        seekBar = findViewById(R.id.seekBar); //Initializes uses IDs
        myTV = findViewById(R.id.myTV); //Initializes uses IDs


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                myTV.setVisibility(View.VISIBLE); //Changes visibility of text only while scraping
                myTV.setText(progress + "/15"); //Visualizes progress compared to max value
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
                playClickSound();
                Toast.makeText(AMazeActivity.this, "Selected Generator: " + generation_selection, Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Selected Generator: " + generation_selection );


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> generation_arrayList = new ArrayList<>();
        generation_arrayList.add("DFS");
        generation_arrayList.add("Prim");
        generation_arrayList.add("Boruvka's");
        ArrayAdapter<String> generation_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generation_arrayList);
        generation_adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        Gen_Spinner.setAdapter(generation_adapter);

        //ROOM SPINNER
        Room_Spinner = findViewById(R.id.Rooms_Spinner);
        Room_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                room_selection = adapterView.getItemAtPosition(position).toString();
                playClickSound();
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
    public void openGenerating(String seed) {
        Intent generatingIntent = new Intent(this, GeneratingActivity.class);
        generatingIntent.putExtra(EXTRA_SEED, seed);
        generatingIntent.putExtra(EXTRA_SKILL_LEVEL, skill_level);
        generatingIntent.putExtra(EXTRA_GENERATION, generation_selection);
        generatingIntent.putExtra(EXTRA_ROOMS, room_selection);
        generatingIntent.putExtra(EXTRA_REVISIT, revisited);
        Log.v(TAG, "Seed: " + seed + ",  Skill Level: " + skill_level + ",  Generator: " + generation_selection + ",  Rooms: " + room_selection);
        startActivity(generatingIntent);
    }

    /**
     * This method is executed upon the user's selection of the Explore button.
     * It transmits the user's preferred difficulty level, maze generator, and maze solver to the StateGenerating class.
     */
    public void switchStatesExplore() {
        revisited = "false";
        Random random = new Random();
        String seed = String.valueOf(random.nextInt(Integer.MAX_VALUE));
        openGenerating(seed);
    }

    /**
     * This method is invoked when the user taps the Revisit button.
     * It generates the identical maze that the user previously navigated.
     * However, this functionality is only possible if the user has already completed the maze at least once before.
     */
    public void switchStatesRevisit() {
        String seedKey = findSeedKey(generation_selection, skill_level);
        String seed = sharedPreferences.getString(seedKey, null);
        if (seed == null){
            Log.v(TAG, "There is no maze to revisit");
            return;
        }
        revisited = "true";
        openGenerating(seed);
    }


    private void startOwl(){
        player = MediaPlayer.create(getApplicationContext(), R.raw.owl);
        player.start();
    }

    private void playClickSound(){
        MediaPlayer click = MediaPlayer.create(getApplicationContext(), R.raw.clicksound);
        click.start();
    }

}
