package com.clauzon.clauzcliente.Clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private String name;
    private ArrayList<Producto> list=new ArrayList<>();

    public Category() {
    }

    public Category(String name, ArrayList<Producto> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Producto> getList() {
        return list;
    }

    public void setList(ArrayList<Producto> list) {
        this.list = list;
    }
}
