package ru.groups.entity.DTO;

import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.GroupVk;

@Getter
@Setter

public class ShopDTO {
    private String shopName;
    private String shopDiscription;
    private String shopAddress;
    private String shopTimeOfWork;

    public ShopDTO(GroupVk groupVk){
        shopName = groupVk.getShopName();
        shopDiscription = groupVk.getShowDescription();
        shopAddress = groupVk.getShopAddress();
        shopTimeOfWork = groupVk.getShopTimeOfWork();
    }
}
