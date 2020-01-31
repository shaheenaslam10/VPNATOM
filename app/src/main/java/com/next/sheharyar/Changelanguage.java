package com.next.sheharyar;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.next.sheharyar.LocaleManager.BaseActivity;
import com.next.sheharyar.LocaleManager.LocaleManager;
import com.next.sheharyar.logger.Log;

import static com.next.sheharyar.LocaleManager.LocaleManager.LANGUAGE_ARABIC;
import static com.next.sheharyar.LocaleManager.LocaleManager.LANGUAGE_CHINESE;
import static com.next.sheharyar.LocaleManager.LocaleManager.LANGUAGE_DUTCH;
import static com.next.sheharyar.LocaleManager.LocaleManager.LANGUAGE_ENGLISH;
import static com.next.sheharyar.LocaleManager.LocaleManager.LANGUAGE_FRENCH;
import static com.next.sheharyar.LocaleManager.LocaleManager.LANGUAGE_GERMAN;
import static com.next.sheharyar.LocaleManager.LocaleManager.LANGUAGE_SPANISH;
import static com.next.sheharyar.LocaleManager.LocaleManager.LANGUAGE_TURKISH;

public class Changelanguage extends BaseActivity implements View.OnClickListener {

    TextView PfnSettingsBackText;
    ImageView PfnSettingsBackImage;
    ImageView arabicCheck, chineseCheck, dutchCheck, englishCheck, frenchCheck, germanCheck, spanishCheck, turkishCheck,
            english_uncheck, arabic_uncheck, chinese_uncheck, dutch_uncheck, french_uncheck, german_uncheck, spanish_uncheck, turkish_uncheck;
    String language;
    public static final String TAG = "shani";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_changelanguage);

        PfnSettingsBackText = (TextView)findViewById(R.id.pfn_settings_back);
        PfnSettingsBackImage = (ImageView)findViewById(R.id.pfn_settings_back_image);

        PfnSettingsBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        PfnSettingsBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Changelanguage();
    }

    public void Changelanguage()
    {
        RelativeLayout arabicLayout, chineseLayout, dutchLayout, englishLayout, frenchLayout, germanLayout, spanishLayout, turkishLayout;


        arabicLayout = (RelativeLayout)findViewById(R.id.arabic_layout);
        chineseLayout = (RelativeLayout)findViewById(R.id.chinese_layout);
        dutchLayout = (RelativeLayout)findViewById(R.id.dutch_layout);
        englishLayout = (RelativeLayout)findViewById(R.id.english_layout);
        frenchLayout = (RelativeLayout)findViewById(R.id.french_layout);
        germanLayout = (RelativeLayout)findViewById(R.id.german_layout);
        spanishLayout = (RelativeLayout)findViewById(R.id.spanish_layout);
        turkishLayout = (RelativeLayout)findViewById(R.id.turkish_layout);

        arabicCheck = (ImageView)findViewById(R.id.arabic_check);
        arabic_uncheck = (ImageView)findViewById(R.id.aarabic_uncheck);
        chineseCheck = (ImageView)findViewById(R.id.chinese_check);
        chinese_uncheck = (ImageView)findViewById(R.id.chinese_uncheck);
        englishCheck = (ImageView)findViewById(R.id.english_check);
        english_uncheck = (ImageView)findViewById(R.id.english_uncheck);
        dutchCheck = (ImageView)findViewById(R.id.dutch_check);
        dutch_uncheck = (ImageView)findViewById(R.id.dutch_uncheck);
        frenchCheck = (ImageView)findViewById(R.id.french_check);
        french_uncheck = (ImageView)findViewById(R.id.french_uncheck);
        germanCheck = (ImageView)findViewById(R.id.german_check);
        german_uncheck = (ImageView)findViewById(R.id.german_uncheck);
        spanishCheck = (ImageView)findViewById(R.id.spanish_check);
        spanish_uncheck = (ImageView)findViewById(R.id.spanish_uncheck);
        turkishCheck = (ImageView)findViewById(R.id.turkish_check);
        turkish_uncheck = (ImageView)findViewById(R.id.turkish_uncheck);


        chineseLayout.setOnClickListener((View.OnClickListener)this);
        dutchLayout.setOnClickListener((View.OnClickListener)this);
        englishLayout.setOnClickListener((View.OnClickListener)this);
        frenchLayout.setOnClickListener((View.OnClickListener)this);
        germanLayout.setOnClickListener((View.OnClickListener)this);
        spanishLayout.setOnClickListener((View.OnClickListener)this);
        turkishLayout.setOnClickListener((View.OnClickListener)this);
        arabicLayout.setOnClickListener((View.OnClickListener)this);


        SharedPreferences prefs = getSharedPreferences("connection", MODE_PRIVATE);
        if (prefs != null) {
            language = prefs.getString("language", null);

            if (language==null){

                englishCheck.setVisibility(View.VISIBLE);
                english_uncheck.setVisibility(View.INVISIBLE);

            }else {

                if (language.equals(LANGUAGE_ARABIC)){

                    Log.d(TAG," ARABIC not null language.....");

                    arabicCheck.setVisibility(View.VISIBLE);
                    arabic_uncheck.setVisibility(View.INVISIBLE);

                }else if (language.equals(LANGUAGE_DUTCH)){
                    dutchCheck.setVisibility(View.VISIBLE);
                    dutch_uncheck.setVisibility(View.INVISIBLE);
                }else if (language.equals(LANGUAGE_CHINESE)){
                    chineseCheck.setVisibility(View.VISIBLE);
                    chinese_uncheck.setVisibility(View.INVISIBLE);
                }else if (language.equals(LANGUAGE_ENGLISH)){
                    Log.d(TAG," _ENGLISH not null language.....");

                    englishCheck.setVisibility(View.VISIBLE);
                    english_uncheck.setVisibility(View.INVISIBLE);
                }else if (language.equals(LANGUAGE_FRENCH)){
                    frenchCheck.setVisibility(View.VISIBLE);
                    french_uncheck.setVisibility(View.INVISIBLE);
                }else if (language.equals(LANGUAGE_GERMAN)){
                    germanCheck.setVisibility(View.VISIBLE);
                    german_uncheck.setVisibility(View.INVISIBLE);
                }else if (language.equals(LANGUAGE_SPANISH)){
                    spanishCheck.setVisibility(View.VISIBLE);
                    spanish_uncheck.setVisibility(View.INVISIBLE);
                }else if (language.equals(LANGUAGE_TURKISH)){
                    turkishCheck.setVisibility(View.VISIBLE);
                    turkish_uncheck.setVisibility(View.INVISIBLE);
                }
            }
        }


    }


    @Override
    public void onClick(View view) {




        switch(view.getId()){
            case R.id.arabic_layout:
                arabicCheck.setVisibility(View.VISIBLE);
                arabic_uncheck.setVisibility(View.INVISIBLE);
                englishCheck.setVisibility(View.INVISIBLE);
                english_uncheck.setVisibility(View.VISIBLE);
                chineseCheck.setVisibility(View.INVISIBLE);
                chinese_uncheck.setVisibility(View.VISIBLE);
                dutchCheck.setVisibility(View.INVISIBLE);
                dutch_uncheck.setVisibility(View.VISIBLE);
                frenchCheck.setVisibility(View.INVISIBLE);
                french_uncheck.setVisibility(View.VISIBLE);
                germanCheck.setVisibility(View.INVISIBLE);
                german_uncheck.setVisibility(View.VISIBLE);
                spanishCheck.setVisibility(View.INVISIBLE);
                spanish_uncheck.setVisibility(View.VISIBLE);
                turkishCheck.setVisibility(View.INVISIBLE);
                turkish_uncheck.setVisibility(View.VISIBLE);
                setNewLocale(LANGUAGE_ARABIC, false);
                break;
            case R.id.dutch_layout:
                dutchCheck.setVisibility(View.VISIBLE);
                arabicCheck.setVisibility(View.INVISIBLE);
                arabic_uncheck.setVisibility(View.VISIBLE);
                englishCheck.setVisibility(View.INVISIBLE);
                english_uncheck.setVisibility(View.VISIBLE);
                chineseCheck.setVisibility(View.INVISIBLE);
                chinese_uncheck.setVisibility(View.VISIBLE);
                frenchCheck.setVisibility(View.INVISIBLE);
                french_uncheck.setVisibility(View.VISIBLE);
                germanCheck.setVisibility(View.INVISIBLE);
                german_uncheck.setVisibility(View.VISIBLE);
                spanishCheck.setVisibility(View.INVISIBLE);
                spanish_uncheck.setVisibility(View.VISIBLE);
                turkishCheck.setVisibility(View.INVISIBLE);
                turkish_uncheck.setVisibility(View.VISIBLE);
                setNewLocale(LANGUAGE_DUTCH, false);
                break;
            case R.id.chinese_layout:
                chineseCheck.setVisibility(View.VISIBLE);
                arabicCheck.setVisibility(View.INVISIBLE);
                arabic_uncheck.setVisibility(View.VISIBLE);
                englishCheck.setVisibility(View.INVISIBLE);
                english_uncheck.setVisibility(View.VISIBLE);
                dutchCheck.setVisibility(View.INVISIBLE);
                dutch_uncheck.setVisibility(View.VISIBLE);
                frenchCheck.setVisibility(View.INVISIBLE);
                french_uncheck.setVisibility(View.VISIBLE);
                germanCheck.setVisibility(View.INVISIBLE);
                german_uncheck.setVisibility(View.VISIBLE);
                spanishCheck.setVisibility(View.INVISIBLE);
                spanish_uncheck.setVisibility(View.VISIBLE);
                turkishCheck.setVisibility(View.INVISIBLE);
                turkish_uncheck.setVisibility(View.VISIBLE);
                setNewLocale(LANGUAGE_CHINESE, false);
                break;
            case R.id.english_layout:
                englishCheck.setVisibility(View.VISIBLE);
                arabicCheck.setVisibility(View.INVISIBLE);
                arabic_uncheck.setVisibility(View.VISIBLE);
                chineseCheck.setVisibility(View.INVISIBLE);
                chinese_uncheck.setVisibility(View.VISIBLE);
                dutchCheck.setVisibility(View.INVISIBLE);
                dutch_uncheck.setVisibility(View.VISIBLE);
                frenchCheck.setVisibility(View.INVISIBLE);
                french_uncheck.setVisibility(View.VISIBLE);
                germanCheck.setVisibility(View.INVISIBLE);
                german_uncheck.setVisibility(View.VISIBLE);
                spanishCheck.setVisibility(View.INVISIBLE);
                spanish_uncheck.setVisibility(View.VISIBLE);
                turkishCheck.setVisibility(View.INVISIBLE);
                turkish_uncheck.setVisibility(View.VISIBLE);
                setNewLocale(LANGUAGE_ENGLISH, false);
                break;
            case R.id.french_layout:
                frenchCheck.setVisibility(View.VISIBLE);
                arabicCheck.setVisibility(View.INVISIBLE);
                arabic_uncheck.setVisibility(View.VISIBLE);
                chineseCheck.setVisibility(View.INVISIBLE);
                chinese_uncheck.setVisibility(View.VISIBLE);
                dutchCheck.setVisibility(View.INVISIBLE);
                dutch_uncheck.setVisibility(View.VISIBLE);
                englishCheck.setVisibility(View.INVISIBLE);
                english_uncheck.setVisibility(View.VISIBLE);
                germanCheck.setVisibility(View.INVISIBLE);
                german_uncheck.setVisibility(View.VISIBLE);
                spanishCheck.setVisibility(View.INVISIBLE);
                spanish_uncheck.setVisibility(View.VISIBLE);
                turkishCheck.setVisibility(View.INVISIBLE);
                turkish_uncheck.setVisibility(View.VISIBLE);
                setNewLocale(LANGUAGE_FRENCH, false);
                break;
            case R.id.german_layout:
                germanCheck.setVisibility(View.VISIBLE);
                arabicCheck.setVisibility(View.INVISIBLE);
                arabic_uncheck.setVisibility(View.VISIBLE);
                chineseCheck.setVisibility(View.INVISIBLE);
                chinese_uncheck.setVisibility(View.VISIBLE);
                dutchCheck.setVisibility(View.INVISIBLE);
                dutch_uncheck.setVisibility(View.VISIBLE);
                frenchCheck.setVisibility(View.INVISIBLE);
                french_uncheck.setVisibility(View.VISIBLE);
                englishCheck.setVisibility(View.INVISIBLE);
                english_uncheck.setVisibility(View.VISIBLE);
                spanishCheck.setVisibility(View.INVISIBLE);
                spanish_uncheck.setVisibility(View.VISIBLE);
                turkishCheck.setVisibility(View.INVISIBLE);
                turkish_uncheck.setVisibility(View.VISIBLE);
                setNewLocale(LANGUAGE_GERMAN, false);
                break;
            case R.id.spanish_layout:
                spanishCheck.setVisibility(View.VISIBLE);
                arabicCheck.setVisibility(View.INVISIBLE);
                arabic_uncheck.setVisibility(View.VISIBLE);
                chineseCheck.setVisibility(View.INVISIBLE);
                chinese_uncheck.setVisibility(View.VISIBLE);
                dutchCheck.setVisibility(View.INVISIBLE);
                dutch_uncheck.setVisibility(View.VISIBLE);
                frenchCheck.setVisibility(View.INVISIBLE);
                french_uncheck.setVisibility(View.VISIBLE);
                germanCheck.setVisibility(View.INVISIBLE);
                german_uncheck.setVisibility(View.VISIBLE);
                englishCheck.setVisibility(View.INVISIBLE);
                english_uncheck.setVisibility(View.VISIBLE);
                turkishCheck.setVisibility(View.INVISIBLE);
                turkish_uncheck.setVisibility(View.VISIBLE);
                setNewLocale(LANGUAGE_SPANISH, false);

                break;
            case R.id.turkish_layout:
                turkishCheck.setVisibility(View.VISIBLE);
                arabicCheck.setVisibility(View.INVISIBLE);
                arabic_uncheck.setVisibility(View.VISIBLE);
                chineseCheck.setVisibility(View.INVISIBLE);
                chinese_uncheck.setVisibility(View.VISIBLE);
                dutchCheck.setVisibility(View.INVISIBLE);
                dutch_uncheck.setVisibility(View.VISIBLE);
                frenchCheck.setVisibility(View.INVISIBLE);
                french_uncheck.setVisibility(View.VISIBLE);
                germanCheck.setVisibility(View.INVISIBLE);
                german_uncheck.setVisibility(View.VISIBLE);
                englishCheck.setVisibility(View.INVISIBLE);
                english_uncheck.setVisibility(View.VISIBLE);
                spanishCheck.setVisibility(View.INVISIBLE);
                spanish_uncheck.setVisibility(View.VISIBLE);
                setNewLocale(LANGUAGE_TURKISH, false);

                break;
        }
    }

    private boolean setNewLocale(String language, boolean restartProcess) {

        Log.d(TAG,"selected....."+language);

        SharedPreferences.Editor editor = getSharedPreferences("connection", MODE_PRIVATE).edit();
        editor.putString("language", language);
        editor.apply();


        LocaleManager.setNewLocale(this, language);
        Intent i = new Intent(this, Changelanguage.class);
        startActivity(i);
        finish();

        if (restartProcess) {
            System.exit(0);
        } else {
            Toast.makeText(this, "Language changed.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}
