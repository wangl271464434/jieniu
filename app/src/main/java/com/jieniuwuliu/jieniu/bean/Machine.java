package com.jieniuwuliu.jieniu.bean;

import java.util.List;

public class Machine {
    private String name = "";
    private String exp = "";
    private List<Type> list;

    public List<Type> getList() {
        return list;
    }

    public void setList(List<Type> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getExp() {
        return exp;
    }
    public void setExp(String exp) {
        this.exp = exp;
    }
   public static class Type{
        private String type = "";
        private String money = "";

       public String getType() {
           return type;
       }

       public void setType(String type) {
           this.type = type;
       }

       public String getMoney() {
           return money;
       }

       public void setMoney(String money) {
           this.money = money;
       }
   }
}
