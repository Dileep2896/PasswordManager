package com.dileepkumar.passwordmanager.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dileepkumar.passwordmanager.DataBase.DataBaseHelper;
import com.dileepkumar.passwordmanager.MainActivity;
import com.dileepkumar.passwordmanager.PasswordAddPage;
import com.dileepkumar.passwordmanager.R;

public class DialogBox {

    // Creating activity code to get data from activity_password_add
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    MainActivity mainActivity;

    private final Activity activity;
    private AlertDialog alertDialog;

    private final String id;
    private final String email;
    private final String password;
    private final String web;

    DataBaseHelper myDataBase;

    public DialogBox(Activity activity, MainActivity mainActivity, DataBaseHelper myDataBase,
                     String web, String id, String email, String password) {
        this.activity = activity;
        this.mainActivity = mainActivity;

        this.myDataBase = myDataBase;

        this.id = id;
        this.web = web;
        this.email = email;
        this.password = password;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_dialog_box, null);

        TextView tvDialogEmail = dialogLayout.findViewById(R.id.tvDialogEmail);
        TextView tvDialogPassword = dialogLayout.findViewById(R.id.tvDialogPassword);
        TextView tvDialogWebName = dialogLayout.findViewById(R.id.tvDialogWebName);

        ImageButton ibDialogCopy = dialogLayout.findViewById(R.id.ibDialogCopy);
        ImageButton ibDialogOK = dialogLayout.findViewById(R.id.ibDialogOK);
        ImageButton ibDialogEdit = dialogLayout.findViewById(R.id.ibDialogEdit);
        ImageButton ibDialogDelete = dialogLayout.findViewById(R.id.ibDialogDelete);

        tvDialogWebName.setText(web);
        tvDialogEmail.setText(email);
        tvDialogPassword.setText(password);

        builder.setView(dialogLayout);
        builder.setCancelable(false);

        // Building the dialog box
        alertDialog = builder.create();

        // Removing the white background from the dialog box
        alertDialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Showing the dialog box
        alertDialog.show();

        // Button to dismiss the dialog box
        ibDialogOK.setOnClickListener(view -> {
            dismissDialog();
        });

        // Button to copy the password to clipboard
        ibDialogCopy.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("password", password);
            clipboard.setPrimaryClip(clip);
            toastMessage("Password Copied To Clipboard", R.drawable.custom_bg_primary,
                    R.drawable.ic_copy);
        });

        // Button to delete the data from database
        ibDialogDelete.setOnClickListener(view -> {
            myDataBase.deleteData(id);
            mainActivity.reload();
            dismissDialog();
        });

        // Button to edit the data of database
        ibDialogEdit.setOnClickListener(view -> {
            Intent intent = new Intent(activity, PasswordAddPage.class);
            intent.putExtra("websiteName", web);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("id", id);
            intent.putExtra("isUpdate", true);
            activity.startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);;
            dismissDialog();
        });

    }

    // Method to dismiss the dialog box
    public void dismissDialog() {
        alertDialog.dismiss();
    }


    // Creating a custom toast message
    public void toastMessage(String message, int background, int icon) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                activity.findViewById(R.id.toast_layout_root));
        layout.setBackgroundResource(background);

        ImageView image = layout.findViewById(R.id.image);
        image.setImageResource(icon);
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
