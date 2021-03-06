package il.ac.shenkar.david.todolistex2;

/**
 * Created by David on 19-Dec-15.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class Signup_Activity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private String blockCharacterSet = "~#^|$%&*!";
    private TextView usrname_title;
    private TextView usrpswd_title;
    private ParseObject parse_otsusr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextUsername = (EditText) findViewById(R.id.editTextusername);
        editTextPassword = (EditText) findViewById(R.id.userpsswrd);
        editTextUsername.setFilters(new InputFilter[] { special_filter });

        editTextUsername.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    editTextUsername.clearFocus();
                    editTextPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });

        editTextPassword.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    editTextPassword.clearFocus();
                    return true;
                }
                return false;
            }
        });

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
        usrname_title = (TextView) findViewById(R.id.usernametextView);
        usrpswd_title = (TextView) findViewById(R.id.passwstextView);

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
            usrname_title.setTextColor(Color.RED);
            valid_inputs = false;
        }

        if((editTextUsername.getText().toString().length()<5)&&(valid_inputs))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Invalid Username")
                    .setMessage("Username must be at least 5 characters.\nPlease enter a valid Username")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            usrname_title.setTextColor(Color.RED);
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
            usrpswd_title.setTextColor(Color.RED);
            valid_inputs = false;
        }

        if((editTextPassword.getText().toString().length()<8)&&(valid_inputs))
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
            usrpswd_title.setTextColor(Color.RED);
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
            usrpswd_title.setTextColor(Color.RED);
            valid_inputs = false;
        }

        //check is username & password exist
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("OTSUser");
        query.whereEqualTo("Username", editTextUsername.getText().toString());
        List<ParseObject> usrs=null;

        try {
            usrs = query.find();

            if (usrs.size() == 1){
                valid_inputs = false;
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Credentials")
                        .setMessage("Manager info already exists")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();}
            else if (usrs.size() == 0){
                valid_inputs = true;}
            else{
                valid_inputs = false;
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Credentials")
                        .setMessage("Username or password do not exist")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();}
        } catch (ParseException e) {}

        if(valid_inputs)
        {
            SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);
            sharedpreferences.edit().putBoolean("LoginState", true).apply();
            sharedpreferences.edit().putString("LoginUsr", editTextUsername.getText().toString()).apply();
            sharedpreferences.edit().putString("LoginPswd", editTextPassword.getText().toString()).apply();
            Globals.signed_uped=true;

            parse_otsusr = new ParseObject("OTSUser");
            parse_otsusr.put("Username", editTextUsername.getText().toString());
            parse_otsusr.put("Password",editTextPassword.getText().toString());
            parse_otsusr.put("Email", editTextUsername.getText().toString());
            parse_otsusr.put("IsManager", 1);
            parse_otsusr.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // if null, it means the save has succeeded
                    } else {
                        // the save call was not successful.
                    }
                }
            });

            Intent returnIntent = new Intent(this,CreateTeam.class);
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