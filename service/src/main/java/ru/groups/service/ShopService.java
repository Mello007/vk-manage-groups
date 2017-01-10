package ru.groups.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.DTO.ShopDTO;
import ru.groups.entity.GroupVk;
import ru.groups.entity.Product;


@Service
public class ShopService {

    @Autowired GroupService groupService;
    @Autowired SessionFactory sessionFactory;

    public void addNewShopToGroup(ShopDTO shopDTO, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.setShopAddress(shopDTO.getShopAddress());
        groupVk.setShopName(shopDTO.getShopName());
        groupVk.setShopTimeOfWork(shopDTO.getShopTimeOfWork());
        groupVk.setShowDescription(shopDTO.getShopDescription());
        sessionFactory.getCurrentSession().saveOrUpdate(groupVk);
    }

    // we need to add one object to list, I dont know how
    public void addNewProductToShopInGroup(Product product, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
    }

    public ShopDTO getShopFromGroup(String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        return new ShopDTO(groupVk);
    }


    public String getTimeOfWorkOfShop(GroupVk groupVk){
        return "Время работы магазина: " + groupVk.getShopTimeOfWork();
    }

    public String getContactsShop(GroupVk groupVk){
        return groupVk.getShopAddress();
    }

}
