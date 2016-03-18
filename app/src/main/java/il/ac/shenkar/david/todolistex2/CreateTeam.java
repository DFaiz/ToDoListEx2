package il.ac.shenkar.david.todolistex2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class CreateTeam extends AppCompatActivity
{
    private EditText editTextTeamname;
    private String blockCharacterSet = "~#^|$%&*!";

    private ParseObject parse_team_name=null;
    private ParseObject ots_User=null;
    private ParseQuery<ParseObject> query=null;
    private List<ParseObject> usrs=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextTeamname = (EditText) findViewById(R.id.editTextteamname);
        editTextTeamname.setFilters(new InputFilter[]{special_filter});
    }

    public void onExitbtn(View view)
    {
        finish();
        System.exit(0);
    }

    public void onInviteMembers(View view)
    {
        editTextTeamname = (EditText) findViewById(R.id.editTextteamname);
        boolean valid_inputs=true;

        if(editTextTeamname.getText().toString().matches(""))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Enter Team Name")
                    .setMessage("Team Name is empty.\nPlease enter a valid team name of at least 5 characters")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        if(editTextTeamname.getText().toString().length()<5)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Enter Team Name")
                    .setMessage("Team Name too short.\nPlease enter a valid team name of at least 5 characters")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            valid_inputs = false;
        }

        //check is team name already exists
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Teams");
        query.whereEqualTo("TeamName", editTextTeamname.getText().toString());
        List<ParseObject> team_names;

        try {
            team_names = query.find();
            if (team_names.size() > 0){
                valid_inputs = false;
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Team Name")
                        .setMessage("Team name already exists, please enter different name.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();}
            else {valid_inputs = true;}

        } catch (ParseException e) {}

        if(valid_inputs)
        {
            Globals.team_name = editTextTeamname.getText().toString();
            SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);
            sharedpreferences.edit().putString("TeamName", editTextTeamname.getText().toString()).apply();

            parse_team_name = new ParseObject("Teams");
            parse_team_name.put("TeamName", Globals.team_name);
            parse_team_name.put("TeamManager", sharedpreferences.getString("LoginUsr", null));
            parse_team_name.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // if null, it means the save has succeeded
                    } else {
                        // the save call was not successful.
                    }
                }
            });

            query = new ParseQuery<ParseObject>("OTSUser");
            query.whereEqualTo("Username",(sharedpreferences.getString("LoginUsr", null)));

            try {
                usrs = query.find();
                for (ParseObject tmp : usrs)
                {
                    tmp.put("TeamName",Globals.team_name);

                    tmp.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // if null, it means the save has succeeded
                            } else {
                            }
                        }
                    });
                    tmp.saveInBackground();
                }
            } catch (ParseException e) {}

            Intent returnIntent = new Intent(this,InviteMember.class);
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
}