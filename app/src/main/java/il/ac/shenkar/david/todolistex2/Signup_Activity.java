package il.ac.shenkar.david.todolistex2;

/**
 * Created by David on 19-Dec-15.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

public class Signup_Activity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private String blockCharacterSet = "~#^|$%&*!";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextUsername = (EditText) findViewById(R.id.editTextusername);
        editTextUsername.setFilters(new InputFilter[] { special_filter });
    }

    public void onExitbtn(View view)
    {
        finish();
        System.exit(0);
    }

    public void onSignUpbtn(View view)
    {
        editTextUsername = (EditText) findViewById(R.id.editTextusername);
        editTextPassword = (EditText) findViewById(R.id.userpsswrd);
        editTextPhone = (EditText) findViewById(R.id.userphonenumber);

        boolean valid_inputs=true;

        if(editTextUsername.getText().toString().matches(""))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Enter Username")
                    .setMessage("Username field is empty.\nPlease enter a valid username")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        if(editTextUsername.getText().toString().length()<8)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Invalid Username")
                    .setMessage("Username must be at least 8 characters.\nPlease enter a valid Username")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        if(editTextPassword.getText().toString().matches(""))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Enter Password")
                    .setMessage("Password field is empty.\nPlease enter a valid password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        if(editTextPassword.getText().toString().length()<8)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Invalid Password")
                    .setMessage("Password must be at least 8 characters.\nPlease enter a valid password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        if(isAlphaNumeric(editTextPassword.getText().toString())==false)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Invalid Password")
                    .setMessage("Password must contain both letters and digits.\nPlease enter a valid password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        if(editTextPhone.getText().toString().matches(""))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Enter Password")
                    .setMessage("Password field is empty.\nPlease enter a valid password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        if(editTextPhone.getText().toString().length()!=10)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Enter Phone Number")
                    .setMessage("Phone Number must be 10 digits.\nPlease enter a valid Phone Number")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        if(valid_inputs)
        {
            Globals.signed_uped=true;
            Intent returnIntent = new Intent(this,create_team.class);
            setResult(RESULT_OK, returnIntent);
            startActivity(returnIntent);
        }
    }

    private InputFilter special_filter = new InputFilter()
    {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }
}