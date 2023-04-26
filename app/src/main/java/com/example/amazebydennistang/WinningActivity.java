package com.example.amazebydennistang;

import android.content.Intent;
import android.os.Bundle;
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

public class WinningActivity extends AppCompatActivity {

    TextView path_length_text, shortest_path_text, energy_consumed_text;
    BottomNavigationView nav;
    Button Play_Again_Button;
    String EXTRA_ENERGY_CONSUMPTION = "com.example.mazewidgetpractice.EXTRA_ENERGY_CONSUMPTION" ;
    String EXTRA_PATH_LENGTH = "com.example.mazewidgetpractice.EXTRA_PATH_LENGTH";
    String EXTRA_SHORTEST_PATH = "com.example.mazewidgetpractice.SHORTEST_PATH" ;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winningactivity);

        intent = getIntent();

        // For manual maze win
        int path_length = intent.getIntExtra(EXTRA_PATH_LENGTH, 0);
        int shortest_path = intent.getIntExtra(EXTRA_SHORTEST_PATH, 0);

        // Additionally for automatic maze win
        int energy_consumption = intent.getIntExtra(EXTRA_ENERGY_CONSUMPTION, 0);

        path_length_text = findViewById(R.id.path_length_text);
        shortest_path_text = findViewById(R.id.shortest_path_text);
        energy_consumed_text = findViewById(R.id.total_energy_consumed);

        // Sets the text passed variables to the winning screen
        path_length_text.setText("Total Path Length: " + path_length);
        shortest_path_text.setText("Shortest Path Length: " + shortest_path);

        // Sets visibility of text depending on if winning screen was reached from manual or animation
        // Manual: energy_consumption = 0
        if (energy_consumption != 0){
            energy_consumed_text.setText("Total Energy Consumed: " + energy_consumption);
        }
        else if (energy_consumption == 0){
            energy_consumed_text.setVisibility(View.INVISIBLE);
        }




        //FOR TOOLBAR
        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("Dennis's Lost Woods");

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WinningActivity.this, "You clicked the back icon", Toast.LENGTH_SHORT).show();
                openHome();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WinningActivity.this, "You clicked in settings icon", Toast.LENGTH_SHORT).show();
            }
        });


        //FOR BOTTOM HOME BAR
        nav = findViewById(R.id.bottomNavigationView);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){ //Possibly add more options later
                    case R.id.home:
                        Toast.makeText(WinningActivity.this, "Home", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(WinningActivity.this, "Play Again button clicked!", Toast.LENGTH_SHORT).show();
                openHome();
            }
        });



    }
    public void openHome() {
        Intent homeIntent = new Intent(this, AMazeActivity.class);
        startActivity(homeIntent);
    }
}
