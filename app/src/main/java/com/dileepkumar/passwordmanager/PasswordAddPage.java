package com.dileepkumar.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dileepkumar.passwordmanager.PasswordGenerator.PasswordGenerator;

import java.util.Arrays;

public class PasswordAddPage extends AppCompatActivity {

    // Creating EditText Variables
    EditText etAddEmail, etAddPassword, etAddWebsite;

    // Creating Button Variables
    Button btnAddPassword;

    // Creating String variable to handle email and password
    String email, password, web, id;

    // Creating PasswordGenerator Class Object
    PasswordGenerator generator;

    // Check for isUpdate
    boolean isUpdate = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_add_page);

        // Initialising Layout Components
        etAddEmail = findViewById(R.id.etAddEmail);
        etAddPassword = findViewById(R.id.etAddPassword);
        etAddWebsite = findViewById(R.id.etAddWebsite);
        btnAddPassword = findViewById(R.id.btnAddPassword);

        // Assigning PasswordGenerator to new class
        generator = new PasswordGenerator();

        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);

        if (isUpdate) {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            web = intent.getStringExtra("websiteName");
            id = intent.getStringExtra("id");

            etAddEmail.setText(email);
            etAddPassword.setText(password);
            etAddWebsite.setText(web);

            btnAddPassword.setText("Update");
        }

    }

    // Functionality to generate password
    public void btnGenerate(View view) {
        // Using Password Generator Class To Get Password
        String password = generator.getPassword(12);
        Log.i("Password", "Password - " + password);

        // Adding the generated password to the Edit Text
        etAddPassword.setText(password);
    }

    // Functionality off Add button on the password_add_page activity
    public void btnAdd(View view) {
        String email = etAddEmail.getText().toString();
        String password = etAddPassword.getText().toString();
        String web = etAddWebsite.getText().toString();

        // Checking if the editText is not empty
        if (email.isEmpty() || password.isEmpty() || web.isEmpty()) {
            // Using a custom toast
            toastMessage("Please Fill All The Fields",
                    R.drawable.custom_bg_accent,
                    R.drawable.ic_error);
        } else {
            Log.i("email", email);
            Log.i("password", password);
            Log.i("web", web);
            // Creating intent and passing the edit text values to the main activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("website", web);
            resultIntent.putExtra("email", email);
            resultIntent.putExtra("password", password);
            resultIntent.putExtra("isUpdate", isUpdate);
            resultIntent.putExtra("id", id);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

    }

    // Creating a custom toast message
    public void toastMessage(String message, int background, int icon) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        layout.setBackgroundResource(background);

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(icon);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}