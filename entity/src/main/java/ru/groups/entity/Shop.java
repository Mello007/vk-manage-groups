package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.GroupVk;

@Getter
@Setter
public class Shop {
    private String shopName;
    private String shopDescription;
    private String shopAddress;
    private String shopTimeOfWork;

    public Shop(GroupVk groupVk){
        shopName = groupVk.getShopName();
        shopDescription = groupVk.getShopDescription();
        shopAddress = groupVk.getShopAddress();
        shopTimeOfWork = groupVk.getShopTimeOfWork();
    }
}
