package com.tanmay.helloworld;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {


    private EditText edtBroker, edtTopic, edtMessage;
    private Button btnConnect, btnPublish;
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;
    private String broker, clientId, topic, message;
    MemoryPersistence persistence;

    FloatingActionButton subscribeFloat;

    boolean isValid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtBroker = findViewById(R.id.et_broker);
        edtTopic = findViewById(R.id.et_topic);
        edtMessage = findViewById(R.id.et_content);

        btnConnect = findViewById(R.id.btn_connect);
        btnPublish = findViewById(R.id.btn_publish);
        subscribeFloat = findViewById(R.id.floatingActionButton);

        persistence = new MemoryPersistence();


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(TextUtils.isEmpty(edtBroker.getText().toString())){
                        edtBroker.setError("Enter a broker url \n For example: tcp://broker.hivemq.com:1883");
                    }else {
                        connectToBroker();
                    }

            }

        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edtTopic.getText().toString()) || TextUtils.isEmpty(edtMessage.getText().toString())){
                    edtTopic.setError("Enter a Topic");
                    edtMessage.setError("Enter a Message");
                }else {
                    publishMessage();
                }

            }
        });

        subscribeFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SubscribeActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    public void connectToBroker(){

        broker = edtBroker.getText().toString();
        clientId = "client123/Tanmay";

        try {
            mqttClient = new MqttClient(broker, clientId,persistence);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Toast.makeText(MainActivity.this, "Connection lost", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Toast.makeText(MainActivity.this, "Message arrived: " + message.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Toast.makeText(MainActivity.this, "Message Delivered", Toast.LENGTH_SHORT).show();
                }
            });

            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setKeepAliveInterval(120);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttClient.connect(mqttConnectOptions);


            Toast.makeText(this, "Client Connected", Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void publishMessage(){



        if (mqttClient == null || !mqttClient.isConnected()) {
            Toast.makeText(MainActivity.this, "Client not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        topic = edtTopic.getText().toString();
        message = edtMessage.getText().toString();

        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttClient.publish(topic, mqttMessage);
            Toast.makeText(this, "Message Delivered To Topic: "+ topic, Toast.LENGTH_SHORT).show();

            //Keep on reconnecting the client Until message gets delivered
            broker = edtBroker.getText().toString();
            clientId = "client123/Tanmay";

            try {
                mqttClient = new MqttClient(broker, clientId,persistence);
                mqttClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Toast.makeText(MainActivity.this, "Connection lost", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Toast.makeText(MainActivity.this, "Message arrived: " + message.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Toast.makeText(MainActivity.this, "Message Delivered", Toast.LENGTH_SHORT).show();
                    }
                });

                mqttConnectOptions = new MqttConnectOptions();
                mqttConnectOptions.setCleanSession(false);
                mqttConnectOptions.setKeepAliveInterval(120);
                mqttConnectOptions.setAutomaticReconnect(true);
                mqttClient.connect(mqttConnectOptions);


            } catch (MqttException e) {
                e.printStackTrace();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}