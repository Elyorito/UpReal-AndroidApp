package com.upreal.utils;

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
        if (soapObject.hasProperty("picture") && soapObject.getProperty("picture") != null)
            prod.setPicture(soapObject.getPropertyAsString("picture").toString());

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
            user.setId_address(Integer.parseInt(soapObject.getPropertyAsString("id_address")));
        user.setUsername(soapObject.getPropertyAsString("username"));
        if (soapObject.hasProperty("lastname") && soapObject.getProperty("lastname") != null)
            user.setLastname(soapObject.getPropertyAsString("lastname"));
        if (soapObject.hasProperty("phone") && soapObject.getProperty("phone") != null)
            user.setPhone(Integer.parseInt(soapObject.getPropertyAsString("phone")));
        if (soapObject.hasProperty("short_desc") && soapObject.getProperty("short_desc") != null)
            user.setShort_desc(soapObject.getPropertyAsString("short_desc"));
        if (soapObject.hasProperty("picture") && soapObject.getProperty("picture") != null)
            user.setPicture(soapObject.getProperty("picture").toString());

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
            store.setId_address(Integer.parseInt(soapObject.getProperty("id_address").toString()));
        if (soapObject.hasProperty("id_company") && soapObject.getProperty("id_company") != null)
            store.setId_company(Integer.parseInt(soapObject.getProperty("id_company").toString()));
        if (soapObject.hasProperty("picture") && soapObject.getProperty("picture") != null)
            store.setPicture(soapObject.getProperty("picture").toString());

        return store;
    }

    public static Article convertToArticle(SoapObject soapObject) {
        Article news = new Article();
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
            rate.setmId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("id_user") && soapObject.getProperty("id_user") != null)
            rate.setmId_user(Integer.parseInt(soapObject.getPropertyAsString("id_user").toString()));
        if (soapObject.hasProperty("mark") && soapObject.getProperty("mark") != null)
            rate.setmMark(Integer.parseInt(soapObject.getPropertyAsString("mark").toString()));
        if (soapObject.hasProperty("commentary") && soapObject.getProperty("commentary") != null)
            rate.setmCommentary(soapObject.getPropertyAsString("commentary").toString());
        if (soapObject.hasProperty("date") && soapObject.getProperty("date") != null)
            rate.setmDate(soapObject.getPropertyAsString("date").toString());
        if (soapObject.hasProperty("active") && soapObject.getProperty("active") != null)
            rate.setmActive(Integer.parseInt(soapObject.getPropertyAsString("active").toString()));

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
            lists.setL_public(Integer.parseInt(soapObject.getProperty("public").toString()));
        if (soapObject.hasProperty("type") && soapObject.getProperty("type") != null)
            lists.setType(Integer.parseInt(soapObject.getProperty("type").toString()));
        if (soapObject.hasProperty("nb_items") && soapObject.getProperty("nb_items") != null)
            lists.setNb_items(Integer.parseInt(soapObject.getProperty("nb_items").toString()));
        if (soapObject.hasProperty("id_user") && soapObject.getProperty("id_user") != null)
            lists.setId_user(Integer.parseInt(soapObject.getProperty("id_user").toString()));
        if (soapObject.hasProperty("date") && soapObject.getProperty("date") != null)
            lists.setDate(soapObject.getProperty("date").toString());

        return lists;
    }

    public static Items convertToItems(SoapObject soapObject) {
        Items items = new Items();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            items.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("id_list") && soapObject.getProperty("id_list") != null)
            items.setId_list(Integer.parseInt(soapObject.getProperty("id_list").toString()));
        if (soapObject.hasProperty("id_product") && soapObject.getProperty("id_product") != null)
            items.setId_product(Integer.parseInt(soapObject.getProperty("id_product").toString()));
        if (soapObject.hasProperty("id_user") && soapObject.getProperty("id_user") != null)
            items.setId_user(Integer.parseInt(soapObject.getProperty("id_user").toString()));

        return items;
    }

    public static Address convertToAddress(SoapObject soapObject) {
        Address address = new Address();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            address.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("address") && soapObject.getProperty("address") != null)
            address.setAddress(soapObject.getProperty("address").toString());
        if (soapObject.hasProperty("address_2") && soapObject.getProperty("address_2") != null)
            address.setAddress2(soapObject.getProperty("address_2").toString());
        if (soapObject.hasProperty("city") && soapObject.getProperty("city") != null)
            address.setCity(soapObject.getProperty("city").toString());
        if (soapObject.hasProperty("country") && soapObject.getProperty("country") != null)
            address.setCountry(soapObject.getProperty("country").toString());
        if (soapObject.hasProperty("postal_code") && soapObject.getProperty("postal_code") != null)
            address.setPostalCode(Integer.parseInt(soapObject.getProperty("postal_code").toString()));

        return address;
    }

    public static Specification convertToSpecification(SoapObject soapObject) {
        Specification spec = new Specification();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            spec.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("id_product") && soapObject.getProperty("id_product") != null)
            spec.setIdProduct(Integer.parseInt(soapObject.getProperty("id_product").toString()));
        if (soapObject.hasProperty("id_property") && soapObject.getProperty("id_property") != null)
            spec.setIdProperty(Integer.parseInt(soapObject.getProperty("id_property").toString()));
        if (soapObject.hasProperty("value") && soapObject.getProperty("value") != null)
            spec.setValue(soapObject.getProperty("value").toString());

        return spec;
    }

    public static StoreSell convertToStoreSell(SoapObject soapObject) {
        StoreSell obj = new StoreSell();
        if (soapObject.hasProperty("id") && soapObject.getProperty("id") != null)
            obj.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
        if (soapObject.hasProperty("price") && soapObject.getProperty("price") != null)
            obj.setPrice(Double.parseDouble(soapObject.getProperty("price").toString()));
        if (soapObject.hasProperty("id_product") && soapObject.getProperty("id_product") != null)
            obj.setIdProduct(Integer.parseInt(soapObject.getProperty("id_product").toString()));
        if (soapObject.hasProperty("id_store") && soapObject.getProperty("id_store") != null)
            obj.setIdStore(Integer.parseInt(soapObject.getProperty("id_store").toString()));

        return obj;
    }
}
