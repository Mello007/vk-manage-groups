package ru.groups.service.shops;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.groups.entity.Shop;
import ru.groups.entity.GroupVk;
import ru.groups.entity.Product;
import ru.groups.service.GroupService;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShopService {

    @Autowired
    GroupService groupService;
    @Autowired SessionFactory sessionFactory;

    public void addNewShopToGroup(Shop shop, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
        groupVk.setShopAddress(shop.getShopAddress());
        groupVk.setShopName(shop.getShopName());
        groupVk.setShopTimeOfWork(shop.getShopTimeOfWork());
        groupVk.setShopDescription(shop.getShopDescription());
        sessionFactory.getCurrentSession().saveOrUpdate(groupVk);
    }

    // we need to add one object to list, I don't know how
    public void addNewProductToShopInGroup(Product product, String groupId){
        GroupVk groupVk = groupService.searchGroup(groupId);
    }

    public void addShopMessages(GroupVk groupVk){
        addAsksAboutPaymentMethod(groupVk);
        addAsksAboutShop(groupVk);
        addAsksAboutShopAddress(groupVk);
        addAsksAboutShopTime(groupVk);
        sessionFactory.getCurrentSession().merge(groupVk);
    }

    //There are two methods about payment methods: asks and answers

    private void addAsksAboutPaymentMethod(GroupVk groupVk){
        List<String> asksAboutPaymentMethod = new ArrayList<>();
        asksAboutPaymentMethod.add("способы оплаты");
        asksAboutPaymentMethod.add("оплата");
    }

    private String answerAboutPaymentMethod(GroupVk groupVk){
        return "Доступные способы оплаты: ";
    }



    //There are two methods about time of work: asks and answers

    private void addAsksAboutShopTime(GroupVk groupVk){
        List<String> asksAboutShopTime = new ArrayList<>();
        asksAboutShopTime.add("время");
        asksAboutShopTime.add("когда работает магазин");
        asksAboutShopTime.add("во сколько можно");
        asksAboutShopTime.add("работа магазина");
    }

    private String answerAboutTimeOfWorkOfShop(GroupVk groupVk){
        return "Время работы магазина: " + groupVk.getShopTimeOfWork();
    }

    //There are two methods about shop address: asks and answers

    private void addAsksAboutShopAddress(GroupVk groupVk){
        List<String> asksAboutShopAddress = new ArrayList<>();
        asksAboutShopAddress.add("адрес");
        asksAboutShopAddress.add("где находится магазин");
        asksAboutShopAddress.add("как проехать к Вам");
    }

    private String answerAboutShopAddress(GroupVk groupVk){
        return "Адрес магазина: " + groupVk.getShopAddress();
    }


    // There are two methods about shop: possible asks and answer.

    private void addAsksAboutShop(GroupVk groupVk){
        List<String> asksAboutShopInformation = new ArrayList<>();
        asksAboutShopInformation.add("Магазин");
        asksAboutShopInformation.add("информация о магазине");
        asksAboutShopInformation.add("все о магазине");
    }

    private String answerAboutShop(GroupVk groupVk){
        return "Информация о магазине\n " +
                "Название магазина: " + groupVk.getShopName() + "\n" +
                "Время работы магазина: " + groupVk.getShopTimeOfWork() + "\n" +
                "Адрес магазина" + groupVk.getShopAddress() + "\n" +
                "Время работы магазина" + groupVk.getShopDescription() + "\n";
    }
}
