package com.example.gatewaytest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.albroco.barebonesdigest.DigestAuthentication;
import com.albroco.barebonesdigest.DigestChallengeResponse;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button send = findViewById(R.id.send_to_open);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncRequest().execute();

            }
        });
    }

    class AsyncRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg) {

            try {
                connect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private void connect() throws Exception {
        URL url = new URL("http://192.168.0.1:1025");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            DigestAuthentication auth = DigestAuthentication.fromResponse(connection);
            auth.username("admin").password("admin");

            if (!auth.canRespond()) {
                return;
            }

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(DigestChallengeResponse.HTTP_HEADER_AUTHORIZATION,
                    auth.getAuthorizationForRequest("GET", connection.getURL().getPath()));

        }
    }
}