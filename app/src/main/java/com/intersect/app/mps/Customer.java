package com.intersect.app.mps;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 5/29/16.
 */
public class Customer extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private int vat_number;
    private String address;
    private String telephone;
    private String fax;
    private String email;
    private String city;
    private String updated;

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_state) {
        this.customer_status = customer_state;
    }

    private String customer_type;
    private String customer_status;

    public Customer()
    {}

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

   public static List<Customer> getAll()
    {
        Realm realm = App.Realm();
        return realm.where(Customer.class).findAll();
    }

    public int getVat_number() {
        return vat_number;
    }

    public void setVat_number(int vat_number) {
        this.vat_number = vat_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
