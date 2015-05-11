package org.altbeacon.beaconreference;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

/**
 * 
 * @author dyoung
 * @author Matt Tyler
 */
public class MonitoringActivity extends Activity {
	protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		
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
//			       String  url = "http://www.google.com";
//					
//					Intent intentNew = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//					intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					//intentNew.get
//					application.getmRegionBootstrap().disable();
//				    getApplicationContext().startActivity(intentNew);
				dialog.cancel();
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
		
		
		
		
		
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);
		//verifyBluetooth();
	}
	
	public void onRangingClicked(View view) {
		Intent myIntent = new Intent(this, RangingActivity.class);
		alertDialog.show();
		this.startActivity(myIntent);
	}

	private void verifyBluetooth() {

		try {
			if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");			
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();
			            System.exit(0);					
					}					
				});
				builder.show();
			}			
		}
		catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");			
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					finish();
		            System.exit(0);					
				}
				
			});
			builder.show();
			
		}
		
	}	

    @Override 
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override 
    protected void onPause() {
    	super.onPause();
    	// Tell the Application not to pass off monitoring updates to this activity
    	((BeaconReferenceApplication)this.getApplication()).setMonitoringActivity(null);
    }
    @Override 
    protected void onResume() {
    	super.onResume();
    	// Tell the Application to pass off monitoring updates to this activity
    	((BeaconReferenceApplication)this.getApplication()).setMonitoringActivity(this);
    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }    
    
    private void logToDisplay(final String line,final String message) {
    	runOnUiThread(new Runnable() {
    	    public void run() {
    	    	EditText editText = (EditText)MonitoringActivity.this
    					.findViewById(R.id.monitoringText);
       	    	editText.append(line+"\n"); 
       	    	alertDialog.setMessage(message);
       	     //alertDialog.show();
       	    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    	    }
    	});
    }
    
    public void didEnterRegion(Region region) {
      logToDisplay("I just saw a beacon named "+ region.getUniqueId() +" for the first time!","Alana giris yaptiniz" );
//      alertDialog.setMessage("Alana giris yaptiniz");
//  	  alertDialog.show();
      //Toast.makeText(getApplicationContext(), "Enter", Toast.LENGTH_SHORT).show();
    }

    public void didExitRegion(Region region) {
    	logToDisplay("I no longer see a beacon named "+ region.getUniqueId(),"Alandan cikis yaptiniz");
//    	 alertDialog.setMessage("Alandan cikis yaptiniz");
//     	  alertDialog.show();
    	//Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
    }

    public void didDetermineStateForRegion(int state, Region region) {
    	logToDisplay("I have just switched from seeing/not seeing beacons: "+state,"Beacon bulundu");
//    	 alertDialog.setMessage("Beacon bulundu");
//     	  alertDialog.show();
    	//Toast.makeText(getApplicationContext(), "Determine", Toast.LENGTH_SHORT).show();
    }
	
}
