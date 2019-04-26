package com.wolf.paras.readingfox;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Book {
    private String mTitle;
    private String[] mAuthor_name;
    private String mPublisherDate;
    private String mDescription;
    private String mPageCOunt;
    private String[] mCategoryList;
    private String mAverageRating;
    private String mRatingCount;
    private String mMaturityRating;
    private Bitmap mSmallThumbnail;
    private String mLanguage;
    private String mPreviewLink;
    private String mSaleblity;
    private String mPrice;
    private String mBuyLink;

    public Book(String title,String[] author_name,String description,String pageCount,String[] categoryList,String averageRating,String ratingCount,String maturityRating,String language,String previewLink,String saleblity,String price,String buyLink,Bitmap bmp){
        mTitle = title;
        mAuthor_name = author_name;
        mDescription = description;
        mPageCOunt = pageCount;
        mCategoryList = categoryList;
        mAverageRating = averageRating;
        mRatingCount = ratingCount;
        mMaturityRating = maturityRating;
        mSmallThumbnail = bmp;
        mLanguage = language;
        mPreviewLink = previewLink;
        mSaleblity = saleblity;
        mPrice = price;
        mBuyLink = buyLink;
    }




    public String getTitle(){
        return mTitle;
    }

    public String getAuthorName(){
        String AllauthorName = "";
        for(int i = 0;i<mAuthor_name.length;i++)
            AllauthorName = AllauthorName + mAuthor_name[i] + ", ";

        return AllauthorName;
    }


    public String getDescription(){
        return mDescription;
    }

    public String getPageCOunt(){
        return mPageCOunt;
    }

    public String getCategoryList(){
        String allCategory = "";
        for(int i = 0;i<mCategoryList.length;i++){
            allCategory = "Category: " + allCategory + mCategoryList[i] + ", ";

        }

        return allCategory;
    }

    public String getmAverageRating(){
        return "Rating: " + mAverageRating;
    }

    public String getRatingCount(){
        return "Rating Count: " + mRatingCount;
    }

    public String getMaturityRating(){
        return "Maturity Rating: " + mMaturityRating;
    }

    public Bitmap getSmallThumbnail(){
        return mSmallThumbnail;
    }

    public String getLanguage(){
        return "Language: " + mLanguage;
    }

    public String getPreviewLink(){
        return  mPreviewLink;
    }

    public String getSaleblity(){
        return  mSaleblity;
    }

    public  String getPrice(){
        return mPrice;
    }

    public String getmBuyLink(){
        return mBuyLink;
    }


}
