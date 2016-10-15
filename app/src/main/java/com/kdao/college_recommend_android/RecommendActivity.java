package com.kdao.college_recommend_android;

import com.kdao.college_recommend_android.data.College;
import com.kdao.college_recommend_android.util.Config;
import com.kdao.college_recommend_android.util.PreferenceData;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class RecommendActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ListView collegeList;
    private List<College> colleges = new ArrayList<College>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        getData();
    }

    private int getPublicHour() {
        String hour = PreferenceData.getVolunteerHourRange(getApplicationContext());
        if (hour.equals(Config.PUBLIC_HOUR_1)) {
            return 1;
        } else if (hour.equals(Config.PUBLIC_HOUR_2)) {
            return 2;
        } else if (hour.equals(Config.PUBLIC_HOUR_3)) {
            return 3;
        } else {
            return 4;
        }
    }

    private int getTuition() {
        String tuition = PreferenceData.getTuitionRange(getApplicationContext());
        if (tuition.equals(Config.TUITION1)) {
            return 15000;
        } else if (tuition.equals(Config.TUITION2)) {
            return 20000;
        } else if (tuition.equals(Config.TUITION3)) {
            return 30000;
        } else if (tuition.equals(Config.TUITION4)) {
            return 40000;
        } else {
            return 70000;
        }
    }


    //From service
//            var sat_score = parseInt(params.sat_score, 10);
//            var act_score = parseInt(params.act_score, 10);
//            var median_salary = parseInt(params.median_salary, 10);
//            var public_service_hour = parseInt(params.public_service_hour, 10);
//            var location = params.location;
//            var tuition = parseInt(params.tuition, 10);
    private String getQuery() {
        String sat_score = PreferenceData.getSatScore(getApplicationContext());
        String act_score = PreferenceData.getActScore(getApplicationContext());
        String location = PreferenceData.getState(getApplicationContext());
        int hour = getPublicHour();
        int tuition = getTuition();
        return "?sat_score=" + sat_score
                + "&act_score=" + act_score
                + "&location=" + location
                + "&hour=" + hour
                + "&tuition=" + tuition;
    }

    private void populateListView() {
        ArrayAdapter<College> adapter = new MyListAdapter();
        collegeList = (ListView) findViewById(R.id.recommendList);
        collegeList.setAdapter(adapter);
    }

    /**
     * Helper function to get college
     */
    public void getData() {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(RecommendActivity.this, "", "Get recommend...");
            }

            @Override
            protected String doInBackground(String... params) {
                System.out.println("http://college-recommended.herokuapp.com/recommend/" + getQuery());
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://college-recommended.herokuapp.com/recommend/" + getQuery());
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
                    populateCollege(arrayObj);
                    populateListView();
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute();
    }

//    {
//        acceptance_rate: 72.9,
//            graduation_rate: 55,
//            student_falculty_ratio: 23,
//            total_student: 36739,
//            tuition: 8959,
//            sat_score: 1510,
//            median_salary: 41000,
//            act_score: 22,
//            id: "566",
//            ranking: "N/A",
//            createdAt: "2016-10-09T19:41:33.610Z",
//            location: "San Marcos, Texas",
//            img: "Texas_State_University-San_Marcos_213524.png",
//            name: "Texas State University"
//    }
    private void populateCollege(JSONArray arrObj) {
        if (arrObj.size() > 0) {
            System.out.println(">>>>>>> Comments count:" + arrObj.size());
            for (int i = 0; i < arrObj.size(); i++) {
                //(String name, String image, String location, String tuition, String acceptance_rate, String ratio)
                try {
                    JSONObject object = (JSONObject) arrObj.get(i);
                    College college = new College(object.get("name").toString(),
                            object.get("img").toString(), object.get("location").toString(),
                            object.get("tuition").toString(), object.get("acceptance_rate").toString(),
                            object.get("student_falculty_ratio").toString(), object.get("total_student").toString());
                    colleges.add(college);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    private class MyListAdapter extends ArrayAdapter<College> {
        public MyListAdapter() {
            super(RecommendActivity.this, R.layout.college_item, colleges);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.college_item, parent, false);
            }

            // Find the car to work with.
            College college = colleges.get(position);
            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText(college.getName());
            TextView location = (TextView) itemView.findViewById(R.id.location);
            location.setText(college.getLocation());
            TextView tuitition = (TextView) itemView.findViewById(R.id.tuition);
            tuitition.setText("Tuition: $" + college.getTuition());
            TextView acceptance_rate = (TextView) itemView.findViewById(R.id.acceptance_rate);
            acceptance_rate.setText("Acceptance Rate:" + college.getAcceptance_rate() + "%");
            TextView ratio = (TextView) itemView.findViewById(R.id.ratio);
            ratio.setText("Falculty ratio: " + college.getRatio());
            return itemView;
        }
    }

    /**
     * Function to navigate back home
     * @param v
     */
    public void navigateToMainActivity(View v) {
        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(newIntent);
    }
}
