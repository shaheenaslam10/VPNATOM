package com.next.sheharyar;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.next.sheharyar.LocaleManager.LocaleManager;

public class AboutActivity extends AppCompatActivity {
    TextView PfnSettingsBack, PfnOpportunityText, PrivacyPolicyText, TermsofServiceText;
    ImageView PfnSettingsbackImage, PfnopportunityImage,  PrivacyPolicyImage, TermsofServiceImage;
    RelativeLayout TermsOfService, PrivacyPolicy, PfnOpportunity;
    public static final String TAG = "shani";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);

        //TextViews

        PfnSettingsBack = (TextView)findViewById(R.id.pfn_settings_back);
        PfnOpportunityText = (TextView)findViewById(R.id.pfn_opportunity_text);
        PrivacyPolicyText = (TextView)findViewById(R.id.privacy_policy_text);
        TermsofServiceText = (TextView)findViewById(R.id.terms_of_service_text);

        //ImageViews

        PfnSettingsbackImage = (ImageView)findViewById(R.id.pfn_settings_back_image);
        PfnopportunityImage = (ImageView)findViewById(R.id.pfn_opportunity_image);
        PrivacyPolicyImage = (ImageView)findViewById(R.id.privacy_policy_image);
        TermsofServiceImage = (ImageView)findViewById(R.id.terms_of_service_image);

        //RelativeLayout

        TermsOfService = (RelativeLayout)findViewById(R.id.terms_of_service);
        PrivacyPolicy = (RelativeLayout)findViewById(R.id.privacy_policy);
        PfnOpportunity = (RelativeLayout)findViewById(R.id.pfn_opportunity);

        //Opening the web urls using intents inside Relative Laypouts

        TermsOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent termsOfService = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-terms-of-service/"));
                startActivity(termsOfService);*/

/*               Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
               intent.putExtra("about_type","TermsofService");
               startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-terms-of-service/"));
                startActivity(browserIntent);
            }
        });

        PrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent privacyPolicy = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-privacy-policy/"));
                startActivity(privacyPolicy);*/

                /*Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("about_type","PrivacyPolicy");
                startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-privacy-policy/"));
                startActivity(browserIntent);
            }
        });


        PfnOpportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent pfnopportunity = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-opportunity/"));
                startActivity(pfnopportunity);*/
                /*Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("about_type","PfnOpportunity");
                startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-opportunity/"));
                startActivity(browserIntent);
            }
        });

        //Opening the web urls using intents inside ImageViews

        PfnSettingsbackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent pfnSettingsBackImage = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(pfnSettingsBackImage);*/
               finish();
            }
        });

        PfnopportunityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent pfnopportunityImage = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-opportunity/"));
                startActivity(pfnopportunityImage);*/
               /* Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("about_type","PfnOpportunity");
                startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-opportunity/"));
                startActivity(browserIntent);
            }
        });



        PrivacyPolicyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent privacyPolicyImage = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-terms-of-service/"));
                startActivity(privacyPolicyImage);*/
                /*Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("about_type","PrivacyPolicy");
                startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-privacy-policy/"));
                startActivity(browserIntent);
            }
        });

        TermsofServiceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent termsOfServiceImage = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-terms-of-service/"));
                startActivity(termsOfServiceImage);*/
                /*Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("about_type","TermsofService");
                startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-terms-of-service/"));
                startActivity(browserIntent);
            }
        });


        //Opening the web urls using intents inside TextViews
        PfnSettingsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent pfnSettingsBack = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(pfnSettingsBack);*/
                finish();
            }
        });

        PfnOpportunityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent pfnOpportunityText = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-opportunity/"));
                startActivity(pfnOpportunityText);*/
                /*Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("about_type","PfnOpportunity");
                startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-opportunity/"));
                startActivity(browserIntent);
            }
        });


        PrivacyPolicyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent privacyPolicyText = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-privacy-policy/"));
                startActivity(privacyPolicyText);*/
                /*Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("about_type","PrivacyPolicy");
                startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-privacy-policy/"));
                startActivity(browserIntent);
            }
        });

        TermsofServiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent termsOfServiceText = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.onlinesecurityapp.com/app-terms-of-service/"));
                startActivity(termsOfServiceText);*/
               /* Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("about_type","TermsofService");
                startActivity(intent);*/

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.onlinesecurityapp.com/app-terms-of-service/"));
                startActivity(browserIntent);
            }
        });

    }

    String language;
    @Override
    protected void onResume() {
        super.onResume();


        Log.d(TAG,"current language......"+ LocaleManager.getLanguage(this));
        Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()));
        Log.d(TAG,"current locale......"+ LocaleManager.getLocale(this.getResources()).getLanguage());

       /* String oldLanguage = LocaleManager.getLanguage(this);
        language = LocaleManager.getLocale(this.getResources()).getLanguage();

        if (!oldLanguage.equals(language)){
            LocaleManager.setNewLocale(this,LocaleManager.getLanguage(this));
            finish();
            startActivity(getIntent());
        }*/
    }
}
