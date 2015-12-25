package il.ac.shenkar.david.todolistex2;

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

public class CreateTeam extends AppCompatActivity
{
    private EditText editTextTeamname;
    private String blockCharacterSet = "~#^|$%&*!";

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

        if(valid_inputs)
        {
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
