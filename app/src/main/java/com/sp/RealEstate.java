package com.sp;

public class RealEstate {
    private String realestatePrice = "";
    private String realestateAddress = "";
    private String realestateStatus = "";
    private String realestateType = "";
    private String realestateSize = "";
    private String realestateAgent = "";

    public String getPrice(){return realestatePrice;}
    public void setPrice(String realestatePrice){this.realestatePrice = realestatePrice;}

    public String getAddress(){return realestateAddress;}
    public void setAddress(String realestateAddress){this.realestateAddress = realestateAddress;}

    public String getType(){return realestateType;}
    public void setType(String realestateType){this.realestateType = realestateType;}

    public String getSize(){return realestateSize;}
    public void setSize(String realestateSize){this.realestateSize = realestateSize;}

    public String getStatus(){return realestateStatus;}
    public void setStatus(String realestateStatus){this.realestateStatus = realestateStatus;}

    public String getAgent(){return realestateAgent;}
    public void setAgent(String realestateAgent){this.realestateAgent = realestateAgent;}

//    public byte[] getImage(){return realestateImage;}
//    public void setImage(byte[] realestateImage){this.realestateImage = realestateImage;}


    @Override
    public String toString(){return (getAddress());}
}