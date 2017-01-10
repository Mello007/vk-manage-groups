package ru.groups.entity.DTO;

import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.GroupVk;

@Getter
@Setter
public class ShopDTO {
    private String shopName;
    private String shopDescription;
    private String shopAddress;
    private String shopTimeOfWork;

    public ShopDTO(GroupVk groupVk){
        shopName = groupVk.getShopName();
        shopDescription = groupVk.getShowDescription();
        shopAddress = groupVk.getShopAddress();
        shopTimeOfWork = groupVk.getShopTimeOfWork();
    }
}
