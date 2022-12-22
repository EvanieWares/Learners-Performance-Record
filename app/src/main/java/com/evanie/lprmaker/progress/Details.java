package com.evanie.lprmaker.progress;

public class Details {
    private String id;
    private String name;
    private String sex;
    private String arts;
    private String chichewa;
    private String english;
    private String maths;
    private String science;
    private String social;
    private String total;

    public Details(String id, String name, String sex, String arts, String chichewa, String english, String maths, String science, String social, String total) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.arts = arts;
        this.chichewa = chichewa;
        this.english = english;
        this.maths = maths;
        this.science = science;
        this.social = social;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getArts() {
        return arts;
    }

    public void setArts(String arts) {
        this.arts = arts;
    }

    public String getChichewa() {
        return chichewa;
    }

    public void setChichewa(String chichewa) {
        this.chichewa = chichewa;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getMaths() {
        return maths;
    }

    public void setMaths(String maths) {
        this.maths = maths;
    }

    public String getScience() {
        return science;
    }

    public void setScience(String science) {
        this.science = science;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
