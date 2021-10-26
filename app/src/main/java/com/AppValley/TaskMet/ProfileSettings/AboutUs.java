package com.AppValley.TaskMet.ProfileSettings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.AppValley.TaskMet.R;
import com.AppValley.TaskMet.constant.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AboutUs extends Fragment implements OnMapReadyCallback {

    Context context;
    View view;
    GoogleMap map;
    MapView mapView;
    TextView phoneNumber,address;
    Button btn_office_call,btn_email;

   SharedPreferences sharedPreferences;
   String country_name;
   Double lat, lon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        onBackPressed(view);

        sharedPreferences = getContext().getSharedPreferences(Constants.USER_PROFILE, Context.MODE_PRIVATE);
        country_name = sharedPreferences.getString(Constants.COUNTRY_NAME, Constants.NULL);

        btn_email = view.findViewById(R.id.btn_email);
        btn_office_call = view.findViewById(R.id.btn_office_call);
        phoneNumber = view.findViewById(R.id.phoneNubmerTextView);
        address = view.findViewById(R.id.addressTextView);
        mapView = view.findViewById(R.id.ad_map);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        if(country_name.equals("Bangladesh"))
        {
            phoneNumber.setText("+88 01789 266565");
            address.setText("Islampur Town, Jamalpur, Mymensingh, Bangladesh");
            lat = 25.12952;
            lon = 89.8918;
        }
        else
        {
            phoneNumber.setText("+92 305 8971088");
            address.setText("Office # 5, Street # 3, Chah hafiz wala, Kasim bela,Multan, Pakistan");
            lat = 30.181674;
            lon = 71.405057;
        }

        btn_office_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+923058971088"));
                startActivity(intent);

            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "taskmet@gmail.com"});

                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex) {

                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Sorry! we're facing some issues while sending email.")
                            .show();

                }


            }
        });



        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        int height = 160;
        int width = 145;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ad_map_markers);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDescriptor ad_marker = BitmapDescriptorFactory.fromBitmap(smallMarker);
        map.addMarker(new MarkerOptions()
                .position(new LatLng( lat,lon))
                .title("Taskmet Office")
                .icon(ad_marker));

        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng( lat,lon))
                .bearing(0)
                .tilt(45)
                .zoom(16)
                .build();

        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        drawCircle(new LatLng( lat,lon));

    }
    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x3078d6af);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        map.addCircle(circleOptions);

    }
    public void onBackPressed(View view){
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {

            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        } );

    }
}