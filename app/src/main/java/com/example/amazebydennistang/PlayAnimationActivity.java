package com.example.amazebydennistang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Timer;
import java.util.TimerTask;


public class PlayAnimationActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Button Go2Winning_Button;
    Button Go2Losing_Button;
    LinearProgressIndicator lpi;
    TextView textRemainingEnergy;
    SeekBar animationSpeedBar;
    TextView animationSpeedTV;
    int EnergyRemaining = 3500;
    int path_length = 300;
    int shortest_path = 100;
    boolean isLosingButtonClicked = false;
    boolean isWinningButtonClicked = false;
    String noEnergy_message = "Your robot ran out of energy!";
    String crash_message = "Your robot crashed!";
    String EXTRA_ENERGY_CONSUMPTION = "com.example.mazewidgetpractice.EXTRA_ENERGY_CONSUMPTION" ;
    String EXTRA_PATH_LENGTH = "com.example.mazewidgetpractice.EXTRA_PATH_LENGTH";
    String EXTRA_SHORTEST_PATH = "com.example.mazewidgetpractice.SHORTEST_PATH" ;
    String EXTRA_MESSAGE = "com.example.mazewidgetpractice.SHORTEST_MESSAGE" ;

    final Handler mHandler = new Handler(); //Use Handler because you cannot set the textview inside the timer
    //TextView is related to the UI thread
    //Handler handles the UI related views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playanimationactivity);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //FOR BOTTOM HOME BAR
        nav = findViewById(R.id.bottomNavigationView);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){ //Possibly add more options later
                    case R.id.home:
                        Toast.makeText(PlayAnimationActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        openHome();
                }


                return true;
            }
        });

        //FOR TOOLBAR
        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayAnimationActivity.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                openHome();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayAnimationActivity.this, "You clicked the settings icon", Toast.LENGTH_SHORT).show();
            }
        });
        title.setText("Dennis...");


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

            }
        });

        //Opens Go2Winning and changes the button click true
        Go2Winning_Button = findViewById(R.id.go2Winning);
        Go2Winning_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWinningButtonClicked = true;
                openWinning();
            }
        });

        //Opens Go2Losing and changes the button click true
        Go2Losing_Button = findViewById(R.id.go2Losing);

        Go2Losing_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLosingButtonClicked = true;
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
                if(isLosingButtonClicked == true || isWinningButtonClicked == true){
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
        startActivity(homeIntent);
    }

    //Goes to winning screen with required parameters for display
    public void openWinning() {

        Intent winningIntent = new Intent(this, WinningActivity.class);
        winningIntent.putExtra(EXTRA_ENERGY_CONSUMPTION, 3500-EnergyRemaining);
        winningIntent.putExtra(EXTRA_PATH_LENGTH, path_length);
        winningIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
        startActivity(winningIntent);
    }
    public void openLosing() {
        //Goes to losing screen with required parameters for display
        //Ran out of energy, displays different message
        if(EnergyRemaining == 0){
            Intent losingIntent = new Intent(this, LosingActivity.class);
            losingIntent.putExtra(EXTRA_ENERGY_CONSUMPTION, 3500-EnergyRemaining);
            losingIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
            losingIntent.putExtra(EXTRA_MESSAGE, noEnergy_message);
            startActivity(losingIntent);
        }

        //Robot Crashed, displays different message
        else{
            Intent losingIntent = new Intent(this, LosingActivity.class);
            losingIntent.putExtra(EXTRA_ENERGY_CONSUMPTION, 3500-EnergyRemaining);
            losingIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
            losingIntent.putExtra(EXTRA_MESSAGE, crash_message);
            startActivity(losingIntent);
        }

    }
}
