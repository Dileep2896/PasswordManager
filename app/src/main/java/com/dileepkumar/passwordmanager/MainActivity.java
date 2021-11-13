package com.dileepkumar.passwordmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dileepkumar.passwordmanager.DataBase.DataBaseHelper;
import com.dileepkumar.passwordmanager.custom.DialogBox;
import com.dileepkumar.passwordmanager.custom.ListAdapter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    // Creating activity code to get data from activity_password_add
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    // Creating variables to handle data
    ArrayList<String> userData;

    // creating a My HashTable Dictionary
    Hashtable<String, String> my_dict = new Hashtable<String, String>();

    // Creating an instance of custom list adapter
    ListAdapter listAdapter;

    ListView lvEmails;
    TextView tvNotData;

    DataBaseHelper myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising the view items
        lvEmails = findViewById(R.id.lvEmails);
        tvNotData = findViewById(R.id.tvNotData);

        tvNotData.setVisibility(View.GONE);
        lvEmails.setVisibility(View.VISIBLE);

        tvNotData.setText(" ");

        // Initialising the ArrayList with empty values
        userData = new ArrayList<>();

        // Creating an instance of the SQLite DataBase
        myDataBase = new DataBaseHelper(this);

        // Setting the custom listAdapter...
        listAdapter = new ListAdapter(this, userData);
        lvEmails.setAdapter(listAdapter);

        // Setting on click listener on the listview.
        lvEmails.setOnItemClickListener((adapterView, view, i, l) -> {

            // Displaying the dialog box to show, edit, copy and delete fields
            DialogBox dialogBox = new DialogBox(this,
                    this,
                    myDataBase,
                    userData.get(i),
                    Objects.requireNonNull(my_dict.get(userData.get(i))).split("\\|")[0],
                    Objects.requireNonNull(my_dict.get(userData.get(i))).split("\\|")[1],
                    Objects.requireNonNull(my_dict.get(userData.get(i))).split("\\|")[2]);
            dialogBox.startLoadingDialog();

            Log.i("Email", userData.get(i));
            Log.i("Password", my_dict.get(userData.get(i)));
        });

        // Method to display all the data from DataBase.
        displayData();

    }

    public void reload() {
        finish();
        startActivity(getIntent());
    }

    public void displayData() {

        // Clean The userData
        userData.clear();

        // Getting the data from the database using cursor
        Cursor cursor = myDataBase.getAllData();

        // Checking weather we have data in database or not
        if (cursor.getCount() == 0) {
            tvNotData.setVisibility(View.VISIBLE);
            lvEmails.setVisibility(View.GONE);

            tvNotData.setText("No Data Saved To Display\n\nClick '+' Button To Start Adding Data");
        } else {
            tvNotData.setVisibility(View.GONE);
            lvEmails.setVisibility(View.VISIBLE);
            tvNotData.setText(" ");

            while (cursor.moveToNext()) {
                my_dict.put(cursor.getString(1),
                            cursor.getString(0) + "|" +
                            cursor.getString(2) + "|" +
                            cursor.getString(3));

                // adding the website name to the arrayLists...
                userData.add(cursor.getString(1));
            }

            listAdapter.notifyDataSetChanged();

            Log.i("DataFromDataBase", String.valueOf(my_dict));
        }

    }

    // Floating action button function to move to next activity to get user data
    public void btnAddPassword(View view) {
        Intent intent = new Intent(this, PasswordAddPage.class);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    // Getting back the user data and handling
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent
                if (data != null) {

                    // Fetching the email and password from the PasswordAddPage.
                    // Assigning the email and password to the new String.
                    String returnWebsite = data.getStringExtra("website");
                    String returnEmail = data.getStringExtra("email");
                    String returnPassword = data.getStringExtra("password");
                    String returnId = data.getStringExtra("id");
                    boolean isUpdate = data.getBooleanExtra("isUpdate", false);
                    Log.i("UserData", returnEmail);

                    // checking weather is to update or add
                    if (isUpdate) {
                        // Updating the email, webName and password in the SQLite Database
                        updateData(returnId, returnWebsite, returnEmail, returnPassword);
                    } else {
                        // Adding the email & password to the SQLite Database
                        AddData(returnWebsite, returnEmail, returnPassword);
                    }

                }
            }
        }

    }

    public void AddData(String website, String email, String password) {

        // Inserting the email and password to the database by calling insert from DataBaseHelper
        boolean result = myDataBase.insertData(website, email, password);

        // Checking weather the insert was success or not
        if (result) {
            // If the Insertion is successful
            toastMessage("Password Saved To Local Database",
                    R.drawable.custom_bg_primary, R.drawable.ic_done);
            displayData();
        } else {
            // If the Insertion is not successful
            toastMessage("Password Not Saved To Database",
                    R.drawable.custom_bg_accent, R.drawable.ic_error);
        }
    }

    public void updateData(String id, String website, String email, String password) {
        boolean isUpdated = myDataBase.updateData(id, website, email, password);

        // Checking weather the update was success or not
        if (isUpdated) {
            // If the update is successful
            toastMessage("Your Data Is Updated",
                    R.drawable.custom_bg_primary, R.drawable.ic_done);
            displayData();
        } else {
            // If the Update is not successful
            toastMessage("Your Data Was Not Updated",
                    R.drawable.custom_bg_accent, R.drawable.ic_error);
        }

    }

    // Creating a custom toast message
    public void toastMessage(String message, int background, int icon) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                findViewById(R.id.toast_layout_root));
        layout.setBackgroundResource(background);

        ImageView image = layout.findViewById(R.id.image);
        image.setImageResource(icon);
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}