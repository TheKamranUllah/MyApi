package com.kamranullah.jsondataviavolley;

public class myData {

    private String name;
    private String field_email;
    private String field_url;
    private String id;
    private String field_logo;

    public myData(){}

    public myData(String name, String field_email, String field_url, String id, String field_logo) {
        this.name = name;
        this.field_email = field_email;
        this.field_url = field_url;
        this.id = id;
        this.field_logo = field_logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField_email() {
        return field_email;
    }

    public void setField_email(String field_email) {
        this.field_email = field_email;
    }

    public String getField_url() {
        return field_url;
    }

    public void setField_url(String field_url) {
        this.field_url = field_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField_logo() {
        return field_logo;
    }

    public void setField_logo(String field_logo) {
        this.field_logo = field_logo;
    }
}
