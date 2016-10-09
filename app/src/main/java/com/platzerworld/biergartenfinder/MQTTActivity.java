package com.platzerworld.biergartenfinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import org.eclipse.paho.android.service.MqttAndroidClient;

public class MQTTActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private TextView textViewResult;
    private EditText editTextMQTTResult;
    private EditText editTextMessage;
    private MqttCallback mqttCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.textViewResult = (TextView)findViewById(R.id.textViewResult);
        this.editTextMQTTResult = (EditText)findViewById(R.id.editTextMQTT);
        this.editTextMessage = (EditText)findViewById(R.id.txtMessage);

        Button btnClear = (Button)findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewResult.setText("");
            }
        });

        Button btnConnect = (Button)findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        Button btnDisconnect = (Button)findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();
            }
        });

        Button btnSubscribe = (Button)findViewById(R.id.btnSubscribe);
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe();
            }
        });

        Button btnUnSubscribe = (Button)findViewById(R.id.btnUnSubscribe);
        btnUnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unsubscribe();
            }
        });

        Button btnPublish = (Button)findViewById(R.id.btnPublish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });

        Button btnPublishRetain = (Button)findViewById(R.id.btnPublishRetain);
        btnPublishRetain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishRetain();
            }
        });

    }

    public void connect () {
        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://h2454492.stratoserver.net:1883",
                        clientId);
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    textViewResult.append("\n" + "Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    //subscribe();
                } else {
                    textViewResult.append("\n" + "Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                textViewResult.append("\n" + "The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                textViewResult.append("\n" + "Incoming message: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);


        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);


        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    textViewResult.append("\n" + "Connected to MQTT sucessful");
                    Log.d("MQTTActivity", "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    textViewResult.append("\n" + "Connected to MQTT failed");
                    Log.d("MQTTActivity", "onFailure");

                }
            });


            // Connect with MQTT 3.1 or MQTT 3.1.1
            //MqttConnectOptions options = new MqttConnectOptions();
            //options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            //IMqttToken token311 = client.connect(options);

            // Connect with LWT
            //String topic = "users/last/will";
            //byte[] payload = "some payload".getBytes();
            //options.setWill(topic, payload ,1,false);
            //IMqttToken tokenLWT = client.connect(options);


            // Connect with Username / Password
            //options.setUserName("USERNAME");
            //options.setPassword("PASSWORD".toCharArray());
            //IMqttToken token = client.connect(options);

        } catch (MqttException e) {
            e.printStackTrace();
        }

        textViewResult.append("\n" + "Connected to " +editTextMQTTResult.getText().toString());
        Log.d("MQTTActivity", "Connect");
    }

    public void disconnect () {
        // Disconnect
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    textViewResult.append("\n" + "Disconnected to MQTT sucessful");
                    Log.d("MQTTActivity", "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    textViewResult.append("\n" + "Disconnected to MQTT failed");
                    Log.d("MQTTActivity", "onSuccess");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe () {
        // Subscribe
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(editTextMQTTResult.getText().toString(), qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    textViewResult.append("\n" + "Subscribe to MQTT sucessful");
                    Log.d("MQTTActivity", "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    textViewResult.append("\n" + "Subscribe to MQTT failed");
                    Log.d("MQTTActivity", "onSuccess");
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish () {
        // Publish
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = editTextMessage.getText().toString().getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            if(!client.isConnected()){

            }
            client.publish(editTextMQTTResult.getText().toString(), message);
            textViewResult.append("\n" + "Publish to MQTT sucessful");
        } catch (UnsupportedEncodingException | MqttException e) {
            textViewResult.append("\n" + "Publish to MQTT failed");
            e.printStackTrace();
        }
    }

    public  void unsubscribe () {
        // Unsubscribe
        try {
            IMqttToken unsubToken = client.unsubscribe(editTextMQTTResult.getText().toString());
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    textViewResult.append("\n" + "Usubscribe to MQTT sucessful");
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    textViewResult.append("\n" + "Usubscribe to MQTT failed");
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void publishRetain () {
        // Publish a retained message
        String payload = "the payload 1 GPL";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(editTextMQTTResult.getText().toString(), message);
            textViewResult.append("\n" + "publishRetain to MQTT sucessful");
        } catch (UnsupportedEncodingException | MqttException e) {
            textViewResult.append("\n" + "publishRetain to MQTT failed");
            e.printStackTrace();
        }
    }


}
