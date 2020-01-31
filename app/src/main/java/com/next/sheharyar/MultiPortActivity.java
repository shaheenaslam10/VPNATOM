package com.next.sheharyar;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.logger.Log;

import java.lang.reflect.Field;
import java.util.Locale;

public class MultiPortActivity extends AppCompatActivity {

    LinearLayout enter_port_layout;
    TextView port , manual_port_selection_desc , pfn_settings_back;
    CheckBox checkBox;
    ImageView pfn_settings_back_image;
    public static final String TAG = "shani";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String languageToLoad  = LocaleManager.getLanguage(this); // change your language here
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_multi_port);

        pfn_settings_back = (TextView) findViewById(R.id.pfn_settings_back);
        pfn_settings_back_image = (ImageView) findViewById(R.id.pfn_settings_back_image);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        enter_port_layout = (LinearLayout)findViewById(R.id.enter_port_layout);
        port = (TextView)findViewById(R.id.port);
        manual_port_selection_desc = (TextView)findViewById(R.id.manual_port_selection_desc);



        enter_port_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowPopUp();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d(TAG, "checked = "+isChecked);
            }
        });

        pfn_settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pfn_settings_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    public void ShowPopUp(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage(null);

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        setCursorColor(input, getResources().getColor(R.color.Grey));
        input.setHint("53,5500-30000");
        input.getBackground().mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();


    }

    public static void setCursorColor(EditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    String language;
    /*@Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"current language......"+ LocaleManager.getLanguage(this));
        Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()));
        Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()).getLanguage());

        String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)){
            LocaleManager.setNewLocale(this,LocaleManager.getLanguage(this));
            startActivity(getIntent());
                    finish();
        }

    }*/
}
