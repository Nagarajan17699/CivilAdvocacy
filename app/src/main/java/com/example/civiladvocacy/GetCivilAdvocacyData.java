package com.example.civiladvocacy;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class GetCivilAdvocacyData {
    private static final String TAG = "GetCivilAdvocacyData";

    public static MainActivity main;
    private static RequestQueue queue;

    public  NormalizedInput normalizedInput;
    public  ArrayList<Offices> officesList = new ArrayList<>();
    public  ArrayList<Officials> officialsList = new ArrayList<>();
    public static final String googleAPI = "https://www.googleapis.com/civicinfo/v2/representatives";
    public static final String APIKey = "AIzaSyBe2VXgwzlGaZw8ldVRIYx09inAzy8Dbmc";

    public void downloadData(MainActivity mainAct, String address) {
        main = mainAct;
        queue = Volley.newRequestQueue(main);

        //Building URL
        Uri.Builder buildURL = Uri.parse(googleAPI).buildUpon();
        buildURL.appendQueryParameter("key", APIKey);
        buildURL.appendQueryParameter("address", address);
        String url = buildURL.build().toString();

        Response.Listener<JSONObject> listener = response -> {
         parseJSON(response.toString());
            Log.d(TAG, "downloadData: "+ response);
    };

        Response.ErrorListener error =
                error1 -> {
                    normalizedInput = null;
                    officesList = null;
                    officialsList = null;
                    Log.d(TAG, "downloadCivilData: data download error ..."+ error1.getMessage());
                };

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void parseJSON(String s) {

        try {
            JSONObject civilData = new JSONObject(s);
            JSONObject nInput = civilData.getJSONObject("normalizedInput");
            String line1 = nInput.getString("line1");
            String city = nInput.getString("city");
            String state = nInput.getString("state");
            String zip = nInput.getString("zip");

            normalizedInput = new NormalizedInput(line1, city, state, zip);
            Log.d(TAG, "parseJSON: "+ normalizedInput);

            JSONArray offc = civilData.getJSONArray("offices");
            for(int i=0;i<offc.length();i++){
                JSONObject offcObj = offc.getJSONObject(i);
                String name = offcObj.getString("name");

                JSONArray offcIndArr = offcObj.getJSONArray("officialIndices");
                ArrayList<Integer> al = new ArrayList<>();
                for(int j=0;j<offcIndArr.length();j++){
                    al.add(Integer.parseInt(offcIndArr.get(j).toString()));
                }
                Offices offices_temp = new Offices(name, al);
                officesList.add(offices_temp);
                Log.d(TAG, "parseJSON: "+ offices_temp );
            }

            JSONArray officials = civilData.getJSONArray("officials");
            for(int i=0;i<officials.length();i++){
                JSONObject jsonObject = officials.getJSONObject(i);
                String name = jsonObject.getString("name");

                HashMap<String,String> hm = new HashMap<>();

                if(jsonObject.has("address")) {

                    JSONArray addressArr = jsonObject.getJSONArray("address");
                    String line_1 = (addressArr.getJSONObject(0).has("line1")) ? addressArr.getJSONObject(0).getString("line1") : "";
                    String line_2 = (addressArr.getJSONObject(0).has("line2")) ? addressArr.getJSONObject(0).getString("line2") : "";
                    String line_3 = (addressArr.getJSONObject(0).has("line3")) ? addressArr.getJSONObject(0).getString("line3") : "";
                    String city_offc = (addressArr.getJSONObject(0).has("city")) ? addressArr.getJSONObject(0).getString("city") : "";
                    String state_offc = (addressArr.getJSONObject(0).has("state")) ? addressArr.getJSONObject(0).getString("state") : "";
                    String zip_offc = (addressArr.getJSONObject(0).has("zip")) ? addressArr.getJSONObject(0).getString("zip") : "";

                    hm.put("line1", line_1);
                    hm.put("line2", line_2);
                    hm.put("line3", line_3);
                    hm.put("city", city_offc);
                    hm.put("state", state_offc);
                    hm.put("zip", zip_offc);
                }

                String party = jsonObject.has("party") ? jsonObject.getString("party") : "";
                String phone = jsonObject.has("phones") ? jsonObject.getJSONArray("phones").get(0).toString() : "";
                String urls = jsonObject.has("urls") ? jsonObject.getJSONArray("urls").get(0).toString() : "";
                String emails = jsonObject.has("emails") ?jsonObject.getJSONArray("emails").get(0).toString() : "";
                String photoUrl = jsonObject.has("photoUrl") ? jsonObject.getString("photoUrl") :"";

                HashMap<String, String> hm2 = new HashMap<>();
                if(jsonObject.has("channels")) {
                    JSONArray channelsArr = jsonObject.getJSONArray("channels");
                    for (int k = 0; k < channelsArr.length(); k++) {
                        JSONObject channels = channelsArr.getJSONObject(k);
                        String type = channels.getString("type");
                        String id = channels.getString("id");
                        hm2.put(type, id);
                    }
                }

                officialsList.add(new Officials(name, hm, party, phone, urls, emails, photoUrl, hm2));
            }

            main.showCivilAdvocacies(normalizedInput, officesList, officialsList);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public  NormalizedInput getNormalizedInput() {
        return normalizedInput;
    }

    public  ArrayList<Offices> getOfficesList() {
        return officesList;
    }

    public  ArrayList<Officials> getOfficialsList() {
        return officialsList;
    }
}
