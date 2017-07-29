package com.a09gmail.a23.adityaprakash.particleswitch;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudException;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;

import static io.particle.android.sdk.utils.Py.list;

public class MainActivity extends AppCompatActivity {

    //Variables...
    private ToggleButton button;
    private ParticleDevice myDevice;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (ToggleButton) findViewById(R.id.tb);
        ParticleCloudSDK.init(getApplicationContext());
        img = (ImageView)findViewById(R.id.image);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    ParticleCloudSDK.getCloud().logIn("emailID", "password");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_LONG).show();
                        }
                    });

                    myDevice = ParticleCloudSDK.getCloud().getDevice("access token");
                    final String nameString = myDevice.getName();
                    if(ParticleCloudSDK.getCloud().getDevice("access token").isConnected()){
                    while(ParticleCloudSDK.getCloud().getDevice("access token").isConnected()){

                        runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        onToggleClick(v);
                                    } catch (ParticleCloudException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (ParticleDevice.FunctionDoesNotExistException e) {
                                        e.printStackTrace();
                                    } catch (ParticleDevice.VariableDoesNotExistException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        });
                    }}
                    }catch (ParticleCloudException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
    }



    public void onToggleClick(View view) throws ParticleCloudException, IOException, ParticleDevice.FunctionDoesNotExistException, ParticleDevice.VariableDoesNotExistException {

        if (button.isChecked() == true) {
            //Board is Switched ON
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        int result = myDevice.callFunction("power", list("on"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageResource(R.drawable.on);
                            }
                        });

                    } catch (ParticleDevice.FunctionDoesNotExistException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParticleCloudException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();

        } else if (button.isChecked() == false) {
            //Board is Switched OFF
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        int result = myDevice.callFunction("power", list("off"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageResource(R.drawable.off);
                            }
                        });
                    } catch (ParticleDevice.FunctionDoesNotExistException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParticleCloudException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }
}


