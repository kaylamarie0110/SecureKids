package com.igrapesinc.securekids;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDisplayActivity extends Activity {
	private GoogleMap googleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_display);

		// Google Map
		try {
			// Loading map
			initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.mapView)).getMap();
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.getUiSettings().setZoomGesturesEnabled(false);
			googleMap.setBuildingsEnabled(true);

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			} else {
				// latitude and longitude(please take it from previous class)
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(36.8860153, -76.30439249999999))
						.zoom(17).build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
				// latitude and longitude(please take it from previous class)
				double latitude = 36.8860153;
				double longitude = -76.30439249999999;

				// create marker
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title(
						"Secure Kids");
				// GREEN color icon
				marker.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
				// Adding marker
				googleMap.addMarker(marker);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	// Back Button Click
	public void backClick(View v) {
		finish();
	}
	
}