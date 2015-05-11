package org.altbeacon.beaconreference;

import java.util.Collection;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.WindowManager;
import android.widget.EditText;

public class RangingActivity extends Activity {
    protected static final String TAG = "RangingActivity";
    BeaconReferenceApplication application;
   
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranging);
		application = ((BeaconReferenceApplication)this.getApplication());
		
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Defacto indirim firsati");

		Cursor c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null); 
		c.moveToFirst();
		String name =(c.getString(c.getColumnIndex("display_name")));
		c.close();
		// set dialog message
		alertDialogBuilder
			.setMessage("Merhaba "+name+".Viaport defactoda size özel indirimler var")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				Intent intent = getIntent();
					 
			        // 2. get message value from intent
			        //String url = (String)intent.getExtras().get("url");
			       String  url = "http://www.google.com";
					
					Intent intentNew = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//intentNew.get
					application.getmRegionBootstrap().disable();
				    getApplicationContext().startActivity(intentNew);
				}
			  })
			.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});

			// create alert dialog
			alertDialog = alertDialogBuilder.create();
		
		
    }
    @Override 
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override 
    protected void onPause() {
    	super.onPause();
    	// Tell the Application not to pass off ranging updates to this activity
    	((BeaconReferenceApplication)this.getApplication()).setRangingActivity(null);
    }
    @Override 
    protected void onResume() {
    	super.onResume();
    	// Tell the Application to pass off ranging updates to this activity
    	((BeaconReferenceApplication)this.getApplication()).setRangingActivity(this);
    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }    

    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() > 0) {
        	EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
            for (Beacon beacon: beacons) {
            	if(beacon.getBluetoothName().contains("Mini"))
            		logToDisplay("Beacon "+beacon.toString()+" is about "+beacon.getDistance()+" meters away, with Rssi: "+beacon.getRssi(),"Entering");            	
            }
        }
    }

    private void logToDisplay(final String line,final String action) {
    	runOnUiThread(new Runnable() {
    	    public void run() {
    	    	EditText editText = (EditText)RangingActivity.this
    					.findViewById(R.id.rangingText);
    	    	editText.append(line+"\n");  
    	    	editText.setText(line);
    	    	if("Entering".equals(action))
    	    		alertDialog.setMessage("Alana giris yaptiniz");
    	    	if("Existing".equals(action))
    	    		alertDialog.setMessage("Alandan cikis yaptiniz");
    	    	alertDialog.show();
    	    	
    	    }
    	});
    }
}
