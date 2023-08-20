package com.nazrudy.ugledarwar01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.FontFamily;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PublicKey;

public class MainUgledarWar extends AppCompatActivity {
    Long downloadReferenceID;
    // Постоянная ссылка на версию в сети
    final String urlWebver = "https://drive.google.com/uc?export=download&id=1upyBBlwl34jUxZTZ5TMAVyPMpK7o16sS";
    // Постоянная ссылка на файл с обновлениями в сети
    final String urlWebdata = "https://drive.google.com/uc?export=download&id=1W1urvuu-4aSBBRWj00mIcNc_96s2c3_f";
    final String urlWebCountInfo="https://drive.google.com/uc?export=download&id=12QzxnpJFVWZIK8w1Bs34-PXOZgPYD2TT";
    final String urlWebDataInfo="https://drive.google.com/uc?export=download&id=1SLJVIWrkJPU_4idgUKV_ERruh0L_mSWs";
    DataMap dataMapObj;
    String verWeb;
    String verDev;
    String datWeb = null;
    String subPath;
    File filePath;   // filePath=getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    final String versionDev = "version.txt";
    final String versionWeb = "versionWeb.txt";
    final String dataWeb = "dataWeb.txt";
    final int rows = 64;
    final int colums = 81;
    LinearLayout linearLayoutDown;
    TextView textViewDown;
    FrameLayout frameLayout;
    TableLayout tableUgledarWar;
    int levelContext=0;
    long downRefIDver;
    boolean isDownVer=false;
    long downRefIDdat;

    final String newText="new";
    SharedPreferences shPref;
    final String politik="POLITIK";
    final String polKonUrl="https://doc-hosting.flycricket.io/ugledarwar-privacy-policy/b714c769-da4c-4e09-9f31-e3765db07392/privacy";
    // инструкция
    final String INSTRUKC="Инструкция";
    final String INSTRUKURL="https://kept.com.ua/image/hmeV/instrukciya.png";
    // территориальная принадлежность
    final String TERPRIN="Территориальная принадлежность";
    final String TERPRINURL="https://kept.com.ua/image/8XX6/terprinUkr.png";
    // телеграмм
    final String TELEGRAM="https://t.me/UgledarWarGroup";
    // о приложении
    final String AVTUGLWAR="О приложении";
    final String AVTUGLWARURL="https://kept.com.ua/image/93dY/oprog3.png";
    // новости
    final String dataInfo="dataInfo.txt"; // переменная названия файла новостей
    final String countInfoDev="countInfoDev.txt"; // переменная названия файла количества новостей в девайсе
    final String countInfoWeb="countInfoWeb.txt"; // переменная названия файла количества новостей из интернента
    boolean isNewInfo=false;  // флаг наличия новостей
    long downRefIDcouInfo;  // ID загрузки файла количества новостей
    String cNewInfoDev;     // переменная для информации из файла countInfoDev.txt
    String cNewInfoWeb;     // переменная для информации из файла countInfoWeb.txt
    boolean isDownCount=false;  // флаг - загружен ли файл количества новостей
    int countNewInfo=0;         // количество новых новостей
    long downRefIDdatInfo;      // ID загрузки файла новостей

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ugledar_war);

        shPref=getSharedPreferences(politik, MODE_PRIVATE);

        // получаем framelayoutroot
        frameLayout = findViewById(R.id.framelayoutroot);

        // Регистрация широковещательного приемника ACTION_DOWNLOAD_COMPLETE
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        Picasso.get()                       // включение записи логов picasso
                .setLoggingEnabled(true);


        // сохранение в переменной директории приложения
        filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        titleScreen();
        Boolean stPolitik=shPref.getBoolean(politik, false);
        linearLayoutDown.removeAllViews();
        //Log.d("POLITIK", stPolitik.toString());

        if (stPolitik)workProg();
        else politikKonf();

    }
    // работа программы
    public void workProg(){
        // текст down
        linearLayoutDown.removeAllViews();
        textViewDown = new TextView(this);
        textViewDown.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        textViewDown.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ArtifaktElement-Regular.ttf"),Typeface.ITALIC);
        textViewDown.setTextSize(16);
        textViewDown.setTextColor(Color.argb(255,128,128,128));
        linearLayoutDown.addView(textViewDown);

        textViewDown.setText("Проверка обновлений...");
        // Проверка на наличие файлов версии, если нет, то создать начальный файл version.txt (new)
        checkFileVer();
        // Запуск начала программы 1
        startProg1();
    }

    // Проверка версии
    public void startProg1() {
        // Загрузка из сети версии и новостей
        // удаление файла предидущей загрузки версии
        File fileVersionWeb=new File(filePath, versionWeb);
        fileVersionWeb.delete();
        // удаление файла предидущей загрузки новостей
        File filecountinfo=new File(filePath, countInfoWeb);
        filecountinfo.delete();
        // загрузка нового файла версий
        subPath = versionWeb;
        downRefIDver=file_download(urlWebver, subPath);
        // загрузка файла количества новостей
        subPath=countInfoWeb;
        downRefIDcouInfo=file_download(urlWebCountInfo,subPath);
    }

    // Продолжение программы после проверуи версии и загрузки файла с данными карты
    public void startProg2() {
        textViewDown.setText("Создание карты...");
        datWeb = fileToTxt(dataWeb);
        dataMapObj = new DataMap(datWeb);
        dataMapObj.initializeArrayMap();
        dataMapObj.createArrayMap();
        // построение и отображение карты
        initializeFrame(frameLayout);
        createMapUgledar();

    }
    // построение окна с разными имеющимися фото
    public void visibleMedia(int rowIndex, int colIndex) {
        levelContext=1;
        // Параметры для LinearLayot
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        //params.setMargins(10, 10, 10, 10);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.00f);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        //params3.setMargins(10, 10, 10, 10);
        // Параметры для текста
        LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Построение окна
        // Основное окно
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // Элемент окна статуса заагрузки(не используется)
        //LinearLayout linearLayoutSt=new LinearLayout(this); //Статус закгрузки
        // Элемент окна названия улицы(адреса)
        LinearLayout linearLayoutUl=new LinearLayout(this); //Улица
        // Элнмент окна медиа
        LinearLayout linearLayoutMed=new LinearLayout(this); //Медиа
        linearLayoutMed.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutMed.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

        // Создание 10ти пустых окон для более точного местоположения окна
        LinearLayout linearLayoutMed1=new LinearLayout(this); //Медиа1
        linearLayoutMed1.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed2=new LinearLayout(this); //Медиа2
        linearLayoutMed2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed3=new LinearLayout(this); //Медиа3
        linearLayoutMed3.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed4=new LinearLayout(this); //Медиа4
        linearLayoutMed4.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed5=new LinearLayout(this); //Медиа5
        linearLayoutMed5.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed6=new LinearLayout(this); //Медиа6
        linearLayoutMed6.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed7=new LinearLayout(this); //Медиа7
        linearLayoutMed7.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed8=new LinearLayout(this); //Медиа8
        linearLayoutMed8.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed9=new LinearLayout(this); //Медиа9
        linearLayoutMed9.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutMed10=new LinearLayout(this); //Медиа10
        linearLayoutMed10.setOrientation(LinearLayout.VERTICAL);

        // Удаление всех элементов построенных ранее(бомбы и пустые клетки)
        tableUgledarWar.removeAllViews();
        // слушатель нажатия для возврата, если не выбрано фото
        tableUgledarWar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMapUgledar();
            }
        });

        // добавление layoutov для постройки окна выбора фото
        LinearLayout linearLayoutMedia=null;
        linearLayout.addView(linearLayoutMed1, params);
        linearLayout.addView(linearLayoutMed2, params);
        linearLayout.addView(linearLayoutMed3, params);
        linearLayout.addView(linearLayoutMed4, params);
        linearLayout.addView(linearLayoutMed5, params);
        linearLayout.addView(linearLayoutMed6, params);
        linearLayout.addView(linearLayoutMed7, params);
        linearLayout.addView(linearLayoutMed8, params);
        linearLayout.addView(linearLayoutMed9, params);
        linearLayout.addView(linearLayoutMed10,params);

        // Цвет основного окна
        linearLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));

        // Координата отображения окна по вертикали (размер высоты окна 5+5)
        int forVisRowInd=rowIndex+2;
        if (rowIndex>13)forVisRowInd=rowIndex-6;
        if (rowIndex>rows-12)forVisRowInd=rows-12;
        for (int i=0; i<forVisRowInd; i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ImageView imageViewN = new ImageView(this);
            imageViewN.setImageResource(R.drawable.kletkafree);
            tableRow.addView(imageViewN);
            tableUgledarWar.addView(tableRow, i);
        }
        tableUgledarWar.addView(linearLayout, params);


        // Выбор координаты по горизонтали (1 до 10)
        if (colIndex<=8)linearLayoutMedia=linearLayoutMed1;
        if (colIndex>=9&&colIndex<=16)linearLayoutMedia=linearLayoutMed2;
        if (colIndex>=17&&colIndex<=24)linearLayoutMedia=linearLayoutMed3;
        if (colIndex>=25&&colIndex<=32)linearLayoutMedia=linearLayoutMed4;
        if (colIndex>=33&&colIndex<=40)linearLayoutMedia=linearLayoutMed5;
        if (colIndex>=41&&colIndex<=48)linearLayoutMedia=linearLayoutMed6;
        if (colIndex>=49&&colIndex<=56)linearLayoutMedia=linearLayoutMed7;
        if (colIndex>=57&&colIndex<=64)linearLayoutMedia=linearLayoutMed8;
        if (colIndex>=65&&colIndex<=72)linearLayoutMedia=linearLayoutMed9;
        if (colIndex>=73)linearLayoutMedia=linearLayoutMed10;

        // запрос информаци из массива данных
        dataMapObj.setRow(rowIndex);
        dataMapObj.setColumn(colIndex);
        dataMapObj.dataSampling();
        String textInfo=dataMapObj.getTextInfo();

        // постройка окна выбора фото
        linearLayoutMedia.setLayoutParams(params2);
        // Название объекта
        TextView textUl=new TextView(this);
        textUl.setLayoutParams(paramsText);
        textUl.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ArtifaktElement-Regular.ttf"));
        textUl.setTextSize(16);
        textUl.setTextColor(Color.argb(255,255,255,0));
        textUl.setPadding(10,10,10,10);
        textUl.setText(textInfo);

        linearLayoutUl.setLayoutParams(params);

        linearLayoutUl.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        linearLayoutUl.addView(textUl);
        linearLayoutMedia.addView(linearLayoutUl, params3);

        linearLayoutMedia.setBackgroundColor(Color.argb(255,0,0,0));
        linearLayoutMedia.setPadding(10,10,10,10);

        // Загрузка маленьких фото в linearLayoutMedia
        String[] linkImagesArray=dataMapObj.getLinksArrayS();
        String[] linkImagesArrayB=dataMapObj.getLinksArrayB();
        for (int i=0; i<linkImagesArray.length; i++){
            ImageView imageViewMedia=new ImageView(this);
            imageViewMedia.setLayoutParams(params);
            imageViewMedia.setPadding(10,10,10,10);

            Picasso.get()
                    .load(linkImagesArray[i])
                    .placeholder(R.drawable.loading02s)
                    .error(R.drawable.errorloadimage3)
                    .into(imageViewMedia, new Callback() {       // с обработкой ошибок
                        @Override
                        public void onSuccess() {
                            Log.d("error-log", "onSuccess");
                        }
                        @Override
                        public void onError(Exception e) {
                            Log.d("error-log", e.getMessage());
                        }
                    });
            int indexArrayB = i;
            // слушатели нажатия на маленькие фото
            imageViewMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    visibleMedBigAct(textInfo, linkImagesArrayB[indexArrayB]);
                }
            });
            linearLayoutMed.addView(imageViewMedia);
        }
        if (linkImagesArray.length==1)linearLayoutMedia.addView(linearLayoutMed, params3);
        else linearLayoutMedia.addView(linearLayoutMed, params);

        linearLayoutMed.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

    }
    // Отображение большого изображения в новой активности
    public void visibleMedBigAct(String textInfo, String linkBig){
        Intent intent=new Intent(this, visibleBigActivity.class);
        intent.putExtra("textInfo", textInfo);
        intent.putExtra("linkBig", linkBig);
        startActivity(intent);
    }

    // Построение карты - попаданий, рарушений, пустых клеток
    public void createMapUgledar() {
        int cinfo=0; // счетчик, используется для отображения наличия новостей
        // удаление слушателя нажатия на tableUgledarWar
        tableUgledarWar.setOnClickListener(null);
        // Удаление всех элементов из tableUgledarWar
        tableUgledarWar.removeAllViews();
        for (int i = 0; i < rows; i++) {
            // Создание строки
            TableRow tableRow = new TableRow(this);
            // Параметры для строки
            tableRow.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            // заполнение карты
            for (int j = 0; j < colums; j++) {
                // Создание картинки пустой клетки
                ImageView imageViewN = new ImageView(this);
                // Создание картинки для бомбы
                ImageView imageViewB = new ImageView(this);
                // Создание картинки для разрушений
                ImageView imageViewR = new ImageView(this);
                // Создание картинки для захоронений
                ImageView imageViewZ = new ImageView(this);
                // Создание картинки для наличия новостей
                ImageView imageViewI = new ImageView(this);
                // Привязка прозрачной картинки
                imageViewN.setImageResource(R.drawable.kletkafree);
                // Привязка картинки бомбы
                imageViewB.setImageResource(R.drawable.bombared2n);
                // Привязка картинки разрушений
                imageViewR.setImageResource(R.drawable.kletkar10v21gold);
                // Привязка картинки захоронений
                imageViewZ.setImageResource(R.drawable.zahoron2gold);
                // Привязка картинки наличия новостей
                imageViewI.setImageResource(R.drawable.stinfo2);
                // Запрос по координатам информации
                dataMapObj.setRow(i);
                dataMapObj.setColumn(j);
                dataMapObj.dataSampling();
                // извлечение информации о попадании
                switch (dataMapObj.getHitBRN()) {
                    // если попала бомба
                    case "b": {
                        // установить картинку - бомба
                        tableRow.addView(imageViewB, j);
                        int rowIndex = i;
                        int colIndex = j;
                        // назначить картинке слушателя нажатий
                        imageViewB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // переход на отображение фото с места попадания
                                visibleMedia(rowIndex, colIndex);
                                // временно для контроля нажжатия
                                Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                        Integer.valueOf(colIndex).toString());
                            }
                        });

                        break;
                    }
                    // если есть разрушеня
                    case "r":{
                        // установить картинку - разрушения
                        tableRow.addView(imageViewR, j);
                        int rowIndex = i;
                        int colIndex = j;
                        // назначить картинке слушателя нажатий
                        imageViewR.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // переход на отображение фото с места попадания
                                visibleMedia(rowIndex, colIndex);
                                // временно для контроля нажжатия
                               Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                       Integer.valueOf(colIndex).toString());
                            }
                        });
                        break;
                    }
                    // если нажать инструкция
                    case "ins":{
                        // установить пустую картинку
                        tableRow.addView(imageViewN, j);
                        int rowIndex = i;
                        int colIndex = j;
                        // назначить картинке слушателя нажатий
                        imageViewN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // переход на отображение фото в новой активити
                                visibleMedBigAct(INSTRUKC, INSTRUKURL);
                                // временно для контроля нажжатия
                                Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                        Integer.valueOf(colIndex).toString());
                            }
                        });
                        break;
                    }
                    // если нажать территориальная принадлежность
                    case "tpr":{
                        // установить пустую картинку
                        tableRow.addView(imageViewN, j);
                        int rowIndex = i;
                        int colIndex = j;
                        // назначить картинке слушателя нажатий
                        imageViewN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // переход на отображение фото в новой активити
                                visibleMedBigAct(TERPRIN, TERPRINURL);
                                // временно для контроля нажжатия
                                Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                        Integer.valueOf(colIndex).toString());
                            }
                        });
                        break;
                    }
                    // если нажать телеграмм
                    case "tlg":{
                        // установить пустую картинку
                        tableRow.addView(imageViewN, j);
                        int rowIndex = i;
                        int colIndex = j;
                        // назначить картинке слушателя нажатий
                        imageViewN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // переход в телеграмм
                                Uri adress=Uri.parse(TELEGRAM);
                                Intent intent=new Intent(Intent.ACTION_VIEW, adress);
                                startActivity(intent);
                                // временно для контроля нажжатия
                                Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                        Integer.valueOf(colIndex).toString());
                            }
                        });
                        break;
                    }
                    // если нажать о приложении
                    case "avt":{
                        // установить пустую картинку
                        tableRow.addView(imageViewN, j);
                        int rowIndex = i;
                        int colIndex = j;
                        // назначить картинке слушателя нажатий
                        imageViewN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // переход на отображение фото в новой активити
                                visibleMedBigAct(AVTUGLWAR,AVTUGLWARURL);
                                // временно для контроля нажжатия
                                Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                        Integer.valueOf(colIndex).toString());
                            }
                        });
                        break;
                    }
                    // если нажать новости
                    case "inf":{
                        cinfo++;
                        if (cinfo==2&&isNewInfo){
                            // установить картинку наличия новостей
                            tableRow.addView(imageViewI, j);
                            int rowIndex = i;
                            int colIndex = j;
                            // назначить картинке слушателя нажатий
                            imageViewI.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // переход на отображение фото с места попадания
                                    //visibleMedia(rowIndex, colIndex);
                                    // временно для контроля нажжатия
                                    Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                            Integer.valueOf(colIndex).toString());
                                }
                            });
                        }else {
                            // установить пустую картинку
                            tableRow.addView(imageViewN, j);
                            int rowIndex = i;
                            int colIndex = j;
                            // назначить картинке слушателя нажатий
                            imageViewN.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // переход на отображение фото с места попадания
                                    //visibleMedia(rowIndex, colIndex);
                                    // временно для контроля нажжатия
                                    Log.d("coord", Integer.valueOf(rowIndex).toString() + "-" +
                                            Integer.valueOf(colIndex).toString());
                                }
                            });
                        }
                        break;
                    }
                    // если нажать захоронение
                    case "z":{
                        // установить картинку захоронение
                        tableRow.addView(imageViewZ, j);
                        int rowIndex = i;
                        int colIndex = j;
                        // назначить картинке слушателя нажатий
                        imageViewZ.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // переход на отображение фото с места попадания
                                visibleMedia(rowIndex, colIndex);
                                // временно для контроля нажжатия
                                Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                        Integer.valueOf(colIndex).toString());
                            }
                        });
                        break;
                    }

                    // если ничего не известно
                    case "n": {
                        // установить прозрачную картинку
                        tableRow.addView(imageViewN, j);
                        int rowIndex = i;
                        int colIndex = j;
                        // назначить картинке слушателя нажатий

                        imageViewN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // переход на отображение фото с места нажатия
                                //visibleMedia(colIndex, rowIndex);
                                // временно для контроля нажжатия
                                Log.d("coord", Integer.valueOf(rowIndex).toString()+"-"+
                                        Integer.valueOf(colIndex).toString());
                            }
                        });

                        break;
                    }
                }

            }
            tableUgledarWar.addView(tableRow, i);

        }

    }
    // Проверка версии в девайсе
    public void continVersion(){
        isDownVer=false;
        isDownCount=false;

        // Загрузка из девайса количества просмотренных новостей
        cNewInfoDev=fileToTxt(countInfoDev).replace("\n","");
        // Считывание информации из загруженного из сети файла countInfoWeb.txt
        cNewInfoWeb=fileToTxt(countInfoWeb).replace("\n","");
        // Проверка на наличие новостей
        countNewInfo=Integer.parseInt(cNewInfoWeb)-Integer.parseInt(cNewInfoDev); // количество новых новостей
        Log.d("cnewinfo", String.valueOf(countNewInfo));
        if (countNewInfo>0) isNewInfo=true; // есть новые новости
        else isNewInfo=false;               // нет новых новостей

        // Загрузка из девайса версии
        verDev = fileToTxt(versionDev);
        // Считывание информации из загруженного из сети файла VersionWeb.txt
        verWeb=fileToTxt(versionWeb);
        //Toast.makeText(MainUgledarWar.this, "Download versionWeb.txt Completed", Toast.LENGTH_SHORT).show();
        //textViewDown.setText(verDev + " " + verWeb);
        // Проверка на равенство версий
        Log.d("verDW", verDev+"---"+verWeb);
        if (verDev.equals(verWeb)){
            // Если версии равны, то ничего не загружать и выполнять startProg2
            textViewDown.setText("Обновления не требуются...");
            startProg2();

        }else {
            //textViewDown.setText("Требуются обновления!");
            // Если версии не равны
            // старую версию version.txt удаляем
            File fileVersionDev=new File(filePath, versionDev);
            fileVersionDev.delete();

            // создаем новый файл version.txt
            fileVersionDev=new File(filePath, versionDev);
            saveNewFile(fileVersionDev,verWeb);
            // удаление  старого файла с данными
            File fileDataWeb=new File(filePath,dataWeb);
            fileDataWeb.delete();
            // загрузка нового файла данных
            textViewDown.setText("Требуются обновления! Загрузка...");
            subPath=dataWeb;
            downRefIDdat=file_download(urlWebdata, subPath);
        }

    }

    // Загрузка из сети
    public long file_download(String url, String subPath) {

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        //Ограничить типы сетей, по которым может продолжаться эта загрузка.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Установите, может ли эта загрузка продолжаться через соединение в роуминге.
        request.setAllowedOverRoaming(false);
        // Установите заголовок этой загрузки, который будет отображаться в уведомлениях.
        request.setTitle("Document_title");
        //Установите локальное место назначения для загруженного файла на путь в каталоге внешних файлов приложения
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, subPath);
        // Ставим в очередь новую загрузку и сохраняем тот же referenceId
        // enqueue помещает запрос на загрузку в очередь.
        return downloadReferenceID = downloadManager.enqueue(request);
    }
    // Внутри приемника мы просто проверяем, предназначена ли полученная трансляция для нашей загрузки,
    // сопоставляя полученный идентификатор загрузки с нашей загрузкой в очереди
    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Получение идентификатора загрузки, полученного с трансляцией
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Проверяем, соответствует ли полученная трансляция нашей загрузке в очереди, совпадая с идентификатором загрузки
            if (id==downRefIDver){
                isDownVer=true;
                if (isDownCount) continVersion();
            }
            if (id==downRefIDcouInfo){
                isDownCount=true;
                if (isDownVer) continVersion();
            }
            if (id==downRefIDdat)startProg2();

        }
    };


    // Загрузка из файла в переменную String из DIRECTORY_DOWNLOADS
    public String fileToTxt(String filename) {

        File file = new File(filePath, filename);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    // Подготовка начальных файлов прорисовка карты
    public void initializeFrame(FrameLayout frameLayout){
        // Удалени всех дочерних view
        frameLayout.removeAllViews();
        ScrollView scrollView=new ScrollView(this);
        HorizontalScrollView horizontalScrollView=new HorizontalScrollView(this);
        tableUgledarWar=new TableLayout(this);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        horizontalScrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tableUgledarWar.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.addView(scrollView);
        scrollView.addView(horizontalScrollView);
        horizontalScrollView.addView(tableUgledarWar);
        tableUgledarWar.setBackgroundResource(R.drawable.ugledarmap2v2);
    }
    // Проверка на наличие файлов версии, если нет, то создать начальный пустой файл версии и данных
    public void checkFileVer() {
        File fileVerDev=new File(filePath,versionDev);
        File fileDataWeb=new File(filePath, dataWeb);
        File fileCountInfo=new File(filePath, countInfoDev);
        File fileDataInfo=new File(filePath, dataInfo);
        if (!fileVerDev.exists()){
            saveNewFile(fileVerDev,newText);
        }
        if (!fileDataWeb.exists()){
            saveNewFile(fileDataWeb,newText);
        }
        if (!fileDataInfo.exists()){
            saveNewFile(fileDataInfo, newText);
        }
        if (!fileCountInfo.exists()){
            saveNewFile(fileCountInfo, "0");
        }

    }

    // Создание начального пустого файла
    public void saveNewFile(File fileName, String text){
        try {
            FileWriter writer=new FileWriter(fileName);
            writer.append(text);
            writer.flush();
            writer.close();
            //Toast.makeText(MainUgledarWar.this, "Сохранено!", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(MainUgledarWar.this, "НЕ Сохранено!", Toast.LENGTH_SHORT).show();

        }
    }

    // Титульная страница
    public void titleScreen(){

        // Удаление всех элемементов
        frameLayout.removeAllViews();
        frameLayout.setBackgroundResource(R.drawable.ugledar_war0);
        // корневой layout
        LinearLayout linearLayoutTitle=new LinearLayout(this);
        linearLayoutTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayoutTitle.setOrientation(LinearLayout.VERTICAL);
        frameLayout.addView(linearLayoutTitle);
        // Верхний layout
        LinearLayout linearLayoutTop=new LinearLayout(this);
        linearLayoutTop.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.2f));
        linearLayoutTitle.addView(linearLayoutTop);
        // средний для картинки
        LinearLayout linearLayoutSred=new LinearLayout(this);
        linearLayoutSred.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.4f));
        linearLayoutSred.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutTitle.addView(linearLayoutSred);

        // средний левый
        LinearLayout linearLayoutSredLev=new LinearLayout(this);
        linearLayoutSredLev.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,0.1f));
        linearLayoutSred.addView(linearLayoutSredLev);
        // средний для картинки
        LinearLayout linearLayoutSrImage=new LinearLayout(this);
        linearLayoutSrImage.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,0.8f));
        // - установить лого
        ImageView imageViewLogo=new ImageView(this);
        imageViewLogo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imageViewLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageViewLogo.setImageResource(R.drawable.titlelogo2);
        linearLayoutSrImage.addView(imageViewLogo);
        linearLayoutSred.addView(linearLayoutSrImage);

        // средний правый
        LinearLayout linearLayoutSrPr=new LinearLayout(this);
        linearLayoutSrPr.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,0.1f));
        linearLayoutSred.addView(linearLayoutSrPr);
        // разрыв перед текстом
        LinearLayout linearLayoutMarg=new LinearLayout(this);
        linearLayoutMarg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.1f));
        linearLayoutTitle.addView(linearLayoutMarg);
        // для текста
        LinearLayout linearLayoutText=new LinearLayout(this);
        linearLayoutText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.2f));
        linearLayoutText.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        linearLayoutTitle.addView(linearLayoutText);
        // текст
        TextView textView=new TextView(this);
        textView.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ArtifaktElement-Regular.ttf"),Typeface.ITALIC);
        textView.setTextSize(16);
        textView.setTextColor(Color.argb(255,255,255,0));
        textView.setText("Актуальная информация о местах и степени\n    разрушений в г.Угледар, Донецкой обл.");// --- установить текст
        linearLayoutText.addView(textView);
        // разрыв
        LinearLayout linearLayoutMar2=new LinearLayout(this);
        linearLayoutMar2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.05f));
        linearLayoutTitle.addView(linearLayoutMar2);
        // для загрузки
        linearLayoutDown=new LinearLayout(this);
        linearLayoutDown.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.15f));
        linearLayoutDown.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        linearLayoutDown.setOrientation(LinearLayout.VERTICAL);
        linearLayoutTitle.addView(linearLayoutDown);

        // нижний layout
        LinearLayout linearLayoutBot=new LinearLayout(this);
        linearLayoutBot.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0,0.05f));
        linearLayoutTitle.addView(linearLayoutBot);

    }

    public void politikKonf(){
        //
            LinearLayout linearLayoutPK=new LinearLayout(this);
            linearLayoutPK.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayoutPK.setOrientation(LinearLayout.VERTICAL);
            linearLayoutPK.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

            TextView textViewPK=new TextView(this);
            textViewPK.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textViewPK.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ArtifaktElement-Regular.ttf"));
            textViewPK.setTextSize(12);
            textViewPK.setTextColor(Color.argb(255,255,255,255));
            textViewPK.setText("Политика конфеденциальности:");
            linearLayoutPK.addView(textViewPK);

            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ArtifaktElement-Regular.ttf"));
            button.setTextSize(12);
            button.setTextColor(Color.argb(255,255,255,255));
            //button.setBackgroundColor(Color.argb(255, 80,80,80));
            button.setText(" Принять и продолжить ");
            linearLayoutPK.addView(button);

            TextView texViPolUrl = new TextView(this);
            texViPolUrl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            texViPolUrl.setTextColor(Color.argb(255,30,144,255));
            texViPolUrl.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/ArtifaktElement-Regular.ttf"));
            texViPolUrl.setTextSize(12);
            texViPolUrl.setText(polKonUrl);
            linearLayoutPK.addView(texViPolUrl);

            linearLayoutDown.removeAllViews();
            linearLayoutDown.addView(linearLayoutPK);
            // добавляем текст и кнопку


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = shPref.edit();
                    editor.putBoolean(politik, true);
                    editor.commit();
                    workProg();
                }
            });

            texViPolUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri adress=Uri.parse(polKonUrl);
                    Intent intent=new Intent(Intent.ACTION_VIEW, adress);
                    startActivity(intent);
                }
            });


    }

    // Отключение приемника при закрытии приложения
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }
    // Обработка нажатия кнопки назад
    @Override
    public void onBackPressed() {
        switch (levelContext){
            case 0:{super.onBackPressed();break;}
            case 1:{createMapUgledar();levelContext=0;break;}
            case 2:{initializeFrame(frameLayout);
                createMapUgledar();
                levelContext=0;
                break;}
        }
    }
}