package com.htc.eleven;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eleven_xianghtc.chatclientbasedonsocket.R;

public class MainActivity extends AppCompatActivity {

    private ScrollView content_scrollview;
    private TextView chat_content;
    private EditText sendMsg;

    private int Cnt = 0;
    private String linebreak = System.getProperty("line.separator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chat_content = (TextView) findViewById(R.id.chat_content);

        sendMsg = (EditText) findViewById(R.id.chat_message);

        content_scrollview = (ScrollView) findViewById(R.id.chat_content_scrollview);

        Button sendBtn = (Button) findViewById(R.id.send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat_content.append(sendMsg.getText().toString() + Cnt++ + linebreak);

                content_scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        content_scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
    }
}
