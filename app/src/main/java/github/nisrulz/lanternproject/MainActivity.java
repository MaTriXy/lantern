/*
 * Copyright (C) 2017 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.nisrulz.lanternproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Toast;
import github.nisrulz.lantern.Lantern;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 100;

    private Lantern lantern;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwitchCompat toggle = findViewById(R.id.switch_flash);
        lantern = new Lantern(this).checkAndRequestSystemPermission(true);

        if (!lantern.init()) {
            lantern.checkForCameraPermission(REQUEST_CODE);
        }

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                if (state) {
                    // true
                    lantern.alwaysOnDisplay(true)
                            .fullBrightDisplay(true)
                            .enableTorchMode(true)
                            .pulse(true).withDelay(1, TimeUnit.SECONDS);
                } else {
                    //false
                    lantern.alwaysOnDisplay(false)
                            .fullBrightDisplay(false)
                            .enableTorchMode(false).pulse(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        lantern.cleanup();
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (!lantern.init()) {
                Toast.makeText(MainActivity.this, "Camera Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
