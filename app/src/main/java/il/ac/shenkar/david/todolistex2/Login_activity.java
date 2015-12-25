package il.ac.shenkar.david.todolistex2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.TextView;

public class Login_activity extends AppCompatActivity
{
    private EditText usrname;
    private EditText usrpwd;
    private TextView usrname_title;
    private TextView password_title;

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
    }

    public void onLoginbtn (View view)
    {
        boolean valid_inputs=true;
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

        if(valid_inputs)
        {
            Intent returnIntent = new Intent(this,MainActivity.class);
            setResult(RESULT_OK, returnIntent);
            startActivity(returnIntent);
        }
    }
}
