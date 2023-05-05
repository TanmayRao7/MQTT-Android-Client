package com.tanmay.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SubscribeActivity extends AppCompatActivity {

    private EditText topicEditText;
    private Button subscribeButton;
    private TextView statusTextView;
    private TextView messagesTextView;
    private ScrollView messagesScrollView;

    private MqttClient sampleClient;

    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        button = findViewById(R.id.floatingActionButton2);
        topicEditText = findViewById(R.id.topicEditText);
        subscribeButton = findViewById(R.id.subscribeButton);
        statusTextView = findViewById(R.id.statusTextView);
        messagesTextView = findViewById(R.id.messagesTextView);
        messagesScrollView = findViewById(R.id.messagesScrollView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubscribeActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(topicEditText.getText().toString())){
                    topicEditText.setError("Enter a Topic First");
                }else {

                    subscribe();
                }
            }
        });



    }

    private void subscribe(){
        String broker = "tcp://broker.hivemq.com:1883";
        String clientId = MqttClient.generateClientId();
        MemoryPersistence persistence = new MemoryPersistence();
        String topic = topicEditText.getText().toString();

        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            Toast.makeText(SubscribeActivity.this, "Connecting to broker: " + broker, Toast.LENGTH_SHORT).show();
            sampleClient.connect(connOpts);
            Toast.makeText(SubscribeActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            sampleClient.subscribe(topic, 1);
            statusTextView.setText("Subscribed To Topic: "+ topic);
            Toast.makeText(SubscribeActivity.this, "Subscribed to topic: " + topic, Toast.LENGTH_SHORT).show();
            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Called when the connection to the server is lost
                    Toast.makeText(SubscribeActivity.this, "Connection is Lost", Toast.LENGTH_SHORT).show();
                    statusTextView.setText("Connection Lost !");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Called when a message is received on the subscribed topic
                    String content = new String(message.getPayload());
                    appendToMessagesTextView("Message received: " + content);

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Called when a message has been successfully delivered to the server
                }
            });
        } catch (MqttException me) {
            Log.i("reason112", String.valueOf(+me.getReasonCode()));
            Log.i("msg112", me.getMessage());
            Log.i("loc112", me.getLocalizedMessage());
            Log.i("cause112", String.valueOf(me.getCause()));
            Log.i("excep112", String.valueOf(me));
            me.printStackTrace();
        }
    }

    private void appendToMessagesTextView(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesTextView.append(message + "\n");
                messagesScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sampleClient != null && sampleClient.isConnected()) {
            try {
                sampleClient.disconnect();
            } catch (MqttException me) {
                Log.i("reason112", String.valueOf(+me.getReasonCode()));
                Log.i("msg112", me.getMessage());
                Log.i("loc112", me.getLocalizedMessage());
                Log.i("cause112", String.valueOf(me.getCause()));
                Log.i("excep112", String.valueOf(me));
                me.printStackTrace();
            }
        }
    }
}