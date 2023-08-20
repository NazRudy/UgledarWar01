package com.nazrudy.ugledarwar01;

import android.util.Log;

import java.util.ArrayList;

// Класс для получения данных из файла dataMap по координатам row, column
public class DataMap {
    final String datWeb;   // строка с данными
    int row;                // координата строки карты
    int column;             // координата столбца карты
    String[][] dataMaparray;// массив с данными карты
    String textInfo;        // информация о вібранном месте
    String hitBRN;         // информация о попадании
    String[] linksArrayS;   // список ссылок на маленькие фото
    String[] linksArrayB;   // список ссілок на большие фото
    final int rows=64;
    final int colums=81;

    // конструктор с входными данными + инициализация и заполнение массива
    public DataMap(String datWeb) {
        this.datWeb = datWeb;

    }
    // входящая перемення координаты строки
    public void setRow(int row) {
        this.row = row;
    }

    // входящая переменная координаты столбца
    public void setColumn(int column) {
        this.column = column;
    }

    // исходящая переменная информации о выбранном месте
    public String getTextInfo() {
        return textInfo;
    }

    // исходящая информация о попадании
    public String getHitBRN() {
        return hitBRN;
    }

    // исходящий массив ссылок на фото
    public String[] getLinksArrayS() {
        return linksArrayS;
    }

    public String[] getLinksArrayB() {
        return linksArrayB;
    }

    // создание и заполнение массива
    public void createArrayMap() {
        String[] dataMapAr=datWeb.split("#");

        for (int i=0; i<dataMapAr.length; i++){
        String[] data=dataMapAr[i].split(",");
        int row=Integer.parseInt(data[0].substring(1));
        int col=Integer.parseInt(data[1]);
        dataMaparray[row][col]=data[2];
        }
        //Log.d("array", dataMapAr[0]+ " "+Integer.valueOf(dataMapAr.length).toString());
        // Log.d("array", dataMapAr2[0]+ " "+Integer.valueOf(dataMapAr.length).toString());

    }
    // выборка данных на основе входящих col и row
    public void dataSampling(){
        String[] data = dataMaparray[row][column].split("!");
        for (int i=0; i<3; i++) {
            Log.d("arrayD", data[i]);
        }
        hitBRN=data[0];
        textInfo=data[1];
        String linksS=data[2];
        String linksB=data[3];
        linksArrayS=null;
        linksArrayS=linksS.split(";");
        linksArrayB=null;
        linksArrayB=linksB.split(";");
    }

    // Инициализация массива перед использованием
    public void initializeArrayMap() {
        dataMaparray = new String[rows][colums];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < colums; col++) {
                dataMaparray[row][col] = "n!empty!empty!empty";
            }
        }
    }
}
