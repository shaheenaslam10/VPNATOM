package com.next.sheharyar;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ViewAnimator;

import com.next.sheharyar.logger.LogFragment;
import com.next.sheharyar.logger.LogWrapper;
import com.next.sheharyar.logger.MessageOnlyLogFilter;

public class ConnectActivity extends AppCompatActivity {

    LogFragment logFragment;
    int connection_type;
    public LogWrapper logWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        initializeLogging();
        if (savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                if (extras.containsKey("connection_type"))
                {
                    connection_type = extras.getInt("connection_type");
                }
            }
        }
    }

    public void initializeLogging() {
        logWrapper= new LogWrapper();
        com.next.sheharyar.logger.Log.setLogNode(logWrapper);
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
        output.setDisplayedChild(1);

    }
}