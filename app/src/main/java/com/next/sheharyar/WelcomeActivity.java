package com.next.sheharyar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext, btnLogin;
    public static final String TAG = "shani";
    String pref_username, pass;
    boolean first = true;
    private static String uniqueID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefs_id = getSharedPreferences("PREF_UNIQUE_ID", MODE_PRIVATE);
        if (prefs_id != null) {
            uniqueID = prefs_id.getString("PREF_UNIQUE_ID", null);
        }

        SharedPreferences prefs = getSharedPreferences("connection", MODE_PRIVATE);
        if (prefs != null) {
            pref_username = prefs.getString("username", null);
            pass = prefs.getString("pass", null);

            if (pref_username!=null){

                if (AtomDemoApplicationController.getInstance().getAtomManager() != null) {

                    AtomDemoApplicationController.getInstance().getAtomManager().setUUID(uniqueID);

                    Intent intent = new Intent(getApplicationContext(), ModeSelectionActivity.class);
                    intent.putExtra("from","login");
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                }

            }
        }

        // Checking for first time launch - before calling setContentView()
      com.next.sheharyar.PreferenceManager prefManager = new com.next.sheharyar.PreferenceManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
//        btnSkip = (Button) findViewById(R.id.btn_skip);
//        btnNext = (Button) findViewById(R.id.btn_next);
        btnLogin = (Button)findViewById(R.id.login_button);

        layouts = new int[]{
                R.layout.introscreen1,
                R.layout.intro_1,
                R.layout.intro_3};

        addBottomDots(0);

        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginbtn = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginbtn);
//                launchHomeScreen();
            }
        });
    }

    private void addBottomDots(int currentPage) {



        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {


            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        com.next.sheharyar.PreferenceManager prefManager = new com.next.sheharyar.PreferenceManager(this);
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {


            addBottomDots(position);
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
            } else {
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}
