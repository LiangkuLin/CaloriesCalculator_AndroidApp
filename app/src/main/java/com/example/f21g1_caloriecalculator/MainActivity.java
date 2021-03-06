package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="Calorie Demo";
    EditText editName, editPass;
    Button btnLogIn, btnSignUp;
    DBHelper DB;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName=findViewById(R.id.editTextName);
        editPass=findViewById(R.id.editTextPass);
        btnLogIn=findViewById(R.id.btnLogIn);
        btnSignUp=findViewById(R.id.btnSignUp);
        DB=new DBHelper(MainActivity.this);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        btnLogIn.setOnClickListener((View v)->{
            String name=editName.getText().toString();
            String pas=editPass.getText().toString();

            //logic steps: 1st check editText is empty?
            //2nd check account exists?
            try{
                if(name.isEmpty()||pas.isEmpty()){
                    Toast.makeText(MainActivity.this, "If you have account already, please enter!", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean isAccountExist=DB.checkUserNamePassword(name,pas);
                    if(isAccountExist==true){
                        Toast.makeText(MainActivity.this, "Welcome back "+name, Toast.LENGTH_SHORT).show();
                        int userId = DB.getUserId(name, pas);
//                        Log.d("Debug",userId + "!");

                        // use the sharepreferences to share the preferences
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Name", name);
                        editor.putString("Password", pas);
                        editor.putInt("UserId", userId);
                        editor.commit();

                        if(DB.getTDEE(userId)==null){ //this is a new user
                            startActivity(new Intent(MainActivity.this,RecommendationsPage.class));
                        }else{ //this is an old user
                            startActivity(new Intent(MainActivity.this, Calendar.class));
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Account is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Log.e(TAG, e.getMessage());
            }
        });

        btnSignUp.setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
        });
    }
}