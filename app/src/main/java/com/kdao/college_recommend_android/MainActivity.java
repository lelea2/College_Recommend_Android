package com.kdao.college_recommend_android;

import com.kdao.college_recommend_android.util.Config;
import com.kdao.college_recommend_android.util.PreferenceData;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner state_spinner;
    private Spinner tuition_spinner;
//    private Spinner median_salary;
    private Spinner public_work_hour_spinner;
    private ProgressDialog progressDialog;
    private EditText sat;
    private EditText act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        tuition_spinner = (Spinner) findViewById(R.id.tuition);
//        median_salary = (Spinner) findViewById(R.id.median_salary);
        public_work_hour_spinner = (Spinner) findViewById(R.id.public_work_hour);
        sat = (EditText) findViewById(R.id.sat);
        act = (EditText) findViewById(R.id.act);
        //Get data
        generateTuition();
//        generateSalary();
        generatePublicHour();
        getState();
    }

    private void generateTuition() {
        List<String> options = new ArrayList<String>();
        options.add(Config.TUITION1);
        options.add(Config.TUITION2);
        options.add(Config.TUITION3);
        options.add(Config.TUITION4);
        options.add(Config.TUITION5);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
//            state_spinner.setAdapter(dataAdapter);
        tuition_spinner.setPrompt("Select tuition range");
        tuition_spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                dataAdapter,
                R.layout.tuition_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                this));
    }

//    private void generateSalary() {
//
//    }

    private void generatePublicHour() {
        List<String> options = new ArrayList<String>();
        options.add(Config.PUBLIC_HOUR_1);
        options.add(Config.PUBLIC_HOUR_3);
        options.add(Config.PUBLIC_HOUR_2);
        options.add(Config.PUBLIC_HOUR_4);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
//            state_spinner.setAdapter(dataAdapter);
        public_work_hour_spinner.setPrompt("Select public services hour");
        public_work_hour_spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                dataAdapter,
                R.layout.hour_spinner_row_nothing_selected,
                // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                this));
    }

    //Loading to get state
    private void getState() {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://college-recommended.herokuapp.com/data/states");
                try {
                    try {
                        System.out.println(">>>> Get state successfully");
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }
                        return stringBuilder.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception uee) {
                    System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                try {
                    JSONArray arrayObj = null;
                    JSONParser jsonParser = new JSONParser();
                    arrayObj= (JSONArray) jsonParser.parse(result);
                    populateStates(arrayObj);
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

    //private function to popular state view
    private void populateStates(JSONArray arrayObj) {
        if (arrayObj.size() > 0) {
            List<String> statesList = new ArrayList<String>();
            for (int i = 0; i < arrayObj.size(); i++) {
                try {
                    JSONObject object = (JSONObject) arrayObj.get(i);
                    statesList.add(object.get("name").toString());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statesList);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
//            state_spinner.setAdapter(dataAdapter);
            state_spinner.setPrompt("Select your state");
            state_spinner.setAdapter(new NothingSelectedSpinnerAdapter(
                            dataAdapter,
                            R.layout.state_spinner_row_nothing_selected,
                            // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                            this));
        }
    }

    /**
     * Public function to handle button click
     * @param v
     */
    public void startHunting(View v) {
        String sat_score = sat.getText().toString();
        String act_score = act.getText().toString();
        String state = state_spinner.getSelectedItem().toString();
        String tuition = tuition_spinner.getSelectedItem().toString();
        String hour = public_work_hour_spinner.getSelectedItem().toString();
        if (isEmptyString(sat_score) || isEmptyString(act_score) || isEmptyString(state) || isEmptyString(tuition) || isEmptyString(hour)) {
            Toast.makeText(MainActivity.this, Config.INVALID_FORM, Toast.LENGTH_LONG).show();
        } else {
            PreferenceData.setPersonalPref(getApplicationContext(), sat_score, act_score, state, tuition, hour);
            Intent newIntent = new Intent(getApplicationContext(), RecommendActivity.class);
            startActivity(newIntent);
        }
    }


    /**
     * Checking for is empty string function
     * @param txt
     * @return
     */
    private static boolean isEmptyString(String txt){
        return (txt != null && txt.trim().length() > 0) ? false : true;
    }
}
