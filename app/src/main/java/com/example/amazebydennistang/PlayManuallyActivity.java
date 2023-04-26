package com.example.amazebydennistang;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PlayManuallyActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Button ShortCut_Button;
    Button Zoom_In_Button;
    Button Zoom_Out_Button;
    int path_length = 300;
    int shortest_path = 150;
    String EXTRA_DRIVER = "com.example.mazewidgetpractice.EXTRA_DRIVER";
    String EXTRA_PATH_LENGTH = "com.example.mazewidgetpractice.EXTRA_PATH_LENGTH";
    String EXTRA_SHORTEST_PATH = "com.example.mazewidgetpractice.SHORTEST_PATH" ;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmanuallyactivity);

        //Passed driver and robotQuality from Generating, used in P7
        intent = getIntent();
        String driver = intent.getStringExtra(EXTRA_DRIVER);

        //FOR BOTTOM HOME BAR
        nav = findViewById(R.id.bottomNavigationView);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){ //Possibly add more options later
                    case R.id.home:
                        Toast.makeText(PlayManuallyActivity.this, "Home", Toast.LENGTH_SHORT).show();
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
                openHome();
            }
        });

        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the settings icon", Toast.LENGTH_SHORT).show();
            }
        });


        //Toggle show walls button
        ToggleButton Show_Walls_Button = findViewById(R.id.Show_Walls_Button);
        Show_Walls_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    Toast.makeText(PlayManuallyActivity.this, "Show Walls: ON", Toast.LENGTH_SHORT).show();
                } else {
                    // Toggle is OFF
                    Toast.makeText(PlayManuallyActivity.this, "Show Walls: OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Toggle show maze button
        ToggleButton Show_Maze_Button = findViewById(R.id.Show_Maze_Button);
        Show_Maze_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    Toast.makeText(PlayManuallyActivity.this, "Show Maze: ON", Toast.LENGTH_SHORT).show();
                } else {
                    // Toggle is OFF
                    Toast.makeText(PlayManuallyActivity.this, "Show Maze: OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Toggle show solution button
        ToggleButton Show_Solution_Button = findViewById(R.id.Show_Solution_Button);
        Show_Solution_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toggle is ON
                    Toast.makeText(PlayManuallyActivity.this, "Show Solution: ON", Toast.LENGTH_SHORT).show();
                } else {
                    // Toggle is OFF
                    Toast.makeText(PlayManuallyActivity.this, "Show Solution: OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Shortcut Button takes user to winning screen
        ShortCut_Button = findViewById(R.id.shortcut);
        ShortCut_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the shortcut button", Toast.LENGTH_SHORT).show();;
                openWinning(path_length, shortest_path);
            }
        });

        //Zoom in and out buttons
        Zoom_In_Button = findViewById(R.id.ZoomIn_Button);
        Zoom_In_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked ZOOM IN", Toast.LENGTH_SHORT).show();;
            }
        });

        Zoom_Out_Button = findViewById(R.id.ZoomOut_Button);
        Zoom_Out_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked ZOOM OUT", Toast.LENGTH_SHORT).show();;
            }
        });

        //Arrows and Jump Buttons
        ImageButton leftArrow = findViewById(R.id.arrowLeft);
        ImageButton rightArrow = findViewById(R.id.arrowRight);
        ImageButton upArrow = findViewById(R.id.arrowUp);
        ImageButton jump = findViewById(R.id.jump);

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the left arrow", Toast.LENGTH_SHORT).show();
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the right arrow", Toast.LENGTH_SHORT).show();
            }
        });
        upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked the up arrow", Toast.LENGTH_SHORT).show();
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayManuallyActivity.this, "You clicked jump", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Creates intent to return to title screen
    public void openHome() {
        Intent homeIntent = new Intent(this, AMazeActivity.class);
        startActivity(homeIntent);
    }

    //Creates intent to go to winning screen with pathlength and shortest path values passed
    public void openWinning(int path_length, int shortest_path) {
        Intent winningIntent = new Intent(this, WinningActivity.class);
        winningIntent.putExtra(EXTRA_PATH_LENGTH, path_length);
        winningIntent.putExtra(EXTRA_SHORTEST_PATH, shortest_path);
        startActivity(winningIntent);
    }
}