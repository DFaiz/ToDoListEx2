package il.ac.shenkar.david.todolistex2;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class select_location extends AppCompatActivity
{
    private static final int ACTIVITY_RESULT_QR_DRDROID = 0;

    private Button scan_btn;
    private Spinner location_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        location_spinner = (Spinner) findViewById(R.id.selectlocationSpinner);

        scan_btn = (Button) findViewById(R.id.scanqr);

        scan_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                Intent i = new Intent("la.droid.qr.scan");

            }
        });
    }

    public void doneLoc (View view)
    {
        Locations selc_catg=null;
        int position = location_spinner.getSelectedItemPosition();
        switch(position)
        {
            case 0:
                selc_catg=Locations.Meeting_Room;
                break;
            case 1:
                selc_catg=Locations.Office_245;
                break;
            case 2:
                selc_catg=Locations.Lobby;
                break;
            case 3:
                selc_catg=Locations.NOC;
                break;
            case 4:
                selc_catg=Locations.VPsoffice;
                break;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("location",selc_catg);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
