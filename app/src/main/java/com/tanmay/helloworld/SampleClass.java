package com.tanmay.helloworld;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SampleClass {

//    String topic        = "MQTTExamples/Tanmay";
//    String content      = "Message from MqttPublishSample";
//    int qos             = 2;
//    String broker       = "tcp://broker.hivemq.com:1883";
//    String clientId     = "JavaSample";
//    MemoryPersistence persistence = new MemoryPersistence();
//    Button button;

//    button = findViewById(R.id.btn_publish);
//        button.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            try {
//                MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
//                MqttConnectOptions connOpts = new MqttConnectOptions();
//                connOpts.setCleanSession(true);
//                Toast.makeText(MainActivity.this, "Connecting to broker: "+broker, Toast.LENGTH_SHORT).show();
//                sampleClient.connect(connOpts);
//                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "Publishing message: "+content, Toast.LENGTH_SHORT).show();
//                MqttMessage message = new MqttMessage(content.getBytes());
//                message.setQos(qos);
//                sampleClient.publish(topic, message);
//                Toast.makeText(MainActivity.this, "Message published", Toast.LENGTH_SHORT).show();
//                sampleClient.disconnect();
//                Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
//            } catch(MqttException me) {
//                Log.i( "reason112", String.valueOf(+me.getReasonCode()));
//                Log.i("msg112",me.getMessage());
//                Log.i("loc112",me.getLocalizedMessage());
//                Log.i("cause112", String.valueOf(me.getCause()));
//                Log.i("excep112", String.valueOf(me));
//                me.printStackTrace();
//            }
//        }
//    });


}
