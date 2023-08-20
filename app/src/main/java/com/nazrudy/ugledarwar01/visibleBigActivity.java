package com.nazrudy.ugledarwar01;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class visibleBigActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visible_big);
        // Подключние visibleBigFrameLayout
        FrameLayout frameLayout=findViewById(R.id.visibleBigFrameLayout);
        // выборка данных из intent

        String textInfo=(String)getIntent().getSerializableExtra("textInfo");
        String linkBig=(String)getIntent().getSerializableExtra("linkBig");
        // описание параметров
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // удаление с экрана всех элементов
        frameLayout.removeAllViews();
        // установка черного цвета с непрозрачностью
        frameLayout.setBackgroundColor(Color.argb(255,0,0,0));
        // подготовка элементов окна
        ScrollView scrollView=new ScrollView(this);
        HorizontalScrollView horizontalScrollView=new HorizontalScrollView(this);
        LinearLayout linearLayout=new LinearLayout(this);
        TextView textView=new TextView(this);
        textView.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ArtifaktElement-Regular.ttf"));
        textView.setTextSize(16);
        textView.setTextColor(Color.argb(255,255,255,0));
        textView.setPadding(10,10,10,10);
        ImageView imageView=new ImageView(this);
        // установка параметров элементам
        scrollView.setLayoutParams(params);
        horizontalScrollView.setLayoutParams(params);
        linearLayout.setLayoutParams(params2);
        textView.setLayoutParams(params);
        imageView.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        imageView.setBackgroundColor(Color.argb(255,0,0,0));
        imageView.setPadding(0,0,0,0);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        // установка названия улицы
        textView.setText(textInfo);
        //textView.setPadding(8,8,8,8);

        linearLayout.addView(textView);
        linearLayout.addView(imageView);
        //horizontalScrollView.addView(linearLayout);
        //scrollView.addView(horizontalScrollView);
        frameLayout.addView(linearLayout);

        // Загрузка фото из сети
        Picasso.get()
                .load(linkBig)
                .placeholder(R.drawable.loading02s)
                .error(R.drawable.errorloadimage3)
                .into(imageView, new Callback() {       // с обработкой ошибок
                    @Override
                    public void onSuccess() {
                        Log.d("error-log", "onSuccess");
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d("error-log", e.getMessage());
                    }
                });
    }
}