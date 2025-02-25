package com.example.amazebydennistang.gui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazebydennistang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Timer;
import java.util.TimerTask;


public class PlayAnimationActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Button Zoom_In_Button;
    Button Zoom_Out_Button;
    Button Go2Winning_Button;
    Button Go2Losing_Button;
    LinearProgressIndicator lpi;
    TextView textRemainingEnergy;
    SeekBar animationSpeedBar;
    TextView animationSpeedTV;
    int EnergyRemaining = 3500;
    int path_length = 300;
    int shortest_path = 100;
    boolean isBackClicked = false;
    boolean isHomeButtonClicked = false;
    boolean isLosingButtonClicked = false;
    boolean isWinningButtonClicked = false;
    private MediaPlayer music;
    MazePanel panel;
    PlayingActivityOrganizer activityOrganizer;
    String noEnergy_message = "Your robot ran out of energy!";
    String crash_message = "Your robot crashed!";
    String EXTRA_DRIVER = "com.example.mazewidgetpractice.EXTRA_DRIVER";
    String EXTRA_ROBOTQUALITY = "com.example.mazewidgetpractice.EXTRA_ROBOT" ;
    String EXTRA_ENERGY_CONSUMPTION = "com.example.mazewidgetpractice.EXTRA_ENERGY_CONSUMPTION" ;
    String EXTRA_PATH_LENGTH = "com.example.mazewidgetpractice.EXTRA_PATH_LENGTH";
    String EXTRA_SHORTEST_PATH = "com.example.mazewidgetpractice.SHORTEST_PATH" ;
    String EXTRA_MESSAGE = "com.example.mazewidgetpractice.SHORTEST_MESSAGE" ;
    private static final String TAG = "PlayAnimationActivity: ";


    //Use Handler because you cannot set the textview inside the timer
    //TextView is related to the UI thread
    //Handler handles the UI related views
    final Handler mHandler = new Handler();

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playanimationactivity);
        playMusic();

        //To test that maze shows up
        panel = findViewById(R.id.mazePanel);
        activityOrganizer = new PlayingActivityOrganizer();
        activityOrganizer.start(panel, false);

        //Passed driver and robotQuality from Generating, used in P7
        intent = getIntent();
        String driver = intent.getStringExtra(EXTRA_DRIVER);
        String robotQuality = intent.getStringExtra(EXTRA_ROBOTQUALITY);
        Log.v(TAG, "Driver: " + driver + ",  Robot Quality: " + robotQuality);



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
                        Toast.makeText(PlayAnimationActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "Home button clicked");
                        isHomeButtonClicked = true;
                        playClickSound();
                        openHome();
                }

                return true;
            }
        });

        //FOR TOOLBAR
        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Dennis...");

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayAnimationActivity.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Back button clicked");
                isBackClicked = true;
                playClickSound();
                openHome();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayAnimationActivity.this, "You clicked the settings icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Settings button clicked");
                playClickSound();
            }
        });

        /*
         * Zoom in and out buttons that respond to clicks with toast message
         */
        Zoom_In_Button = findViewById(R.id.ZoomIn_Button);
        Zoom_In_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayAnimationActivity.this, "You clicked ZOOM IN", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Zoom in button clicked");
                activityOrganizer.userInput(Constants.UserInput.ZOOMIN);
                playClickSound();
            }
        });

        Zoom_Out_Button = findViewById(R.id.ZoomOut_Button);
        Zoom_Out_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayAnimationActivity.this, "You clicked ZOOM OUT", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Zoom out button clicked");
                activityOrganizer.userInput(Constants.UserInput.ZOOMOUT);
                playClickSound();
            }
        });

        /*
         * Toggle pause robot button that respond to clicks with toast message
         */
        ToggleButton Pause_Robot_Button = findViewById(R.id.Pause_Robot_Button);
        Pause_Robot_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    Toast.makeText(PlayAnimationActivity.this, "Robot is paused", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Robot toggled to PAUSED");
                    playClickSound();
                } else {
                    // Toggle is OFF
                    Toast.makeText(PlayAnimationActivity.this, "Robot is un-paused", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Robot toggled to START");
                    playClickSound();
                }
            }
        });

        /*
         * Toggle completed button that shows completed map, maze, walls, and responds to clicks with toast message
         */
        ToggleButton Completed_Button = findViewById(R.id.Completed_Maze_Button);
        Completed_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    Toast.makeText(PlayAnimationActivity.this, "Show Completed Maze: ON", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Completed Maze toggled: ON");
                    activityOrganizer.userInput(Constants.UserInput.SHOWWALLS);
                    activityOrganizer.userInput(Constants.UserInput.SHOWFULLMAZE);
                    activityOrganizer.userInput(Constants.UserInput.SHOWSOLUTION);
                    playClickSound();
                } else {
                    // Toggle is OFF
                    Toast.makeText(PlayAnimationActivity.this, "Show Completed Maze: OFF", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Completed Maze toggled: OFF");
                    activityOrganizer.userInput(Constants.UserInput.SHOWWALLS);
                    activityOrganizer.userInput(Constants.UserInput.SHOWFULLMAZE);
                    activityOrganizer.userInput(Constants.UserInput.SHOWSOLUTION);
                    playClickSound();
                }
            }
        });

        //FOR SEEKBAR
        animationSpeedBar = findViewById(R.id.animation_Speed_Seekbar); //Initializes by IDs
        animationSpeedTV = findViewById(R.id.animationSpeed_TV); //Initializes by IDs

        animationSpeedBar.setMax(30); // 30 is 10 times the max value (3)
        animationSpeedBar.setProgress(0); // 0 is 10 times the lowest value (0)

        animationSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                animationSpeedTV.setVisibility(View.VISIBLE); //Changes visibility of text only while scraping
                //animationSpeedTV.setText(progress + "x"); //Visualizes progress compared to max value

                // convert progress value to decimal value
                float value = progress / 10.0f;
                // do something with the decimal value
                // e.g. display it in a TextView
                animationSpeedTV.setText(value  + "x");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(PlayAnimationActivity.this, "Robot speed changed", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Robot speed changed");
            }
        });

        //Opens Go2Winning and changes the button click true
        Go2Winning_Button = findViewById(R.id.go2Winning);
        Go2Winning_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWinningButtonClicked = true;
                Toast.makeText(PlayAnimationActivity.this, "Go2Winning Button Clicked!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Go2Winning is clicked.");
                playClickSound();
                openWinning();
            }
        });

        //Opens Go2Losing and changes the button click true
        Go2Losing_Button = findViewById(R.id.go2Losing);

        Go2Losing_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLosingButtonClicked = true;
                Toast.makeText(PlayAnimationActivity.this, "Go2Losing Button Clicked!", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Go2Losing button is clicked.");
                playClickSound();
                openLosing();
            }
        });


        //FOR THE PROGRESS BAR

        lpi = findViewById(R.id.LinearProgressBar);
        textRemainingEnergy = findViewById(R.id.RemainingEnergyText);

        lpi.setIndeterminate(false);

        setProgress();

    }
    private void setProgress(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                EnergyRemaining = EnergyRemaining - 50;

                lpi.setProgressCompat(EnergyRemaining,true);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textRemainingEnergy.setText("Remaining Energy: " + EnergyRemaining);
                    }
                });

                //Cancels timer so losing screen doesn't open if user moves activities before running out of energy
                if(isLosingButtonClicked == true || isWinningButtonClicked == true || isBackClicked == true || isHomeButtonClicked == true){
                    timer.cancel();
                }
                //Opens losing screen if energy reaches 0
                if(EnergyRemaining == 0){
                    timer.cancel();
                    openLosing();
                }
            }
        };
        timer.schedule(task, 10, 300);
    }

    //Opens goes back to title screen
    public void openHome() {
        Intent homeIntent= new Intent(this, AMazeActivity.class);
        stopMusic();
        startActivity(homeIntent);
        finish();
    }

    //Goes to winning screen with required parameters for display
    public void openWinning() {
        Intent winningIntent = new Intent(this, WinningActivity.class);
        winningIntent.putExtra(EXTRA_ENERGY_CONSUMPTION, 3500-EnergyRemaining);
        winningIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
        winningIntent.putExtra(EXTRA_PATH_LENGTH, path_length);
        Log.v(TAG, "Energy Consumed: " + (3500-EnergyRemaining) + ",  Shortest Path: " + shortest_path + ", Path Length: " + path_length);
        stopMusic();
        startActivity(winningIntent);
        finish();
    }
    public void openLosing() {
        //Goes to losing screen with required parameters for display
        //Ran out of energy, displays different message
        if(EnergyRemaining == 0){
            Intent losingIntent = new Intent(this, LosingActivity.class);
            losingIntent.putExtra(EXTRA_ENERGY_CONSUMPTION, 3500-EnergyRemaining);
            losingIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
            losingIntent.putExtra(EXTRA_MESSAGE, noEnergy_message);
            Log.v(TAG, "Energy Consumed: " + (3500-EnergyRemaining) + ",  Shortest Path: " + shortest_path + ", Losing Message: " + noEnergy_message);
            stopMusic();
            startActivity(losingIntent);
            finish();
        }

        //Robot Crashed, displays different message
        else{
            Intent losingIntent = new Intent(this, LosingActivity.class);
            losingIntent.putExtra(EXTRA_ENERGY_CONSUMPTION, 3500-EnergyRemaining);
            losingIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
            losingIntent.putExtra(EXTRA_MESSAGE, crash_message);
            Log.v(TAG, "Energy Consumed: " + (3500-EnergyRemaining) + ",  Shortest Path: " + shortest_path + ", Losing Message: " + crash_message);
            stopMusic();
            startActivity(losingIntent);
            finish();
        }

    }
    private void playMusic(){
        music = MediaPlayer.create(getApplicationContext(), R.raw.woodsmusic);
        music.setLooping(true);
        music.start();

    }
    private void stopMusic(){
        music.stop();
    }
    private void playStepSound(){
        MediaPlayer step = MediaPlayer.create(getApplicationContext(), R.raw.step);
        step.start();
    }
    private void playClickSound(){
        MediaPlayer click = MediaPlayer.create(getApplicationContext(), R.raw.clicksound);
        click.start();
    }
}
