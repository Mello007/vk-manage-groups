package ru.groups.service.shops;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.Shop;
import ru.groups.entity.GroupVk;
import ru.groups.entity.Product;
import ru.groups.service.GroupService;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShopService {

    @Autowired GroupService groupService;
    @Autowired SessionFactory sessionFactory;

    @Transactional
    public void addNewShopToGroup(Shop shop, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.setShop(new Shop(shop));
        addShopMessages(groupVk);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    @Transactional
    // we need to add one object to list, I don't know how
    public void addNewProductToShopInGroup(Product product, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.getShop().getProducts().add(product);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    @Transactional
    private void addShopMessages(GroupVk groupVk){
        Shop shop = groupVk.getShop();
        addAsksAboutPaymentMethod(shop);
        addAsksAboutShop(shop);
        addAsksAboutShopAddress(shop);
        addAsksAboutShopTime(shop);
        sessionFactory.getCurrentSession().merge(shop);
    }


    private void addAsksAboutPaymentMethod(Shop shop){
        List<String> asksAboutPaymentMethod = new ArrayList<>();
        asksAboutPaymentMethod.add("способы оплаты");
        asksAboutPaymentMethod.add("оплата");
        shop.setAsksAboutPaymentMethod(asksAboutPaymentMethod);
    }

    private void addAsksAboutShopTime(Shop shop){
        List<String> asksAboutShopTime = new ArrayList<>();
        asksAboutShopTime.add("время");
        asksAboutShopTime.add("когда работает магазин");
        asksAboutShopTime.add("во сколько можно");
        asksAboutShopTime.add("работа магазина");
        shop.setAsksAboutShopTime(asksAboutShopTime);
    }

    private void addAsksAboutShopAddress(Shop shop){
        List<String> asksAboutShopAddress = new ArrayList<>();
        asksAboutShopAddress.add("адрес");
        asksAboutShopAddress.add("где находится магазин");
        asksAboutShopAddress.add("как проехать к Вам");
        shop.setAsksAboutShopAddress(asksAboutShopAddress);
    }


    private void addAsksAboutShop(Shop shop){
        List<String> asksAboutShopInformation = new ArrayList<>(3);
        asksAboutShopInformation.add("Магазин");
        asksAboutShopInformation.add("информация о магазине");
        asksAboutShopInformation.add("все о магазине");
        shop.setAsksAboutShopInformation(asksAboutShopInformation);
    }

    private String answerAboutShop(Shop shop){
        return "Информация о магазине\n " +
                shop.getShopName() + "\n" +
                shop.getShopTimeOfWork() + "\n" +
                shop.getShopAddress() + "\n" +
                shop.getShopDescription() + "\n";
    }
}
