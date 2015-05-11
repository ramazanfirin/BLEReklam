package org.altbeacon.beaconreference;

import java.util.Collection;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import android.app.Application;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

public class BeaconReferenceApplication extends Application implements BootstrapNotifier, RangeNotifier {
	private static final String TAG = "BeaconReferenceApplication";
	private BeaconManager mBeaconManager;
	private Region mAllBeaconsRegion;
	private MonitoringActivity mMonitoringActivity;
	private RangingActivity mRangingActivity;
	private BackgroundPowerSaver mBackgroundPowerSaver;
	@SuppressWarnings("unused")
	private RegionBootstrap mRegionBootstrap;
	
	public RegionBootstrap getmRegionBootstrap() {
		return mRegionBootstrap;
	}

	public void setmRegionBootstrap(RegionBootstrap mRegionBootstrap) {
		this.mRegionBootstrap = mRegionBootstrap;
	}

	@Override 
	public void onCreate() {
		mAllBeaconsRegion = new Region("all beacons", null, null, null);
		
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.setBackgroundBetweenScanPeriod(5000);
        mBeaconManager.setBackgroundScanPeriod(5000);
        mBeaconManager.setForegroundBetweenScanPeriod(5000);
        mBeaconManager.setForegroundScanPeriod(5000);
      //  mBeaconManager.setDebug(true);
		mBackgroundPowerSaver = new BackgroundPowerSaver(this);		
        
		mRegionBootstrap = new RegionBootstrap(this, mAllBeaconsRegion);
        
//        mBeaconManager.getBeaconParsers().add(new BeaconParser()
//		   .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        
//        mBeaconManager.getBeaconParsers().add(new BeaconParser()
//		   .setBeaconLayout("m:2-3=4C00,i:9-24,i:25-26,i:27-28,p:29-29"));
        
        
        mBeaconManager.getBeaconParsers().add(new BeaconParser()
		   .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        
        //m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25
        
	
        // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
        // find a different type of beacon, you must specify the byte layout for that beacon's
        // advertisement with a line like below.  The example shows how to find a beacon with the
        // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb
        //
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=aabb,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        //
        // In order to find out the proper BeaconLayout definition for other kinds of beacons, do
        // a Google search for "setBeaconLayout" (including the quotes in your search.)
	}
	
	@Override
	public void didRangeBeaconsInRegion(Collection<Beacon> arg0, Region arg1) {
		if (mRangingActivity != null) {
			mRangingActivity.didRangeBeaconsInRegion(arg0, arg1);
		}
//		
		
//		mBeaconManager.getBeaconParsers().add(new BeaconParser()
//		   .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
		//mRegionBootstrap.disable();
	       
		 for (Beacon beacon: arg0) {
         	if(beacon.getBluetoothName().contains("Mini")){
         		Intent intent = new Intent(this, RangingActivity.class);
    	        // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
    	        // created when a user launches the activity manually and it gets launched from here.
    	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	        this.startActivity(intent);
         	}
//         	            	
         }
		
		
		
	}

	@Override
	public void didDetermineStateForRegion(int arg0, Region arg1) {
		// TODO Auto-generated method stub
		if (mMonitoringActivity != null) {
		mMonitoringActivity.didDetermineStateForRegion(arg0, arg1);
		}
	}

	@Override
	public void didEnterRegion(Region arg0) {
		if (mMonitoringActivity != null) {
			mMonitoringActivity.didEnterRegion(arg0);
		}		
//		try {
//			Log.d(TAG, "entered region.  starting ranging");
//			mBeaconManager.startRangingBeaconsInRegion(mAllBeaconsRegion);
//			mBeaconManager.setRangeNotifier(this);
//		} catch (RemoteException e) {
//			Log.e(TAG, "Cannot start ranging");
//		}
	}

	@Override
	public void didExitRegion(Region arg0) {
		if (mMonitoringActivity != null) {
			mMonitoringActivity.didExitRegion(arg0);
		}				
	}
	
	public void setMonitoringActivity(MonitoringActivity activity) {
		mMonitoringActivity = activity;
	}

	public void setRangingActivity(RangingActivity activity) {
		mRangingActivity = activity;
	}
	
}
