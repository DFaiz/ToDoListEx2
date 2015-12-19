package il.ac.shenkar.david.todolistex2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class invite_member extends AppCompatActivity
{
    private EditText member_email;
    private final String email_Subject = "Invitation to Join OTS team";
    private final String email_body = "Hi,\n\n" +
                                "You have been invited to be a team member in an OTS Team created by me.\n" +
                                "Use this link to download and install the App from Google Play.\n\n" +
                                "<LINK to Google Play download>\n\n" +
                                "Best Regards,\n" +
                                "You OTS Manager.   ";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onInviteMember (View view)
    {
        member_email = (EditText) findViewById(R.id.editemailaddress);
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ member_email.getText().toString()});
        email.putExtra(Intent.EXTRA_SUBJECT,email_Subject );
        email.putExtra(Intent.EXTRA_TEXT,email_body );

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    public void onDonebtn (View view)
    {
        Intent returnIntent = new Intent(this,MainActivity.class);
        setResult(RESULT_OK, returnIntent);
        startActivity(returnIntent);
    }

    public void onExitbtn(View view)
    {
        finish();
        System.exit(0);
    }

}
