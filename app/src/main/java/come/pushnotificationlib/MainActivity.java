package come.pushnotificationlib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import push.notifiction.mylibrary.ExtraUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExtraUtils.addToServer(this);
        ExtraUtils.checkIfNotificationRequired(this);
    }
}