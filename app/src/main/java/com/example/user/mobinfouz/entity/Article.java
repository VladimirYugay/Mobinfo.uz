package com.example.user.mobinfouz.entity;


public class Article {
    private String headline, text, img_adress, date, url;
    public String adv;

    public void setHeadline(String s){
        if (s != null){
            headline = s;
        }else{
            headline = "Нет данных";
        }
    }

    public void setText(String s){
        if (s != null && s.length() > 16){
            int begin = 0;
            int end = s.length() - 15;
            text = s.substring(begin, end);
        }else {
            text = "Нет данных";
        }
    }

    public void setImg_adress(String s){
        if (s != null){
            img_adress = s;
        }else{
            img_adress = null;
        }
    }

    public void setDate(String s){
        if (s != null){
            date = s;
        }else{
            date = "000000";
        }
    }

    public void setUrl(String s){
        if (s != null){
            url = s;
        }else{
            url = "http://mobinfo.uz";
        }
    }

    public String getHeadline(){
        return headline;
    }

    public String getText(){
        return  text;
    }

    public String getImg_adress(){
        return img_adress;
    }

    public String getDate(){
        return date;
    }

    public String getUrl(){
        return url;
    }
}
