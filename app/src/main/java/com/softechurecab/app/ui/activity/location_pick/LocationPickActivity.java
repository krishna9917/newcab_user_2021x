package com.softechurecab.app.ui.activity.location_pick;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.softechurecab.app.BuildConfig;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.common.Constants;
import com.softechurecab.app.common.InfoWindowData;
import com.softechurecab.app.common.RecyclerItemClickListener;
import com.softechurecab.app.data.network.model.AddressResponse;
import com.softechurecab.app.data.network.model.UserAddress;
import com.softechurecab.app.ui.adapter.PlacesAutoCompleteAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.softechurecab.app.MvpApplication.RIDE_REQUEST;
import static com.softechurecab.app.common.Constants.LocationActions.SELECT_DESTINATION;
import static com.softechurecab.app.common.Constants.LocationActions.SELECT_SOURCE;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.DEST_ADD;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.DEST_LAT;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.DEST_LONG;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.SRC_ADD;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.SRC_LAT;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.SRC_LONG;

public class LocationPickActivity extends BaseActivity
        implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationPickIView {

    private static final LatLngBounds BOUNDS_BRASIL = new LatLngBounds(new LatLng(-14.235, -51.9253), new LatLng(-14.235, -51.9253));
    private Location mLastKnownLocation;
    protected GoogleApiClient mGoogleApiClient;
    private InfoWindowData destinationLeg;

    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.source)
    EditText source;
    @BindView(R.id.destination)
    EditText destination;
    @BindView(R.id.destination_layout)
    LinearLayout destinationLayout;
    @BindView(R.id.home_address_layout)
    LinearLayout homeAddressLayout;
    @BindView(R.id.work_address_layout)
    LinearLayout workAddressLayout;
    @BindView(R.id.home_address)
    TextView homeAddress;
    @BindView(R.id.work_address)
    TextView workAddress;
    @BindView(R.id.locations_rv)
    RecyclerView locationsRv;
    @BindView(R.id.location_bs_layout)
    CardView locationBsLayout;
    @BindView(R.id.dd)
    CoordinatorLayout dd;
    boolean isEnableIdle = false;
    @BindView(R.id.llSource)
    LinearLayout llSource;

    private boolean isLauchingActivity = true;
    private boolean isLocationRvClick = false;
    private boolean isSettingLocationClick = false;
    private boolean mLocationPermissionGranted;
    private GoogleMap mGoogleMap;
    private String s_address;
    private Double s_latitude;
    private Double s_longitude;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Boolean isEditable = true;
    private UserAddress home, work = null;
    private LocationPickPresenter<LocationPickActivity> presenter = new LocationPickPresenter<>();
    private EditText selectedEditText;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    //Base on action we are show/hide view and setResults
    private String actionName = SELECT_SOURCE;
    private PlacesClient mPlacesClient;
    private AutocompleteSessionToken sessionToken = AutocompleteSessionToken.newInstance();

    private final long REQUEST_PLACES_DELAY = 400; // delay de x segundos
    private final long REQUEST_PLACES_CHARACTER_LIMIT = 7; // delay de x segundos
    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

        }
    };

    private List<Place.Field> filterType = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS
    );

    private TextWatcher filterTextWatcher = new TextWatcher() {

        private boolean userIsTyping;

        public void afterTextChanged(Editable editable) {
            // se o usuario esta digitando ele faz o request, caso contrario ele não faz
            //requestPlacesByCharactersLimit(editable, userIsTyping);

             requestPlacesByDelay(editable);

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            userIsTyping = after > count; // verifica se o usuário esta digitando ou apagando o texto
            //Log.w("MG_TAG", "BEFORE TEXTO  STR :" + s + " START : " + start + " COUNT : " + count + " AFTER : " + after);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userIsTyping = before < count; // verifica se o usuário esta digitando ou apagando o texto
            //Log.w("MG_TAG", "ON TEXTO  STR :" + s + " START : " + start + " COUNT : " + count + " BEFORE : " + before);
        }

    };
    private HashMap<String, Object> ORIGINAL_RIDE_REQUEST;

    private void requestPlacesByDelay(Editable editable) {
        timer.cancel();
        timerTask.cancel();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {

                    if (isEditable && !editable.toString().trim().isEmpty() ) {
//                        Log.d("vndsfmv","sds: " + "ok");
                        locationsRv.setVisibility(View.VISIBLE);

                        // Atualizar lista de predicoes com o texto do autocomplete
                        mAutoCompleteAdapter.getAutoCompletePredictions(editable.toString(), sessionToken);
                        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        isEditable=false;
                    } else {
                        locationsRv.setVisibility(View.GONE);
                        isEditable=true;
                    }

                });
            }
        };
        timer.schedule(timerTask, REQUEST_PLACES_DELAY);
    }


    private void requestPlacesByCharactersLimit(Editable s, boolean userIsTyping) {
        // s.length() % REQUEST_PLACES_CHARACTER_LIMIT == 0, pesquisa de intervalo em intervalo de 5 caracateres por exemplo
        if (isEditable && userIsTyping && s.length() % REQUEST_PLACES_CHARACTER_LIMIT == 0) {
            if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                Log.w("MG_TAG", "MAKING PLACES REQUEST");
                locationsRv.setVisibility(View.VISIBLE);
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else if (!mGoogleApiClient.isConnected()) {
                Log.e("ERROR", "API_NOT_CONNECTED");
            }
        }
        if (s.toString().equals("")) {
            locationsRv.setVisibility(View.GONE);
        }
        Log.d("NOTIFICAÇÃO TEXTO", "RRRRRRRR TEXT CHANGE");
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_location_pick;
    }

    @Override
    public void initView() {
        isLauchingActivity = true;
        buildGoogleApiClient();
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configura o google places
        Places.initialize(getApplicationContext(), BuildConfig.google_map_keys);
        mPlacesClient = Places.createClient(LocationPickActivity.this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mBottomSheetBehavior = BottomSheetBehavior.from(locationBsLayout);
        mBottomSheetBehavior.setDraggable(false);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        //capiturando latitude e longitude local do usuário com a Fusedlocation (wifi, gps, e provider)
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    mLastKnownLocation = task.getResult();
                    //criando lat e long local, modo dinamico
                    LatLngBounds latLongLocal = new LatLngBounds(new LatLng(
                            mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()
                    ), new LatLng(
                            mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()
                    ));

                    Log.d("sdfns","sds: " + mPlacesClient);

                    //passando apenas resultados do place local do usuário
                    mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.list_item_location, mPlacesClient, latLongLocal);

                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
                    locationsRv.setLayoutManager(mLinearLayoutManager);
                    locationsRv.setAdapter(mAutoCompleteAdapter);
                } else {
                    Log.d("Map", "Current location is null. Using defaults.");
                    mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.list_item_location, mPlacesClient, BOUNDS_BRASIL);
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }


        source.addTextChangedListener(filterTextWatcher);
        destination.addTextChangedListener(filterTextWatcher);

        source.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) selectedEditText = source;
        });

        destination.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) selectedEditText = destination;
        });

        destination.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (RIDE_REQUEST.containsKey(SRC_ADD) && RIDE_REQUEST.containsKey(DEST_ADD)) {
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }
                return true;
            }
            return false;
        });


        ORIGINAL_RIDE_REQUEST = new HashMap<>(RIDE_REQUEST);

        if(RIDE_REQUEST.containsKey(SRC_ADD))
        {
            if(TextUtils.isEmpty(Objects.requireNonNull(RIDE_REQUEST.get(SRC_ADD)).toString()))
            {
                source.setText("");
            }else if(!source.getText().toString().equals(String.valueOf(RIDE_REQUEST.get(SRC_ADD))))
            {
                source.setText(String.valueOf(RIDE_REQUEST.get(SRC_ADD)));
            }
        }else
        {
            source.setText("");
        }

        if(RIDE_REQUEST.containsKey(DEST_ADD))
        {
            if(TextUtils.isEmpty(Objects.requireNonNull(RIDE_REQUEST.get(DEST_ADD)).toString()))
            {
                destination.setText("");
            }else if(!destination.getText().toString().equals(String.valueOf(RIDE_REQUEST.get(DEST_ADD))))
            {
                Log.d("Problem", "initView: ");
                destination.setText(String.valueOf(RIDE_REQUEST.get(DEST_ADD)));

            }
        }else
        {
            destination.setText("");
        }




//        source.setText(RIDE_REQUEST.containsKey(SRC_ADD) ? TextUtils.isEmpty(Objects.requireNonNull(RIDE_REQUEST.get(SRC_ADD)).toString()) ? "" : String.valueOf(RIDE_REQUEST.get(SRC_ADD)) : "");
//
//        destination.setText(RIDE_REQUEST.containsKey(DEST_ADD) ? TextUtils.isEmpty(Objects.requireNonNull(RIDE_REQUEST.get(DEST_ADD)).toString()) ? "" : String.valueOf(RIDE_REQUEST.get(DEST_ADD)) : "");


        locationsRv.addOnItemTouchListener(new RecyclerItemClickListener(this, (view, position) -> {
                    if (mAutoCompleteAdapter.getItemCount() == 0) return;
                    final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                    Log.i("LocationPickActivity", "Get place details for place id: " + item.placeId + " address: " + item.address);
                    fetchPlace(item.placeId.toString(), response -> {
                        isLocationRvClick = true;
                        isSettingLocationClick = true;
                        Log.d("Problem", "initView: ");
                        setLocationText(response.getPlace().getAddress(), response.getPlace().getLatLng(), isLocationRvClick, isSettingLocationClick);
                    });
                })
        );

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            actionName = bundle.getString("actionName", SELECT_SOURCE);

            if (!TextUtils.isEmpty(actionName) && actionName.equalsIgnoreCase(SELECT_SOURCE)) {
                destination.setCursorVisible(false);
                source.setCursorVisible(true);
                source.requestFocus();
                selectedEditText = source;
                Log.d("Problem", "initView:1 ");
                setLocationText(null, null, isLocationRvClick, isSettingLocationClick);
            } else if (!TextUtils.isEmpty(actionName) && actionName.equalsIgnoreCase(Constants.LocationActions.SELECT_DESTINATION)) {
                source.setCursorVisible(false);
                destination.setCursorVisible(true);
                destination.requestFocus();
                selectedEditText = destination;
                Log.d("Problem", "initView:2");
                setLocationText(null, null, isLocationRvClick, isSettingLocationClick);
            } else if (!TextUtils.isEmpty(actionName) && actionName.equals(Constants.LocationActions.CHANGE_DESTINATION)) {
                llSource.setVisibility(View.GONE);
                source.setHint(getString(R.string.select_location));
                selectedEditText = destination;
            } else if (!TextUtils.isEmpty(actionName) && (actionName.equals(Constants.LocationActions.SELECT_HOME) || actionName.equals(Constants.LocationActions.SELECT_WORK))) {
                destinationLayout.setVisibility(View.GONE);
                selectedEditText = source;
                source.setText("");
                source.setHint(getString(R.string.select_location));
            } else {
                destinationLayout.setVisibility(View.VISIBLE);
                llSource.setVisibility(View.VISIBLE);
                source.setHint(getString(R.string.pickup_location));
                selectedEditText = source;
            }

        }



        if(getIntent().getStringExtra("type")!=null )
        {
            if(!source.getText().toString().equals(getIntent().getStringExtra("src")))
            {
                source.requestFocus( );
                source.setText(getIntent().getStringExtra("src"));
            }


            if(!destination.getText().toString().equals(getIntent().getStringExtra("des")))
            {
                destination.requestFocus();
                destination.setText(getIntent().getStringExtra("des"));
            }

        }

        presenter.address();
    }

    /**
     * Faz chamada ao google places api para recuperar place detail
     *
     * @param placeId  ID do local
     * @param response On success listener callback
     */
    private void fetchPlace(String placeId, OnSuccessListener<FetchPlaceResponse> response) {

        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest
                .builder(placeId, filterType)
                .setSessionToken(sessionToken)
                .build();

        // Fetch place from places api and add Listners for Success an Error
        mPlacesClient.fetchPlace(request)
                .addOnSuccessListener(response)
                .addOnFailureListener((exception) -> {
                    Toast.makeText(getApplicationContext(), "Lieu introuvable", Toast.LENGTH_SHORT).show();
                    Log.e("[PLACE DETAIL]", "Lieu introuvable: " + exception.getMessage());
                });
    }

    Marker srcMarker,decMarker;
    private void setLocationText(String address, LatLng latLng, boolean isLocationRvClick, boolean isSettingLocationClick) {
        if (address != null && latLng != null) {
            selectedEditText.setText(address);

            Log.d("Problem", "setLocationText: ");
            if (selectedEditText.getTag().equals("source")) {
                s_address = address;
                s_latitude = latLng.latitude;
                s_longitude = latLng.longitude;
                RIDE_REQUEST.put(SRC_ADD, address);
                RIDE_REQUEST.put(SRC_LAT, latLng.latitude);
                RIDE_REQUEST.put(SRC_LONG, latLng.longitude);
                if (srcMarker!=null)
                    srcMarker.remove();
                int   serviceIcon = R.drawable.ic_pin;

               /* srcMarker = mGoogleMap.addMarker(new MarkerOptions()

                        .position(latLng)
                        .title(s_address)
                         .icon(BitmapDescriptorFactory.fromResource(serviceIcon)));

       */
               if(!isEditable)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
               else  locationsRv.setVisibility(View.GONE);


            }


            if (selectedEditText.getTag().equals("destination")) {
                RIDE_REQUEST.put(DEST_ADD, address);
                RIDE_REQUEST.put(DEST_LAT, latLng.latitude);
                RIDE_REQUEST.put(DEST_LONG, latLng.longitude);
                int   serviceIcon = R.drawable.ic_pin;
                if (decMarker!=null)
                    decMarker.remove();

            /*    decMarker = mGoogleMap.addMarker(new MarkerOptions()

                        .position(latLng)
                        .title(address)
                        .icon(BitmapDescriptorFactory.fromResource(serviceIcon)));

        */

                if(!isEnableIdle)
                {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                }else
                {
                    locationsRv.setVisibility(View.GONE);
                }

       /* if (source.getText().length()>2&&!TextUtils.isEmpty(""+RIDE_REQUEST.get(SRC_ADD))){
            callDone();
        }

*/

            }

//            if (RIDE_REQUEST.containsKey(SRC_ADD) && RIDE_REQUEST.containsKey(DEST_ADD)) {
//                setResult(Activity.RESULT_OK, new Intent());
//                finish();
//            }

            if (isSettingLocationClick) {
                hideKeyboard();
                locationsRv.setVisibility(View.GONE);
            }

        } else {
            selectedEditText.setText("");
            locationsRv.setVisibility(View.GONE);


            if (selectedEditText.getTag().equals("source")) {
                RIDE_REQUEST.remove(SRC_ADD);
                RIDE_REQUEST.remove(SRC_LAT);
                RIDE_REQUEST.remove(SRC_LONG);
            }
            if (selectedEditText.getTag().equals("destination")) {

                RIDE_REQUEST.remove(DEST_ADD);
                RIDE_REQUEST.remove(DEST_LAT);
                RIDE_REQUEST.remove(DEST_LONG);
            }
        }

        if (isSettingLocationClick) {
            hideKeyboard();
            locationsRv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            Log.v("Google API", "Connecting");
            mGoogleApiClient.connect();
        }
    }
    private Bitmap getMarkerBitmapFromView() {

        View mView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.map_custom_infowindow, null);

        TextView tvEtaVal = mView.findViewById(R.id.tvEstimatedFare);
        String arrivalTime = destinationLeg.getArrival_time();
        if (arrivalTime.contains("hours")) arrivalTime = arrivalTime.replace("hours", "h\n");
        else if (arrivalTime.contains("hour")) arrivalTime = arrivalTime.replace("hour", "h\n");
        if (arrivalTime.contains("mins")) arrivalTime = arrivalTime.replace("mins", "min");
        tvEtaVal.setText(arrivalTime);
        mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
        mView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(mView.getMeasuredWidth(),
                mView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = mView.getBackground();
        if (drawable != null) drawable.draw(canvas);
        mView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                // TODO Miguel this line was removed, unecessary, because new LocationServices.API has this implementation
                //.addApi(Places.GEO_DATA_API)
                .build();
    }

    boolean focous=false;
    @OnClick({R.id.source, R.id.destination, R.id.reset_source, R.id.reset_destination, R.id.home_address_layout, R.id.work_address_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.source:
                break;
            case R.id.destination:
                break;
            case R.id.reset_source:
                selectedEditText = source;
                isEnableIdle=false;
                source.requestFocus();
                Log.d("Problem", "initView:3");
                setLocationText(null, null, isLocationRvClick, isSettingLocationClick);
                break;
            case R.id.reset_destination:
                destination.requestFocus();
                isEnableIdle=false;
                selectedEditText = destination;
                Log.d("Problem", "initView:4");
                setLocationText(null, null, isLocationRvClick, isSettingLocationClick);
                break;
            case R.id.home_address_layout:
                if (home != null)
                    Log.d("Problem", "initView:5");
                    setLocationText(home.getAddress(), new LatLng(home.getLatitude(), home.getLongitude()), isLocationRvClick, isSettingLocationClick);
                break;
            case R.id.work_address_layout:
                if (work != null)
                    setLocationText(work.getAddress(),
                            new LatLng(work.getLatitude(), work.getLongitude()),
                            isLocationRvClick, isSettingLocationClick);
                break;
        }
    }

    @Override
    public void onCameraIdle() {
        try {
            CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
            isEnableIdle=true;
            if (isEnableIdle) {
                String address = getAddress(cameraPosition.target);
                System.out.println("onCameraIdle " + address);
                hideKeyboard();
                setLocationText(address, cameraPosition.target, isLocationRvClick, isSettingLocationClick);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraMove() {
        System.out.println("LocationPickActivity.onCameraMove");
        hideKeyboard();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            //      Google map custom style...
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        } catch (Resources.NotFoundException e) {
            Log.d("Map:Style", "Can't find style. Error: ");
        }
        this.mGoogleMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    public void fetchCurrentPlace(OnCompleteListener<FindCurrentPlaceResponse> completeListener, OnFailureListener failureListener) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest
                .builder(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG))
                .build();

        // Call findCurrentPlace and handle the response
        mPlacesClient.findCurrentPlace(request).addOnCompleteListener(completeListener).addOnFailureListener(failureListener);
    }

    void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, task -> {
                    mLastKnownLocation = task.getResult();
                    if(mLastKnownLocation!=null)
                        moveCamera(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Move camera do no mapa da google para uma determinada localizacao
     *
     * @param latLng Localizacao
     */
    private void moveCamera(LatLng latLng) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }


    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mLocationPermissionGranted = true;
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_LOCATION);
    }

    private void updateLocationUI() {
        if (mGoogleMap == null) return;
        try {
            if (mLocationPermissionGranted) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.setOnCameraMoveListener(this);
                mGoogleMap.setOnCameraIdleListener(this);
            } else {
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == REQUEST_ACCESS_FINE_LOCATION)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                updateLocationUI();
                getDeviceLocation();
            }
    }

    private void initCameraToSourceOrDestination(Location location) {
        String lat;
        String lng;
        switch (actionName) {
            case SELECT_SOURCE:
                lat = String.valueOf(ORIGINAL_RIDE_REQUEST.get(SRC_LAT));
                lng = String.valueOf(ORIGINAL_RIDE_REQUEST.get(SRC_LONG));
                if (!lat.equals("null")) {
                    moveCamera(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
                    break;
                }
            case SELECT_DESTINATION:
                lat = String.valueOf(ORIGINAL_RIDE_REQUEST.get(SRC_LAT));
                lng = String.valueOf(ORIGINAL_RIDE_REQUEST.get(SRC_LONG));
                if (!lat.equals("null")) {
                    moveCamera(new LatLng(Double.valueOf(lat), Double.valueOf(lng)));
                    break;
                }
            default:
                moveCamera(new LatLng(location.getLatitude(), location.getLongitude()));
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, "API_NOT_CONNECTED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getMenuInflater().inflate(R.menu.location_pick_menu, menu);
        return true;
    }

    public void done(LatLng latLng) {
        if (RIDE_REQUEST.containsKey(SRC_ADD) && RIDE_REQUEST.containsKey(DEST_ADD)) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        } else if (RIDE_REQUEST.containsKey(SRC_ADD) && !RIDE_REQUEST.containsKey(DEST_ADD)) {
            String address = getAddress(latLng);
            RIDE_REQUEST.put(DEST_ADD, address);
            RIDE_REQUEST.put(DEST_LAT, latLng.latitude);
            RIDE_REQUEST.put(DEST_LONG, latLng.longitude);
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        } else if (!RIDE_REQUEST.containsKey(SRC_ADD) && RIDE_REQUEST.containsKey(DEST_ADD)) {
            String address = getAddress(latLng);
            RIDE_REQUEST.put(SRC_ADD, address);
            RIDE_REQUEST.put(SRC_LAT, latLng.latitude);
            RIDE_REQUEST.put(SRC_LONG, latLng.longitude);
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        } else if (!RIDE_REQUEST.containsKey(SRC_ADD) && !RIDE_REQUEST.containsKey(DEST_ADD)) {
            showAlert(getString(R.string.note), getString(R.string.note_description));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                callDone();
                return true;
            case android.R.id.home:
                RIDE_REQUEST = ORIGINAL_RIDE_REQUEST;
                setResult(Activity.RESULT_OK, new Intent());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void callDone(){
        if (!TextUtils.isEmpty(actionName) && actionName.equals(Constants.LocationActions.SELECT_HOME) || actionName.equals(Constants.LocationActions.SELECT_WORK)) {
            Intent intent = new Intent();
            intent.putExtra(SRC_ADD, s_address);
            intent.putExtra(SRC_LAT, s_latitude);
            intent.putExtra(SRC_LONG, s_longitude);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {

            if (mLastKnownLocation != null) {

                LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                if (RIDE_REQUEST.containsKey(SRC_ADD) && RIDE_REQUEST.containsKey(DEST_ADD)) {
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                } else if (RIDE_REQUEST.containsKey(SRC_ADD) && !RIDE_REQUEST.containsKey(DEST_ADD)) {
                    String address = getAddress(latLng);
                    RIDE_REQUEST.put(DEST_ADD, address);
                    RIDE_REQUEST.put(DEST_LAT, latLng.latitude);
                    RIDE_REQUEST.put(DEST_LONG, latLng.longitude);
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                } else if (!RIDE_REQUEST.containsKey(SRC_ADD) && RIDE_REQUEST.containsKey(DEST_ADD)) {
                    String address = getAddress(latLng);
                    RIDE_REQUEST.put(SRC_ADD, address);
                    RIDE_REQUEST.put(SRC_LAT, latLng.latitude);
                    RIDE_REQUEST.put(SRC_LONG, latLng.longitude);
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                } else if (!RIDE_REQUEST.containsKey(SRC_ADD) && !RIDE_REQUEST.containsKey(DEST_ADD)) {
                    showAlert("Alert!", "Your journey is incomplete. Please choose a location from the list of suggestions as you type.");
                }


            }
        }
    }
    /**
     * Exibe alert dialog com a devida aparencia do app
     *
     * @param title   Titulo
     * @param message Messagem
     */
    private void showAlert(String title, String message) {
        hideKeyboard();
        AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogCustom)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, null)
                .show();
        TextView messageTextView = dialog.findViewById(android.R.id.message);
        if (messageTextView != null)
            messageTextView.setTypeface(ResourcesCompat.getFont(this, R.font.clanpro_book));
    }

    @Override
    public void onSuccess(AddressResponse address) {
        if (address.getHome().isEmpty())
            homeAddressLayout.setVisibility(View.GONE);
        else {
            home = address.getHome().get(address.getHome().size() - 1);
            homeAddress.setText(home.getAddress());
            homeAddressLayout.setVisibility(View.VISIBLE);
        }
        if (address.getWork().isEmpty()) workAddressLayout.setVisibility(View.GONE);
        else {
            work = address.getWork().get(address.getWork().size() - 1);
            workAddress.setText(work.getAddress());
            workAddressLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}