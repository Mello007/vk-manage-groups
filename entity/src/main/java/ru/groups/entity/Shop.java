package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.GroupVk;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Shop")
public class Shop extends BaseEntity {
    private String shopName;
    private String shopDescription;
    private String shopAddress;
    private String shopTimeOfWork;
    @ElementCollection private List<String> paymentMethods;

    public Shop(Shop shop) {
        this.shopName = "Название магазина: "+ shop.getShopName();
        this.shopDescription = "Время работы магазина: " + shop.getShopDescription();
        this.shopAddress = "Адрес магазина" + shop.getShopAddress();
        this.shopTimeOfWork = "Время работы магазина" +  shop.getShopTimeOfWork();
    }

    //Asks
    @ElementCollection private List<String> asksAboutShopTime;
    @ElementCollection private List<String> asksAboutShopAddress;
    @ElementCollection private List<String> asksAboutShopInformation;
    @ElementCollection private List<String> asksAboutPaymentMethod;

    //Product
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) @JoinTable private List<Product> products;
    @ElementCollection private List<String> asksAboutProduct;

}
