package il.ac.shenkar.david.todolistex2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class InviteMember extends AppCompatActivity
{
    private EditText member_email;
    private EditText member_phone;
    private final String email_Subject = "Invitation to Join Team OTS";
    private final String email_body = "Hi,\n\n" +
                                "You have been invited to be a team member in an OTS Team ->" + Globals.team_name + " created by me.\n" +
                                "Your username is your email address and password is your phone number\n" +
                                "Use this link to download and install the App from Google Play.\n\n" +
                                "https://play.google.com/store/apps/details?id=il.ac.shenkar.david.todolistex2\n\n" +
                                "Best Regards,\n" +
                                "You OTS Manager.";

    private ArrayList<String> emp_List;
    ListView all_emp_list;
    private ImageView person_img;
    EmployeeItemAdapter emp_item_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getEmployeeList();

        person_img = (ImageView) findViewById(R.id.person_icon);
        person_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(InviteMember.this);
                dialog.setContentView(R.layout.employeelist);
                dialog.setTitle("Employee List");
                all_emp_list = (ListView) dialog.findViewById(R.id.emplist_listView);
                emp_item_adapter = new EmployeeItemAdapter (InviteMember.this,emp_List);
                all_emp_list.setAdapter(emp_item_adapter);
                dialog.show();
            }
        });
    }

    public void onInviteMember (View view)
    {
        member_email = (EditText) findViewById(R.id.editemailaddress);
        member_phone = (EditText) findViewById(R.id.memberuserphonenumber);
        Intent email = new Intent(Intent.ACTION_SEND);

        String[] new_users = member_email.getText().toString().split(",");
        addNewUsers(new_users,member_phone.getText().toString());

        email.putExtra(Intent.EXTRA_EMAIL, new String[]{member_email.getText().toString()});
        email.putExtra(Intent.EXTRA_SUBJECT, email_Subject);
        email.putExtra(Intent.EXTRA_TEXT, email_body);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    public void onDonebtn (View view)
    {
        Intent i = getIntent();
        Intent returnIntent = null;

        String from_act = (String)i.getSerializableExtra("from");

        if(from_act.equals("from_main_activity"))
        {
            returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }

        else
        {
            returnIntent = new Intent(this,MainActivity.class);
            setResult(RESULT_OK, returnIntent);
            startActivity(returnIntent);
        }
    }

    public void onExitbtn(View view) {
        Intent i = getIntent();
        Intent returnIntent = null;

        String from_act = (String)i.getSerializableExtra("from");
        if(from_act.equals("from_main_activity"))
        {
            returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }

        else
        {
            System.exit(0);
        }
    }

    private void addNewUsers (String[] newUsers, String usrpwd)
    {
        SharedPreferences sharedpreferences = getSharedPreferences("il.ac.shenkar.david.todolistex2", Context.MODE_PRIVATE);

        ParseObject parse_otsusr = null;
        for (String usr : newUsers)
        {
            parse_otsusr = new ParseObject("OTSUser");
            parse_otsusr.put("Username", usr);
            parse_otsusr.put("Password",usrpwd);
            parse_otsusr.put("Email", usr);
            parse_otsusr.put("IsManager", 0);
            parse_otsusr.put("TeamName",Globals.team_name);
            parse_otsusr.put("ManagerName",sharedpreferences.getString("LoginUsr", null));
            parse_otsusr.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // if null, it means the save has succeeded
                    } else {
                        // the save call was not successful.
                    }
                }
            });
        }
    }

    private void getEmployeeList ()
    {
        emp_List = new ArrayList<>();

        //check is username & password exist
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("OTSUser");
        query.whereContains("TeamName", Globals.team_name);
        query.whereEqualTo("IsManager", 0);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> usrs, ParseException e) {
                if (e == null) {
                    for (ParseObject prso : usrs) {
                        emp_List.add(new String(prso.getString("Username")));
                    }
                } else {//handle the error
                }
            }
        });
    }
}
