package com.myapplicationdev.android.c302_p08_ps_secured_cloud_address_book2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ViewContactDetailsActivity extends AppCompatActivity {

    EditText etFirstName, etLastName, etMobile;
    Button btnUpdate, btnDelete;
    int contactId;
    String loginId, apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Intent loginIntent = getIntent();
        contactId = getIntent().getIntExtra("contact_id", 0);
        loginId = loginIntent.getStringExtra("loginId");
        apikey = loginIntent.getStringExtra("apikey");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginId", loginId);
        params.put("apikey", apikey);
//        params.put("id", contactId);

        String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/getContactDetails.php?id=";
        client.post(url + contactId, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String fName = response.getString("firstname");
                    String lName = response.getString("lastname");
                    String mobile = response.getString("mobile");

                    etFirstName.setText(fName);
                    etLastName.setText(lName);
                    etMobile.setText(mobile);
                } catch (JSONException e) {
                    Toast.makeText(ViewContactDetailsActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }

        });

        btnUpdate.setOnClickListener((View v) -> {
            updateDetails();
            Toast.makeText(ViewContactDetailsActivity.this,
                    "Contact information has been successfully updated.", Toast.LENGTH_SHORT).show();

        });

        btnDelete.setOnClickListener((View v) -> {
            deleteContact();
            Toast.makeText(ViewContactDetailsActivity.this,
                    "Contact information has been successfully deleted.", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Code for step 1 start
        Intent intent = getIntent();
        contactId = intent.getIntExtra("contact_id", -1);
    }


    void updateDetails() {
        String fName = etFirstName.getText().toString().trim();
        String lName = etLastName.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();

        if (fName.length() != 0 && lName.length() != 0 && mobile.length() != 0) {

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("loginId", loginId);
            params.put("apikey", apikey);
            params.put("id", contactId);
            params.put("FirstName", fName);
            params.put("LastName", lName);
            params.put("Mobile", mobile);

            client.post("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/updateContact.php", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        String message = response.getString("message");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(ViewContactDetailsActivity.this,
                                "Authentication failed", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Toast.makeText(ViewContactDetailsActivity.this,
                    "Check that all of the EditTexts are filled out.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void deleteContact() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("loginId", loginId);
        params.put("apikey", apikey);
        params.put("id", contactId);
        String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/deleteContact.php";
        client.post(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String message = response.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(ViewContactDetailsActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}