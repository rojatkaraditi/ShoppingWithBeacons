package com.example.discountapp;

import java.io.Serializable;

public class Item implements Serializable {
//        "_id": "5fd1866a52d077a7990f5c39",
//            "discount": 10,
//            "name": "Croissants",
//            "photo": "croissants.png",
//            "price": 2.79,
//            "region": "grocery",
//            "discountPrice": "2.51"
    String _id;
    int discount;
    String name;
    String photo;
    double price;
    String region;
    String discountPrice;

    @Override
    public String toString() {
        return "Item{" +
                "_id='" + _id + '\'' +
                ", discount=" + discount +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", price=" + price +
                ", region='" + region + '\'' +
                ", discountPrice='" + discountPrice + '\'' +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public int getDiscount() {
        return discount;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public double getPrice() {
        return price;
    }

    public String getRegion() {
        return region;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }
}
