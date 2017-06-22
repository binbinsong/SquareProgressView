package com.squareprogress;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private MySquareProcessView squareProgressView;
    private MyHandnler myHandnler = new MyHandnler(this);
    private int currentProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        squareProgressView = (MySquareProcessView) findViewById(R.id.sqareProcess);
        myHandnler.sendEmptyMessage(0);
    }

    public static class MyHandnler extends Handler {
        WeakReference<MainActivity> weakReference;

        public MyHandnler(MainActivity mainActivity) {
            weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weakReference.get().currentProcess++;
            if (weakReference.get().currentProcess > 100) {
                weakReference.get().currentProcess = 0;
            }
            weakReference.get().squareProgressView.setCurrentPogress(weakReference.get().currentProcess);
            sendEmptyMessageDelayed(0, 200);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandnler.removeCallbacksAndMessages(null);
    }
}