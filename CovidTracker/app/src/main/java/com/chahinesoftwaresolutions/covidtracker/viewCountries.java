package com.chahinesoftwaresolutions.covidtracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class viewCountries extends AppCompatActivity {
    private TextView mTextMessage;
    public Spinner mCountries;
    JSONObject json;
    public String mConfirmed;
    public String mRecovered;
    public TextView mConfirmedText;
    public TextView mRecoveredText;
    public TextView mDeathsText;
    public String mError;
    public String mDeaths;
    public static List<String> data;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.navigation_countries:
                    startActivity(new Intent(getApplicationContext(), viewCountries.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_countries);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_countries);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mCountries=findViewById(R.id.countries);
        mConfirmedText=findViewById(R.id.txtConfirmed);
        mDeathsText=findViewById(R.id.txtDeaths);
        mRecoveredText=findViewById(R.id.txtRecovered);
        mConfirmedText.setText("");
        mRecoveredText.setText("");
        mDeathsText.setText("");
        ArrayAdapter<CharSequence> countries;

        countries = ArrayAdapter.createFromResource(getApplicationContext(), R.array.countries, android.R.layout.simple_spinner_item);

        mCountries.setAdapter(countries);
        mCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data=new ArrayList<>();
                String country=mCountries.getSelectedItem().toString();
                if(country.equals("Select a country")) {
                    mConfirmedText.setText("");
                    mRecoveredText.setText("");
                    mDeathsText.setText("");
                }
                else {
                    Database getCountry=new Database("https://covid19.mathdro.id/api/countries/"+country);
                    getCountry.parseData=true;
                    getCountry.inventoryNum=2;
                    getCountry.execute();
                    do {
                        try {
                            Thread.sleep(5);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }while(data.isEmpty());
                    try {
                        mConfirmed = findValueJson("confirmed");
                        mRecovered = findValueJson("recovered");
                        mDeaths = findValueJson("deaths");
                        mConfirmedText.setText("Confirmed: " + mConfirmed);
                        mRecoveredText.setText("Recovered: " + mRecovered);
                        mDeathsText.setText("Deaths: " + mDeaths);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        try {
                            mConfirmedText.setText("");
                            mRecoveredText.setText("");
                            mDeathsText.setText("");
                            mError=findError("error", country);
                            Toast.makeText(getApplicationContext(), mError, Toast.LENGTH_LONG).show();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

}
    String findValueJson(String infoType) throws JSONException {
        String value;
        json=new JSONObject(data.get(0));
        json=json.getJSONObject(infoType);
        value=json.getString("value");
        return value;
    }
    String findError(String infoType, String country) throws JSONException {
        String value;
        json=new JSONObject("{\"error\":{\"message\":\"Country `"+country+"` not found in JHU database\"}}");
        json=json.getJSONObject(infoType);
        value=json.getString("message");
        return value;
    }
}