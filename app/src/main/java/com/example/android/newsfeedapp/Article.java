package com.example.android.newsfeedapp;

class Article {
    private String mTitle;
    private String mSection;
    private String mUrl;
    private String mAuthor;
    private String mDate;

    Article(String title, String section, String url, String author, String date) {
        mTitle = title;
        mSection = section;
        mUrl = url;
        mAuthor = author;
        mDate = date;
    }
    String getTitle() {return mTitle;}
    String getSection() {return mSection;}
    String getUrl() {return mUrl;}
    String getAuthor() {return mAuthor;}
    String getDate() {return mDate;}
}
