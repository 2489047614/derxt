package com.iweb.derxt.sso.model.vo;

import lombok.Data;

@Data
public class UserModel {
    private String nikename;
    private Integer sex;
    private String city;
    private String province;
    private String country;
    private String headImgUrl;
    private String mobile;
    private String inviteUrl;
    private String school;
    private String area;
    private String grade;
    private String name;

//    private List<BillModel> billModelList;
}
