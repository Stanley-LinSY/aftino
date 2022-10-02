package com.sp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class DetailsFragment extends Fragment {
    private EditText realestatePrice;
    private EditText realestateAddress;
    private EditText realestateType;
    private EditText realestateSize;
    private EditText realestateAgent;
    private RadioGroup realestateStatus;
    private Button buttonSave;
    private RealEstateHelper helper = null;
    private String realestateID = "";

    private TextView location = null;
    private GPSTracker gpsTracker;
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private double myLatitude = 0.0d;
    private double myLongitude = 0.0d;

    byte[] realestateImage;
    private Button buttonImage;

    public DetailsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realestateID = null;
        helper = new RealEstateHelper(getContext());
        setHasOptionsMenu(true);

        getParentFragmentManager().setFragmentResultListener("listToDetailKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(String Key, Bundle bundle) {
                //receive bundle from ListFragment
                //get the passed restaurant name, address, tel and type from the bundle
                //and set to the respective widget
                realestateID = bundle.getString("id");
                if (realestateID != null) {
                    load();
                } else {
                    clear();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
        gpsTracker.stopUsingGPS();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        realestatePrice = view.findViewById(R.id.realestate_price);
        realestateAddress = view.findViewById(R.id.realestate_address);
        realestateType = view.findViewById(R.id.realestate_type);
        realestateSize = view.findViewById(R.id.realestate_size);
        realestateStatus = view.findViewById(R.id.realestate_status);
        realestateAgent = view.findViewById(R.id.realestate_agent);

        buttonSave = view.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(onSave);

//        buttonImage = view.findViewById(R.id.button_image);
//        buttonImage.setOnClickListener(onSelect);

        location = view.findViewById(R.id.Location);
        gpsTracker = new GPSTracker(getActivity());

        return view;
    }

    private void clear() {
        realestatePrice.setText("");
        realestateAddress.setText("");
        realestateType.setText("");
        realestateSize.setText("");
        realestateAgent.setText("");
        realestateStatus.clearCheck();
    }

    private void load() {
        Cursor c = helper.getById(realestateID);
        c.moveToFirst();
        realestatePrice.setText(helper.getRealEstatePrice(c));
        realestateAddress.setText(helper.getRealEstateAddress(c));
        realestateType.setText(helper.getRealEstateType(c));
        realestateSize.setText(helper.getRealEstateSize(c));
        realestateAgent.setText(helper.getRealEstateAgent(c));
        if (helper.getRealEstateStatus(c).equals("Rent")) {
            realestateStatus.check(R.id.rent);
        } else {
            realestateStatus.check(R.id.sale);
        }

        latitude = helper.getLatitude(c);
        longitude = helper.getLongitude(c);
        location.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));

//        imageViewCamera.setImageBitmap(getImageBitmap(helper.getRealestateImage(c)));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_option, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.get_location) {
            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                location.setText(String.valueOf(latitude) + ", " + String.valueOf(longitude));
                // \n is for new line
                Toast.makeText(getActivity().getApplicationContext(), "Real Estate Location is - \nLat: " + latitude
                        + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }
            return (true);
        } else if (item.getItemId() == R.id.show_map) {
            //get my current location
            myLatitude = gpsTracker.getLatitude();
            myLongitude = gpsTracker.getLongitude();

            Intent intent = new Intent(getActivity(), RealEstateMap.class);
            intent.putExtra("LATITUDE", latitude);
            intent.putExtra("LONGITUDE", longitude);
            intent.putExtra("MYLATITUDE", myLatitude);
            intent.putExtra("MYLONGITUDE", myLongitude);
            intent.putExtra("ADDRESS", realestateAddress.getText().toString());
            startActivity(intent);
            return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //To read data from realestatePrice EditText
            String priceStr = realestatePrice.getText().toString();
            String addrStr = realestateAddress.getText().toString();
            String typeStr = realestateType.getText().toString();
            String sizeStr = realestateSize.getText().toString();
            String agenStr = realestateAgent.getText().toString();
            String realStatus = "";

            //To read selection of realestateStatus RadioGroup
            switch (realestateStatus.getCheckedRadioButtonId()) {
                case R.id.rent:
                    realStatus = "Rent";
                    break;

                case R.id.sale:
                    realStatus = "Sale";
                    break;
            }
            if (realestateID == null) {
                helper.insert(priceStr, addrStr, typeStr, sizeStr, agenStr, realStatus, latitude, longitude);
            } else {
                helper.update(realestateID, priceStr, addrStr, typeStr, sizeStr, agenStr, realStatus, latitude, longitude);
            }
            //get the hosting activity's BottomNavigationView
            BottomNavigationView nav = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationView);
            //activate the BottomNavigatorView's list menu
            nav.setSelectedItemId(R.id.reallist);
        }
    };
}