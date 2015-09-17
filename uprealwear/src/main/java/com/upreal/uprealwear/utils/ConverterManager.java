package com.upreal.uprealwear.utils;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Kyosukke on 27/07/2015.
 */
public class ConverterManager {

    public static Product convertToProduct(SoapObject soapObject) {
        Product prod = new Product();
        if (soapObject.hasProperty("name") && soapObject.getProperty("name") != null)
            prod.setName(soapObject.getProperty("name").toString());
        if (soapObject.hasProperty("ean") && soapObject.getProperty("ean") != null)
            prod.setEan(soapObject.getProperty("ean").toString());
        if (soapObject.hasProperty("brand") && soapObject.getProperty("brand") != null)
            prod.setBrand(soapObject.getProperty("brand").toString());
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            prod.setId(Integer.parseInt(soapObject.getProperty("id").toString()));

        return prod;
    }

    public static User convertToUser(SoapObject soapObject) {
        User user = new User();
        user.setEmail(soapObject.getProperty("email").toString());
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            user.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("firstname") && soapObject.getProperty("firstname") != null)
            user.setFirstname(soapObject.getProperty("firstname").toString());
        user.setId(Integer.parseInt(soapObject.getPropertyAsString("id")));
        if (soapObject.hasProperty("id_address") && soapObject.getProperty("id_address") != null)
            user.setIdAddress(Integer.parseInt(soapObject.getPropertyAsString("id_address")));
        user.setUsername(soapObject.getPropertyAsString("username"));
        if (soapObject.hasProperty("lastname") && soapObject.getProperty("lastname") != null)
            user.setLastname(soapObject.getPropertyAsString("lastname"));
        if (soapObject.hasProperty("phone") && soapObject.getProperty("phone") != null)
            user.setPhone(Integer.parseInt(soapObject.getPropertyAsString("phone")));
        if (soapObject.hasProperty("short_desc") && soapObject.getProperty("short_desc") != null)
            user.setShortDesc(soapObject.getPropertyAsString("short_desc"));
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            user.setActive(Integer.parseInt(soapObject.getProperty("active").toString()));

        return user;
    }

    public static Store convertToStore(SoapObject soapObject) {
        Store store = new Store();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            store.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("name") && soapObject.getProperty("name") != null)
            store.setName(soapObject.getProperty("name").toString());
        if (soapObject.hasProperty("website") && soapObject.getProperty("website") != null)
            store.setWebsite(soapObject.getProperty("website").toString());
        if (soapObject.hasProperty("id_address") && soapObject.getProperty("id_address") != null)
            store.setIdAddress(Integer.parseInt(soapObject.getProperty("id_address").toString()));
        if (soapObject.hasProperty("id_company") && soapObject.getProperty("id_company") != null)
            store.setIdCompany(Integer.parseInt(soapObject.getProperty("id_company").toString()));

        return store;
    }

    public static Article convertToArticle(SoapObject soapObject) {
        Article news = new Article();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            news.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("title") && soapObject.getProperty("title") != null)
            news.setTitle(soapObject.getPropertyAsString("title").toString());
        if (soapObject.hasProperty("body") && soapObject.getProperty("body") != null)
            news.setBody(soapObject.getPropertyAsString("body").toString());
/*        if (soapObject.hasProperty("creation") && soapObject.getProperty("creation") != null)
            news.setCreation(soapObject.getPropertyAsString("creation").toString());*/
        if (soapObject.hasProperty("type") && soapObject.getProperty("type") != null)
            news.setType(Integer.parseInt(soapObject.getPropertyAsString("type").toString()));
        if (soapObject.hasProperty("picture") && soapObject.getProperty("picture") != null)
            news.setPicture(soapObject.getPropertyAsString("picture").toString());

        return news;
    }

    public static Rate convertToRate(SoapObject soapObject) {
        Rate rate = new Rate();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            rate.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("id_user") && soapObject.getProperty("id_user") != null)
            rate.setIdUser(Integer.parseInt(soapObject.getPropertyAsString("id_user").toString()));
        if (soapObject.hasProperty("mark") && soapObject.getProperty("mark") != null)
            rate.setMark(Integer.parseInt(soapObject.getPropertyAsString("mark").toString()));
        if (soapObject.hasProperty("commentary") && soapObject.getProperty("commentary") != null)
            rate.setCommentary(soapObject.getPropertyAsString("commentary").toString());
        if (soapObject.hasProperty("date") && soapObject.getProperty("date") != null)
            rate.setDate(soapObject.getPropertyAsString("date").toString());
        if (soapObject.hasProperty("active") && soapObject.getProperty("active") != null)
            rate.setActive(Integer.parseInt(soapObject.getPropertyAsString("active").toString()));

        return rate;
    }

    public static Achievement convertToAchievement(SoapObject soapObject) {
        Achievement achievement = new Achievement();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            achievement.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("name") && soapObject.getProperty("name") != null)
            achievement.setName(soapObject.getPropertyAsString("name").toString());
        if (soapObject.hasProperty("desc") && soapObject.getProperty("desc") != null)
            achievement.setName(soapObject.getPropertyAsString("desc").toString());

        return achievement;
    }

    public static History convertToHistory(SoapObject soapObject) {
        History history = new History();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            history.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("id_user") && soapObject.getProperty("id_user") != null)
            history.setIdUser(Integer.parseInt(soapObject.getProperty("id_user").toString()));
        if (soapObject.hasProperty("action_type") && soapObject.getProperty("action_type") != null)
            history.setActionType(Integer.parseInt(soapObject.getProperty("action_type").toString()));
        if (soapObject.hasProperty("id_type") && soapObject.getProperty("id_type") != null)
            history.setIdType(Integer.parseInt(soapObject.getProperty("id_type").toString()));
        if (soapObject.hasProperty("id_target") && soapObject.getProperty("id_target") != null)
            history.setIdTarget(Integer.parseInt(soapObject.getProperty("id_target").toString()));
        if (soapObject.hasProperty("date") && soapObject.getProperty("date") != null)
            history.setDate(soapObject.getProperty("date").toString());

        return history;
    }

    public static Lists convertToLists(SoapObject soapObject) {
        Lists lists = new Lists();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            lists.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("name") && soapObject.getProperty("name") != null)
            lists.setName(soapObject.getProperty("name").toString());
        if (soapObject.hasProperty("public") && soapObject.getProperty("public") != null)
            lists.setIsPublic(Integer.parseInt(soapObject.getProperty("public").toString()));
        if (soapObject.hasProperty("type") && soapObject.getProperty("type") != null)
            lists.setType(Integer.parseInt(soapObject.getProperty("type").toString()));
        if (soapObject.hasProperty("nb_items") && soapObject.getProperty("nb_items") != null)
            lists.setNbItems(Integer.parseInt(soapObject.getProperty("nb_items").toString()));
        if (soapObject.hasProperty("id_user") && soapObject.getProperty("id_user") != null)
            lists.setIdUser(Integer.parseInt(soapObject.getProperty("id_user").toString()));
        if (soapObject.hasProperty("date") && soapObject.getProperty("date") != null)
            lists.setDate(soapObject.getProperty("date").toString());

        return lists;
    }

    public static Items convertToItems(SoapObject soapObject) {
        Items items = new Items();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            items.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("id_list") && soapObject.getProperty("id_list") != null)
            items.setIdList(Integer.parseInt(soapObject.getProperty("id_list").toString()));
        if (soapObject.hasProperty("id_product") && soapObject.getProperty("id_product") != null)
            items.setIdProduct(Integer.parseInt(soapObject.getProperty("id_product").toString()));
        if (soapObject.hasProperty("id_user") && soapObject.getProperty("id_user") != null)
            items.setIdUser(Integer.parseInt(soapObject.getProperty("id_user").toString()));

        return items;
    }
}
