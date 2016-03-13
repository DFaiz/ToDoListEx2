package il.ac.shenkar.david.todolistex2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class Login_activity extends AppCompatActivity
{
    private EditText usrname;
    private EditText usrpwd;
    private TextView usrname_title;
    private TextView password_title;
    private CheckBox remb_creds=null;

    private ParseObject parse_usr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usrname = (EditText)findViewById(R.id.editTextusername);
        usrpwd = (EditText)findViewById(R.id.userpsswrd);

        usrname.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    usrname.clearFocus();
                    usrpwd.requestFocus();
                    return true;
                }
                return false;
            }
        });

        SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);
        if(sharedpreferences.getString("LoginUsr", null)!=null)
        {
            usrname.setText(sharedpreferences.getString("LoginUsr", null));
            remb_creds = (CheckBox)findViewById(R.id.saveLoginInfo);
            remb_creds.setChecked(true);
        }
        if(sharedpreferences.getString("LoginPswd",null)!=null)
        {
            usrpwd.setText(sharedpreferences.getString("LoginPswd", null));
        }
    }

    public void onLoginbtn (View view)
    {
        boolean valid_inputs=true;
        List<ParseObject> usrs = null;
        String curr_usrname=null;

        usrname = (EditText)findViewById(R.id.editTextusername);
        usrpwd = (EditText)findViewById(R.id.userpsswrd);
        usrname_title = (TextView)findViewById(R.id.usernametextView);
        password_title = (TextView)findViewById(R.id.passwstextView);

        if(usrname.getText().toString().matches(""))
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

        if((usrname.getText().toString().length()<5)&&(valid_inputs))
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

        if(usrpwd.getText().toString().matches(""))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Enter Password")
                    .setMessage("Password field is empty.\nPlease enter a valid password")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            password_title.setTextColor(Color.RED);
            valid_inputs = false;
        }

        if((usrpwd.getText().toString().length()<8)&&(valid_inputs))
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
            password_title.setTextColor(Color.RED);
            valid_inputs = false;
        }

        //check is username & password exist
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("OTSUser");
        query.whereEqualTo("Username", usrname.getText().toString());
        query.whereEqualTo("Password", usrpwd.getText().toString());

        try {
            usrs = query.find();
            if (usrs.size() == 0){
                valid_inputs = false;
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Credentials")
                        .setMessage("Username or password incorrect")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();}
            else if (usrs.size() == 1){
                valid_inputs = true;}
            else{
                valid_inputs = false;
                Log.d("login", "invalid");
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Credentials")
                        .setMessage("Username or password incorrect")
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
            remb_creds = (CheckBox)findViewById(R.id.saveLoginInfo);
            if(remb_creds.isChecked()) {
                SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);
                curr_usrname = sharedpreferences.getString("LoginUsr", null);
                if((curr_usrname.equals(usrname.getText().toString()))==false)
                {
                    Globals.diffusr=true;
                }
                sharedpreferences.edit().putString("LoginUsr", usrname.getText().toString()).apply();
                sharedpreferences.edit().putString("LoginPswd", usrpwd.getText().toString()).apply();
            }

            //update team name
            Globals.team_name=usrs.get(0).getString("TeamName");

            if(usrs.get(0).getNumber("IsManager")==1)
            {
                Globals.IsManager=true;
            }
            else {
                    Globals.IsManager = false;

                    query = new ParseQuery<ParseObject>("Teams");
                    query.whereEqualTo("TeamName", Globals.team_name);

                    try {
                        usrs = query.find();
                        if (usrs.size() == 1){

                        }
                        else
                        {

                        }
                    } catch (ParseException e) {}

                    if(Globals.diffusr)
                    {
                    Toast.makeText(this, "You have been added to Team: " + usrs.get(0).getString("TeamName") +" by\n"
                            + usrs.get(0).get("TeamManager"), Toast.LENGTH_LONG).show();}
            }

            Intent returnIntent = new Intent(this,MainActivity.class);
            setResult(RESULT_OK, returnIntent);
            startActivity(returnIntent);
        }
    }

    public void onExitbtn (View v)
    {
        finish();
        System.exit(0);
    }
}