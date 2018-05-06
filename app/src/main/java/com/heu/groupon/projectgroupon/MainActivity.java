package com.heu.groupon.projectgroupon;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private TextView mBottomHome,mBottomSearch,mBottomMe;
    private HomeFragment mHome;
    private SearchFragment mSearch;
    private MyFragment mMy;
    private boolean isQuitting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isQuitting=false;
        InitRef();
        InitBtns();
        InitFrags();

    }
    protected void InitRef()
    {
        mBottomHome=findViewById(R.id.main_bottombar_btnhome);
        mBottomSearch=findViewById(R.id.main_bottombar_btsearch);
        mBottomMe=findViewById(R.id.main_bottombar_btnme);
    }
    protected void InitBtns()
    {
        mBottomHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(mHome);
            }
        });
        mBottomSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(mSearch);
            }
        });
        mBottomMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(mMy);
            }
        });
    }
    protected void InitFrags()
    {
        mHome=new HomeFragment(this);
        mSearch=new SearchFragment(this);
        mMy=new MyFragment(this);
        setFragment(mHome);
    }
    protected void setFragment(Fragment frag)
    {
         FragmentManager fm = getFragmentManager();
         FragmentTransaction transaction = fm.beginTransaction();
         transaction.replace(R.id.main_frame, frag);
         transaction.commit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (!isQuitting) {
                Toast.makeText(getApplicationContext(), "再次按下以退出", Toast.LENGTH_SHORT).show();
                isQuitting = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        isQuitting = false;
                    }
                }, 3000);
                return true;
            } else {
                System.exit(0);
                return true;
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
