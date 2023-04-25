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

    BottomNavigationView nav;
    Button Play_Again_Button;

    Intent intent = getIntent();

    //For manual maze win
    int path_length = intent.getIntExtra("EXTRA_PATH_LENGTH",0);
    int shortest_path = intent.getIntExtra("EXTRA_SHORTEST_PATH",0);

    //For automatic maze win
    int energy_consumption = intent.getIntExtra("EXTRA_ENERGY_CONSUMPTION",0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winningactivity);

        //FOR TOOLBAR
        ImageView leftIcon = findViewById(R.id.left_icon);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);

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
        title.setText("Dennis's Lost Woods");

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
                openHome();
            }
        });

    }
    public void openHome() {
        Intent homeIntent = new Intent(this, AMazeActivity.class);
        startActivity(homeIntent);
    }
}
