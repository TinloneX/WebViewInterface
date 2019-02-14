package org.tinlone.demo.webinteface.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.tinlone.demo.webinteface.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickX5(View view) {
        startActivity(new Intent(this,X5WebActivity.class));
    }

    public void onClickInterface(View view) {
        startActivity(new Intent(this,NativeJSInterfaceActivity.class));
    }

    public void onClickJSBridge(View view) {

    }
}
