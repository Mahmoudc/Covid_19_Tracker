package com.chahinesoftwaresolutions.covidtracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static JSONObject json;
    public static List<String> data;
    public String mConfirmed;
    public String mRecovered;
    public TextView mConfirmedText;
    public TextView mRecoveredText;
    public TextView mDeathsText;
    public String mDeaths;
    private TextView mTextMessage;

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
        data=new ArrayList<>();
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mConfirmedText=findViewById(R.id.txtConfirmed);
        mDeathsText=findViewById(R.id.txtDeaths);
        mRecoveredText=findViewById(R.id.txtRecovered);
        Database covidTracker=new Database("https://covid19.mathdro.id/api");
        covidTracker.parseData=true;
        covidTracker.inventoryNum=1;
        covidTracker.execute();
        do {
            try {
                Thread.sleep(5);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }while(data.isEmpty());
       // Toast.makeText(getApplicationContext(), data.get(0), Toast.LENGTH_LONG).show();
        try {
            mConfirmed=findValueJson("confirmed");
            mRecovered=findValueJson("recovered");
            mDeaths=findValueJson("deaths");
            mConfirmedText.setText("Confirmed: "+mConfirmed);
            mRecoveredText.setText("Recovered: "+mRecovered);
            mDeathsText.setText("Deaths: "+mDeaths);

        } catch (JSONException e) {
            e.printStackTrace();
        }

      //  Toast.makeText(getApplicationContext(), mConfirmed, Toast.LENGTH_LONG).show();

    }
String findValueJson(String infoType) throws JSONException {
        String value;
        json=new JSONObject(data.get(0));
        json=json.getJSONObject(infoType);
        value=json.getString("value");
        return value;
}

}