package com.example.amazebydennistang.gui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazebydennistang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PlayManuallyActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Button ShortCut_Button;
    Button Zoom_In_Button;
    Button Zoom_Out_Button;
    boolean visible;
    int path_length = 0;
    int shortest_path;
    private MediaPlayer music;
    MazePanel panel;
    PlayingActivityOrganizer activityOrganizer;
    String outOfBounds_message = "You jumped out of bounds!";
    String EXTRA_DRIVER = "com.example.mazewidgetpractice.EXTRA_DRIVER";
    String EXTRA_PATH_LENGTH = "com.example.mazewidgetpractice.EXTRA_PATH_LENGTH";
    String EXTRA_SHORTEST_PATH = "com.example.mazewidgetpractice.SHORTEST_PATH" ;
    String EXTRA_MESSAGE = "com.example.mazewidgetpractice.SHORTEST_MESSAGE" ;
    private static final String TAG = "PlayManuallyActivity: ";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmanuallyactivity);
        playMusic();

        //Shows custom view and initializes buttons
        panel = findViewById(R.id.mazePanel);
        activityOrganizer = new PlayingActivityOrganizer();
        activityOrganizer.start(panel, true);

        //For connects manual activity to activity organizer
        activityOrganizer.setManualActivity(this);

        //Passed driver and robotQuality from Generating, used in P7
        intent = getIntent();
        String driver = intent.getStringExtra(EXTRA_DRIVER);
        Log.v(TAG, "Driver: " + driver);

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
                        Toast.makeText(PlayManuallyActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "Home button is clicked");
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
                Toast.makeText(PlayManuallyActivity.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Back button clicked");
                playClickSound();
                openHome();
            }
        });

        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the settings icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Settings button clicked");
                playClickSound();
                stopMusic();
            }
        });


        /*
         * Toggles show walls button and sends the appropriate toast message
         */
        ToggleButton Show_Walls_Button = findViewById(R.id.Show_Walls_Button);
        Show_Walls_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    Toast.makeText(PlayManuallyActivity.this, "Show Walls: ON", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Show Walls toggled: ON");
                    activityOrganizer.userInput(Constants.UserInput.SHOWWALLS);
                    playClickSound();
                } else {
                    // Toggle is OFF
                    Toast.makeText(PlayManuallyActivity.this, "Show Walls: OFF", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Show Walls toggled: OFF");
                    activityOrganizer.userInput(Constants.UserInput.SHOWWALLS);
                    playClickSound();
                }
            }
        });

        /*
         * Toggles show maze button and sends the appropriate toast message
         */
        ToggleButton Show_Maze_Button = findViewById(R.id.Show_Maze_Button);
        Show_Maze_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    Toast.makeText(PlayManuallyActivity.this, "Show Maze: ON", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Show Maze toggled: ON");
                    activityOrganizer.userInput(Constants.UserInput.SHOWFULLMAZE);
                    playClickSound();
                } else {
                    // Toggle is OFF
                    Toast.makeText(PlayManuallyActivity.this, "Show Maze: OFF", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Show Maze toggled: OFF");
                    activityOrganizer.userInput(Constants.UserInput.SHOWFULLMAZE);
                    playClickSound();
                }
            }
        });

        /*
         * Toggles show solution button and sends the appropriate toast message
         */
        ToggleButton Show_Solution_Button = findViewById(R.id.Show_Solution_Button);
        Show_Solution_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    Toast.makeText(PlayManuallyActivity.this, "Show Solution: ON", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Show Solution toggled: ON");
                    activityOrganizer.userInput(Constants.UserInput.SHOWSOLUTION);
                    playClickSound();
                } else {
                    // Toggle is OFF
                    Toast.makeText(PlayManuallyActivity.this, "Show Solution: OFF", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "Show Solution toggled: ON");
                    activityOrganizer.userInput(Constants.UserInput.SHOWSOLUTION);
                    playClickSound();
                }
            }
        });

        //Zoom in and out buttons
        Zoom_In_Button = findViewById(R.id.ZoomIn_Button);
        Zoom_In_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked ZOOM IN", Toast.LENGTH_SHORT).show();;
                Log.v(TAG, "Zoom in button is clicked");
                activityOrganizer.userInput(Constants.UserInput.ZOOMIN);
                playClickSound();
            }
        });

        Zoom_Out_Button = findViewById(R.id.ZoomOut_Button);
        Zoom_Out_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked ZOOM OUT", Toast.LENGTH_SHORT).show();;
                Log.v(TAG, "Zoom out button is clicked");
                activityOrganizer.userInput(Constants.UserInput.ZOOMOUT);
                playClickSound();
            }
        });

        /*
         * Arrows and Jump Buttons
         * Counts each button pressed to compute path length
         */

        ImageButton leftArrow = findViewById(R.id.arrowLeft);
        ImageButton rightArrow = findViewById(R.id.arrowRight);
        ImageButton upArrow = findViewById(R.id.arrowUp);
        ImageButton jump = findViewById(R.id.jump);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the left arrow", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Left arrow button is clicked");
                activityOrganizer.userInput(Constants.UserInput.LEFT);
                playStepSound();
                path_length += 1;
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the right arrow", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Right arrow button is clicked");
                activityOrganizer.userInput(Constants.UserInput.RIGHT);
                playStepSound();
                path_length += 1;
            }
        });
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the up arrow", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Up arrow button is clicked");
                activityOrganizer.userInput(Constants.UserInput.UP);
                playStepSound();
                path_length += 1;
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked jump", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Jump button is clicked");
                activityOrganizer.userInput(Constants.UserInput.JUMP);
                playStepSound();
                path_length += 1;
            }
        });

    }

    //Creates intent to return to title screen
    public void openHome() {
        Intent homeIntent = new Intent(this, AMazeActivity.class);
        stopMusic();
        startActivity(homeIntent);
        finish();
    }

    //Creates intent to go to winning screen with pathlength and shortest path values passed
    public void openWinning(int path_length, int shortest_path) {
        Intent winningIntent = new Intent(this, WinningActivity.class);
        winningIntent.putExtra(EXTRA_PATH_LENGTH, path_length);
        winningIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
        Log.v(TAG, "Shortest Path: " + shortest_path + ", Path Length: " + path_length);
        stopMusic();
        startActivity(winningIntent);
        finish();
    }
    public void openLosing(int path_length, int shortest_path) {
        Intent losingIntent = new Intent(this, LosingActivity.class);
        losingIntent.putExtra(EXTRA_PATH_LENGTH, path_length);
        losingIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
        losingIntent.putExtra(EXTRA_MESSAGE, outOfBounds_message);
        stopMusic();
        startActivity(losingIntent);
        finish();
    }

    public int getPathLength(){

        return path_length;
    }

    private void playMusic(){
        music = MediaPlayer.create(getApplicationContext(), R.raw.woodsmusic);
        music.setLooping(true);
        music.start();

    }
    private void stopMusic(){
        music.setLooping(false);
        music.pause();
        music.stop();
        music.release();
    }
    private void playStepSound(){
        MediaPlayer step = MediaPlayer.create(getApplicationContext(), R.raw.step);
        step.start();
    }
    private void playClickSound(){
        MediaPlayer click = MediaPlayer.create(getApplicationContext(), R.raw.clicksound);
        click.start();
    }




    /**
     * method is called when to switch to either StateWinning or StateLosing
     */

}