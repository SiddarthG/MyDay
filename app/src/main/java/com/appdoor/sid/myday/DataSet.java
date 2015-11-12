package com.appdoor.sid.myday;

/**
 * Created by sid on 06/11/15.
 */
import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;


public class DataSet implements Serializable {

   String name, image;
    float actual_price,discount_percentage,effective_price;
    String discount;
    int rating;
    String city, location, description;

    public DataSet() {
        //data="N/A";
    }

    public DataSet(String name, String image, float discount_percentage,float actual_price,float effective_price,String discount,int rating,String city,String location,String description) {
        this.name=name;
        this.actual_price=actual_price;
        this.effective_price=effective_price;
        this.image = image;
        this.discount=discount;
        this.rating=rating;
        this.city=city;
        this.location=location;
        this.description=description;
        this.discount_percentage=discount_percentage;


    }

    public static Comparator<DataSet> PriceComparator = new Comparator<DataSet>() {

        public int compare(DataSet s1, DataSet s2) {
            float price1 = s1.effective_price;
            float price2 = s2.effective_price;

            //ascending order
            return (int) (price1-price2);

        }};

    public static Comparator<DataSet> RatingComparator = new Comparator<DataSet>() {

        public int compare(DataSet s1, DataSet s2) {
            float price1 = s1.rating;
            float price2 = s2.rating;

            //ascending order
            return (int) (price2-price1);

        }};

    public static Comparator<DataSet> DiscountComparator = new Comparator<DataSet>() {

        public int compare(DataSet s1, DataSet s2) {
            float price1 = s1.discount_percentage;
            float price2 = s2.discount_percentage;

            //ascending order
            return (int) (price2-price1);

        }};




}