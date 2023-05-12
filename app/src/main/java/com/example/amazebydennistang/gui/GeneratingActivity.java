package com.example.amazebydennistang.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazebydennistang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.amazebydennistang.generation.Maze;
import com.example.amazebydennistang.generation.Order;
import com.example.amazebydennistang.generation.DefaultOrder;
import com.example.amazebydennistang.generation.MazeFactory;
import com.example.amazebydennistang.generation.Factory;


public class GeneratingActivity extends AppCompatActivity implements Order {

    private LinearProgressIndicator lpi;
    private CircularProgressIndicator cpi;
    private TextView textGrowing;
    private Spinner driverSpinner;
    private Spinner robotQualitySpinner;
    private BottomNavigationView nav;
    private int progress;
    private String driverSelection;
    private String qualitySelection;
    private int seed;
    private int skill_level;
    private String generation;
    private String rooms;
    private Thread mazeGenerationThread;

    private String EXTRA_SEED = "com.example.mazewidgetpractice.EXTRA_SEED";
    private String EXTRA_SKILL_LEVEL = "com.example.mazewidgetpractice.EXTRA_SKILL_LEVEL";
    private String EXTRA_ROOMS = "com.example.mazewidgetpractice.EXTRA_ROOMS";
    private String EXTRA_GENERATION = "com.example.mazewidgetpractice.EXTRA_GENERATION";
    private String EXTRA_DRIVER = "com.example.mazewidgetpractice.EXTRA_DRIVER";
    private String EXTRA_ROBOTQUALITY = "com.example.mazewidgetpractice.EXTRA_ROBOT";
    private static final String TAG = "Generating: ";

    final Handler mHandler = new Handler();

    //Maze Variables
    SharedPreferences sharedPreferences;
    private DefaultOrder defaultOrder;
    private MazeFactory mazeFactory;
    private Maze maze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generating);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mazeFactory = new MazeFactory();

        //Retrieves passed parameters from AMazeActivity into Generating, used later in P7
        if (bundle != null) {
            seed = intent.getIntExtra(EXTRA_SEED, 0);
            skill_level = intent.getIntExtra(EXTRA_SKILL_LEVEL, 0);
            rooms = intent.getStringExtra(EXTRA_ROOMS);
            generation = intent.getStringExtra(EXTRA_GENERATION);
            Log.v(TAG, "Seed: " + seed + ",  Skill Level: " + skill_level + ",  Generator: " + generation + ",  Rooms: " + rooms);
        }


        //FOR BOTTOM HOME BAR
        nav = findViewById(R.id.bottomNavigationView);

        /*
         * If home icon is clicked, take the user to the title screen
         */
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) { //Possibly add more options later
                    case R.id.home:
                        Toast.makeText(GeneratingActivity.this, "Home", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GeneratingActivity.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Back button is clicked.");
                openHome();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GeneratingActivity.this, "You clicked the settings icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Settings button is clicked.");
            }
        });

        //FOR DRIVER SPINNER
        driverSpinner = findViewById(R.id.Driver_Spinner);
        driverSelection = "Select";
        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //on selecting a spinner item, set driverSelection to that item
                driverSelection = adapterView.getItemAtPosition(position).toString();

                if (adapterView.getItemAtPosition(position).equals("Select")) {
                    Toast.makeText(GeneratingActivity.this, "Please select a driver", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "User, must select a driver");
                } else {
                    //show selected spinner item
                    Toast.makeText(GeneratingActivity.this, "Selected Item: " + driverSelection, Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "User selected " + driverSelection + " driver.");

                    //Initially tells user to choose a driver.
                    if (driverSelection.equals("Select")) {
                        driverSelection = "Select";
                        Toast.makeText(GeneratingActivity.this, "Choose a driver.", Toast.LENGTH_SHORT).show();
                    }

                    //Moves to PlayManuallyActive if Manual is selected and progress is at 100%
                    if (driverSelection.equals("Manual")) {
                        //Passes Driver information to the next activity if at 100%
                        if (progress == 100) {
                            goToPlaying();
                        } else {
                            Toast.makeText(GeneratingActivity.this, "The maze will being shortly.", Toast.LENGTH_SHORT).show();
                            Log.v(TAG, "User selected " + driverSelection + " driver before maze was done. Wait for loading to finish");
                        }
                    }

                    //Moves to PlayAnimationActivity if Wizard is selected and progress is at 100%
                    else if (driverSelection.equals("Wizard")) {
                        //Passes Driver and Quality information to the next activity if at 100%
                        if (progress == 100) {
                            goToPlaying();
                        } else {
                            Toast.makeText(GeneratingActivity.this, "The maze will being shortly.", Toast.LENGTH_SHORT).show();
                            Log.v(TAG, "User selected " + driverSelection + " driver before maze was done. Wait for loading to finish");
                        }
                    }

                    //Moves to PlayAnimationActivity if Wall-Follower is selected and progress is at 100%
                    else if (driverSelection.equals("Wall-Follower")) {
                        //Passes Driver and Quality information to the next activity if at 100%
                        if (progress == 100) {
                            goToPlaying();
                        } else {
                            Toast.makeText(GeneratingActivity.this, "The maze will being shortly.", Toast.LENGTH_SHORT).show();
                            Log.v(TAG, "User selected " + driverSelection + " driver before maze was done. Wait for loading to finish");
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> driver_arrayList = new ArrayList<>();

        driver_arrayList.add("Select");
        driver_arrayList.add("Manual");
        driver_arrayList.add("Wizard");
        driver_arrayList.add("Wall-Follower");

        ArrayAdapter<String> driver_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, driver_arrayList);
        driver_adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        driverSpinner.setAdapter(driver_adapter);


        //FOR RobotQuality SPINNER

        //Sets the spinner by ID
        robotQualitySpinner = findViewById(R.id.RobotQuality_Spinner);
        qualitySelection = "Premium";

        //Displays the chosen Quality and sets EXTRA_ROBOTQUALITY for passing to next activity
        robotQualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                qualitySelection = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(GeneratingActivity.this, "Selected Item: " + qualitySelection, Toast.LENGTH_SHORT).show();
                Log.v(TAG, "User selected " + qualitySelection + " robot quality.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayList<String> robotQuality_arrayList = new ArrayList<>();

        robotQuality_arrayList.add("Premium");
        robotQuality_arrayList.add("Mediocre");
        robotQuality_arrayList.add("Soso");
        robotQuality_arrayList.add("Shaky");

        ArrayAdapter<String> robotQuality_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, robotQuality_arrayList);
        robotQuality_adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        robotQualitySpinner.setAdapter(robotQuality_adapter);

        start();
    }

    //FOR THE PROGRESS BAR
    //setProgress();

    /**
     * Draws the current state of maze generation progress onto the screen
     */

    private void updateProgressBar(int progress) {
        lpi = findViewById(R.id.LinearProgressBar);
        cpi = findViewById(R.id.circularProgressBar);
        textGrowing = findViewById(R.id.GrowingText);

        lpi.setIndeterminate(false);
        cpi.setIndeterminate(false);

        lpi.setProgress(progress, false);
        cpi.setProgress(progress, false);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                textGrowing.setText("Growing Trees... " + progress + "%");
                if (progress == 100) {

                    //goToPlaying(); in real implementation
                    openPlayManuallyActivity();
                }
                else if (driverSelection.equals("Select")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GeneratingActivity.this, "Generation is complete. Choose a driver to begin the maze.", Toast.LENGTH_LONG).show();
                            Log.v(TAG, "Loading is finished, waiting for user to choose a driver");
                        }
                    });
                }
            }
        });


            //Uses the "runOnUiThread()" method, to run a Runnable on the main UI thread and display the toast message.

    }


    public void openHome() {
        Intent homeIntent = new Intent(this, AMazeActivity.class);
        startActivity(homeIntent);
    }

    public void openPlayAnimationActivity() {

        Intent animationIntent = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
        animationIntent.putExtra(EXTRA_DRIVER, driverSelection);
        animationIntent.putExtra(EXTRA_ROBOTQUALITY, qualitySelection);
        Log.v(TAG, "Driver: " + driverSelection + ",  Robot Quality: " + qualitySelection);
        startActivity(animationIntent);
        finish();
    }

    public void openPlayManuallyActivity() {
        Intent manualIntent = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
        manualIntent.putExtra(EXTRA_DRIVER, driverSelection);
        Log.v(TAG, "Driver: " + driverSelection);
        startActivity(manualIntent);
        finish();
    }


    /**
     * Starts the maze generation
     */
    @Override
    public void updateProgress(int percentage) {
        updateProgressBar(percentage);
    }

    @Override
    public int getSkillLevel() {
        return skill_level;
    }

    @Override
    public Builder getBuilder() {
        return builderStringToBuilder(generation);
    }

    @Override
    public boolean isPerfect() {
        return false;
    }

    @Override
    public int getSeed() {
        return seed;
    }

    @Override
    public void deliver(Maze mazeConfig) {
        if(mazeConfig == null){
            Log.v(TAG, "Maze is null, something is wrong");
        }
        else{
            Log.v(TAG, "Maze is not null, you're fine");
            DataHolder.setMaze(mazeConfig);
        }
    }

    private void start() {
        mazeFactory.order(this);
    }

    /**
     * Converts the string representation to a Builder
     */
    public Builder builderStringToBuilder(String builderString) {
        switch (builderString) {
            case "DFS":
                return Builder.DFS;
            case "Prim":
                return Builder.Prim;
            case "Boruvka":
                return Builder.Boruvka;
        }

        return Builder.DFS;
    }

    private void goToPlaying() {
        Log.i("Final driver: ", driverSelection);
        if (driverSelection.equals("Manual")) {
            Log.i("Passed!", "Passed!");
            openPlayManuallyActivity();
            finish();
        } else {
            Log.i("Failed!", "Failed!");
            openPlayAnimationActivity();
            finish();
        }

    }

}