package com.chahinesoftwaresolutions.covidtracker;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


//import org.apache.http.client.methods.HttpGet;
//import com.android.volley.toolbox.HttpResponse;

public class Database extends AsyncTask<Void, Void, Void >{
    public String url;
    public boolean parseData=false;
    public boolean parseSellQty=false;
    public String data="";
    public int inventoryNum=0;
    public int compareOrder=0;
    public int egyptOrders=0;
    public String singleParsed;
    // public boolean secondEntry=false;


    Database(String data){
        this.url=data;


    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL test2 = new URL(url);
//            URLConnection connect2=test2.openConnection();
            //connect2.connect();
            //URL path = new URL(info);//The URL were using to parse the data
            //URLConnection connect = path.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) test2.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            if (parseData) {
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                JSONObject json=new JSONObject(data);
                if(inventoryNum==1) {
                  //  MainActivity.json=json;
                    MainActivity.data.add(data);
                }
                else if(inventoryNum==2)
                {
                    viewCountries.data.add(data);
                }

            }
        }catch(MalformedURLException x){

            //e.printStackTrace();
            Log.e("Connection issue", x.getMessage());
        } catch(IOException e){
            //e.printStackTrace();

            Log.e("Connection issue", e.getMessage());
            if(inventoryNum==2) {
                viewCountries.data.add("{\"error\":{\"message\":\"Country `Samoa` not found in JHU database\"}}");
            }
        } catch (Exception e) {

            Log.e("Error", e.getMessage());
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //if you want the data then set the data

    }
}
