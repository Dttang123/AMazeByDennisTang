package com.example.amazebydennistang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Generating extends AppCompatActivity {

    LinearProgressIndicator lpi;
    CircularProgressIndicator cpi;
    TextView textGrowing;
    Spinner driverSpinner;
    Spinner robotQualitySpinner;
    BottomNavigationView nav;
    int progress;
    String driverSelection = "Select";
    String qualitySelection = "Premium";
    String EXTRA_RANDOM_NUMBER = "com.example.mazewidgetpractice.EXTRA_RANDOM_NUMBER" ;
    String EXTRA_SKILL_LEVEL = "com.example.mazewidgetpractice.EXTRA_SKILL_LEVEL";
    String EXTRA_ROOMS = "com.example.mazewidgetpractice.EXTRA_ROOMS" ;
    String EXTRA_GENERATION = "com.example.mazewidgetpractice.EXTRA_GENERATION" ;
    String EXTRA_DRIVER = "com.example.mazewidgetpractice.EXTRA_DRIVER";
    String EXTRA_ROBOTQUALITY = "com.example.mazewidgetpractice.EXTRA_ROBOT";

    //Use Handler because you cannot set the textview inside the timer
    //TextView is related to the UI thread
    //Handler handles the UI related views
    final Handler mHandler = new Handler();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generating);

        //Retrieves passed parameters from AMazeActivity into Generating
        intent = getIntent();
        int seed = intent.getIntExtra(EXTRA_RANDOM_NUMBER,0);
        int skill_level = intent.getIntExtra(EXTRA_SKILL_LEVEL,0);
        String rooms = intent.getStringExtra(EXTRA_ROOMS);
        String generation = intent.getStringExtra(EXTRA_GENERATION);


        //FOR BOTTOM HOME BAR
        nav = findViewById(R.id.bottomNavigationView);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) { //Possibly add more options later
                    case R.id.home:
                        Toast.makeText(Generating.this, "Home", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Generating.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                openHome();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Generating.this, "You clicked the settings icon", Toast.LENGTH_SHORT).show();
            }
        });


        //FOR THE PROGRESS BAR
        lpi = findViewById(R.id.LinearProgressBar);
        cpi = findViewById(R.id.circularProgressBar);
        textGrowing = findViewById(R.id.GrowingText);

        lpi.setIndeterminate(false);
        cpi.setIndeterminate(false);

        setProgress();
    }

    private void setProgress() {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                progress = progress + 1;

                lpi.setProgressCompat(progress, true);
                cpi.setProgressCompat(progress, true);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textGrowing.setText("Growing Trees... " + progress + "%");
                    }
                });

                if (progress == 100) {
                    timer.cancel();

                    if(driverSelection.equals("Manual")){
                        openPlayManuallyActivity();
                    }

                    else if(driverSelection.equals("Wizard") || driverSelection.equals("Wall-Follower")){
                        openPlayAnimationActivity();
                    }

                    else if(driverSelection.equals("Select")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(Generating.this, "Generation is complete. Choose a driver to begin the maze.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    //Uses the "runOnUiThread()" method, to run a Runnable on the main UI thread and display the toast message.

                }
            }
        };

        timer.schedule(task, 10, 100);


        //FOR DRIVER SPINNER
        driverSpinner = findViewById(R.id.Driver_Spinner);
        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //on selecting a spinner item, set driverSelection to that item
                driverSelection = adapterView.getItemAtPosition(position).toString();

                if (adapterView.getItemAtPosition(position).equals("Select"))
                {
                    Toast.makeText(Generating.this, "Please select a driver", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //show selected spinner item
                    Toast.makeText(Generating.this, "Selected Item: " + driverSelection, Toast.LENGTH_SHORT).show();

                    //Initially tells user to choose a driver.
                    if(driverSelection.equals("Select")) //(adapterView.getItemAtPosition(position).equals("Manual"))
                    {
                        driverSelection = "Select";
                        Toast.makeText(Generating.this, "Choose a driver.", Toast.LENGTH_SHORT).show();
                    }

                    //Moves to PlayManuallyActive if Manual is selected and progress is at 100%
                    if(driverSelection.equals("Manual")) //(adapterView.getItemAtPosition(position).equals("Manual"))
                    {
                        //Passes Driver information to the next activity if at 100%
                        if(progress == 100){
                            openPlayManuallyActivity();
                        }
                        else{
                        Toast.makeText(Generating.this, "The maze will being shortly.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Moves to PlayAnimationActivity if Wizard is selected and progress is at 100%
                    else if(driverSelection.equals("Wizard"))
                    {
                        //Passes Driver and Quality information to the next activity if at 100%
                        if(progress == 100){
                            openPlayAnimationActivity();
                        }
                        else{
                            Toast.makeText(Generating.this, "The maze will being shortly.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Moves to PlayAnimationActivity if Wall-Follower is selected and progress is at 100%
                    else if(driverSelection.equals("Wall-Follower"))
                    {
                        //Passes Driver and Quality information to the next activity if at 100%
                        if(progress == 100){
                            openPlayAnimationActivity();
                        }
                        else{
                            Toast.makeText(Generating.this, "The maze will being shortly.", Toast.LENGTH_SHORT).show();
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

        //Displays the chosen Quality and sets EXTRA_ROBOTQUALITY for passing to next activity
        robotQualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                qualitySelection = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(Generating.this, "Selected Item: " + qualitySelection, Toast.LENGTH_SHORT).show();
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


    }

    public void openHome() {
        Intent homeIntent = new Intent(this, AMazeActivity.class);
        startActivity(homeIntent);
    }

    public void openPlayAnimationActivity() {

        Intent animationIntent = new Intent(Generating.this, PlayAnimationActivity.class);
        animationIntent.putExtra(EXTRA_DRIVER, driverSelection);
        animationIntent.putExtra(EXTRA_ROBOTQUALITY, qualitySelection);
        startActivity(animationIntent);
    }

    public void openPlayManuallyActivity() {
        Intent manualIntent = new Intent(Generating.this, PlayManuallyActivity.class);
        manualIntent.putExtra(EXTRA_DRIVER, driverSelection);
        startActivity(manualIntent);
    }
}