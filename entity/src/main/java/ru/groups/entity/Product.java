package ru.groups.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity @Table(name = "Product")
public class Product extends BaseEntity{
    private String nameOfGoods;
    private String priceOfGoods;
    private String countOfGoods;
}