package com.htc.eleven;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private ScrollView content_scrollview;
    private TextView chat_content;
    private EditText sendMsg;
    private Button sendBtn;

    private EditText ip;

    //    private int Cnt = 0;
    private String linebreak = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("onCreate +++");
        chat_content = (TextView) findViewById(R.id.chat_content);

        sendMsg = (EditText) findViewById(R.id.chat_message);

        content_scrollview = (ScrollView) findViewById(R.id.chat_content_scrollview);

        sendBtn = (Button) findViewById(R.id.send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat_content.append("Client: " + sendMsg.getText().toString() + linebreak);

                try {
                    if (writer != null) {
                        writer.write("Myself: " + sendMsg.getText().toString());
                        writer.flush();
                        sendMsg.setText("");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /**
                 * below post function will make sure textView update to bottom automatically.
                 * */
                content_scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        content_scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });

        ip = (EditText) findViewById(R.id.ip_address);
        findViewById(R.id.connect_server).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect(ip.getText().toString());
            }
        });

        System.out.println("onCreate ---");

    }

    private Socket socket;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;

    private void connect(String ipAddress) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String line;

                try {
                    System.out.println("we are connecting to " + strings[0] + ": " + 12345);
                    socket = new Socket(strings[0], 12345);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    while ((line = reader.readLine()) != null) {
                        publishProgress(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

                chat_content.append("Server: " + values[0] + linebreak);

            }
        }.execute(ipAddress);
    }
}
