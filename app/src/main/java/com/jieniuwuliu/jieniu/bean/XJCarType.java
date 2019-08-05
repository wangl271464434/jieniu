package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class XJCarType {

    /**
     * Brands : A
     * Brand : 奥迪
     * Models : ["奥迪Q2L新能源","奥迪A3","奥迪A4L","奥迪A6L","奥迪Q2L","奥迪Q3","奥迪Q5L","奥迪A6L新能源","奥迪Q4","奥迪A4","奥迪A6","奥迪Q5","奥迪RS 3","奥迪RS 4","奥迪RS 5","奥迪RS 6","奥迪RS 7","奥迪R8","奥迪TT RS","奥迪RS Q3","奥迪RSQ e-tron","奥迪e-tron","奥迪A3(进口)","奥迪S3","奥迪A4(进口)","奥迪A5","奥迪S4","奥迪S5","奥迪A6(进口)","奥迪S6","奥迪A7","奥迪S7","奥迪A8","奥迪Q7","奥迪Q7新能源","奥迪TT","奥迪TTS","奥迪A0","奥迪A1","奥迪S1","e-tron Concept","奥迪AI:ME","奥迪A6新能源(进口)","奥迪A7新能源","奥迪Aicon","奥迪e-tron GT","Prologue","奥迪A8新能源","奥迪A9","奥迪S8","allroad","奥迪Q2","奥迪SQ2","奥迪Q3(进口)","奥迪Q4(进口)","奥迪Q4新能源(进口)","奥迪TT offroad","h-tron quattro","奥迪Elaine","奥迪Q5(进口)","奥迪Q5新能源(进口)","奥迪SQ5","奥迪Q8","奥迪SQ7","奥迪SQ8","奥迪Q9","e-tron Vision Gran Turismo","quattro","奥迪PB18","奥迪R18","奥迪Urban","奥迪A2","奥迪80","奥迪A3新能源(进口)","奥迪Coupe","奥迪100","Crosslane Coupe","奥迪Cross","Nanuk"]
     * Imgurl : //car2.autoimg.cn/cardfs/series/g26/M0B/AE/B3/100x100_f40_autohomecar__wKgHEVs9u5WAV441AAAKdxZGE4U148.png
     */

    private String Brands;
    private String Brand;
    private String Imgurl;
    private List<String> Models;

    public String getBrands() {
        return Brands;
    }

    public void setBrands(String Brands) {
        this.Brands = Brands;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }

    public String getImgurl() {
        return Imgurl;
    }

    public void setImgurl(String Imgurl) {
        this.Imgurl = Imgurl;
    }

    public List<String> getModels() {
        return Models;
    }

    public void setModels(List<String> Models) {
        this.Models = Models;
    }
}
