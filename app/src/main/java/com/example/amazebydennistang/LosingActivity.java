package com.example.amazebydennistang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LosingActivity extends AppCompatActivity {

    TextView path_length_text, shortest_path_text, energy_consumed_text, robot_losing_text;
    BottomNavigationView nav;
    Button Play_Again_Button;
    String EXTRA_SHORTEST_PATH = "com.example.mazewidgetpractice.SHORTEST_PATH" ;
    String EXTRA_PATH_LENGTH = "com.example.mazewidgetpractice.EXTRA_PATH_LENGTH";
    String EXTRA_ENERGY_CONSUMPTION = "com.example.mazewidgetpractice.EXTRA_ENERGY_CONSUMPTION" ;
    String EXTRA_MESSAGE = "com.example.mazewidgetpractice.SHORTEST_MESSAGE" ;
    private static final String TAG = "LosingActivity: ";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.losingactivity);


        // Parameters passed from PlayAnimation to LosingActivity
        // Message varies depending on how the robot loses
        // If 0 energy, then depleted energy message
        // If > 0 energy, then crash message
        intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int path_length = intent.getIntExtra(EXTRA_PATH_LENGTH, 0);
        int shortest_path = intent.getIntExtra(EXTRA_SHORTEST_PATH, 0);
        int energy_consumption = intent.getIntExtra(EXTRA_ENERGY_CONSUMPTION, 0);
        Log.v(TAG, "Shortest Path: " + shortest_path + ", Path Length: " + path_length + ", Energy Consumption: " + energy_consumption);

        //Initializes TextViews
        robot_losing_text = findViewById(R.id.robot_failure_text);
        path_length_text = findViewById(R.id.path_length_text);
        shortest_path_text = findViewById(R.id.shortest_path_text);
        energy_consumed_text = findViewById(R.id.total_energy_consumed);

        // Sets the text passed variables to the winning screen
        robot_losing_text.setText(message);
        path_length_text.setText("Total Path Length: " + path_length);
        shortest_path_text.setText("Shortest Path Length: " + shortest_path);
        energy_consumed_text.setText("Total Energy Consumed: " + energy_consumption);



        //FOR TOOLBAR
        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Dennis's Lost Woods");

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LosingActivity.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Back button is clicked.");
                openHome();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LosingActivity.this, "You clicked the settings icon", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Settings button is clicked.");
            }
        });


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
                        Toast.makeText(LosingActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "Home button is clicked.");
                        openHome();
                }

                return true;
            }
        });


        //Play Again button sends user back to title
        Play_Again_Button = findViewById(R.id.Play_Again_Button);
        Play_Again_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LosingActivity.this, "Play Again", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Play Again button is clicked.");
                openHome();
            }
        });

    }

    public void openHome() {
        Intent homeIntent = new Intent(this, AMazeActivity.class);
        startActivity(homeIntent);
    }
}
