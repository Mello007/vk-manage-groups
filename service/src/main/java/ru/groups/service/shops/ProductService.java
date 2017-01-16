package ru.groups.service.shops;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.groups.entity.GroupVk;
import ru.groups.entity.Product;
import ru.groups.entity.Shop;
import ru.groups.service.help.FindMessageHelper;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired SessionFactory sessionFactory;

    public String findInformationAboutProduct(String message, GroupVk groupVk){
        String informationAboutProduct = findProductFromMessage(message, groupVk);
        if (informationAboutProduct != null){
            return informationAboutProduct;
        } else if (findAskAboutProduct(message, groupVk.getShop()) != null){
            return findAskAboutProduct(message, groupVk.getShop());
        }
        return null;
    }

    public String findAskAboutProduct(String message, Shop shop){
        if (FindMessageHelper.messageFound(shop.getAsksAboutProduct(), message)){
            return "О каком товаре вы хотите узнать поподробнее?";
        } else return null;
    }

    @Transactional
    private String findProductFromMessage(String nameOfProduct, GroupVk groupVk){
         for (Product product : groupVk.getProducts()){
             if (product.getNameOfGoods().equals(nameOfProduct)){
                 return "Цена товара: " + product.getPriceOfGoods();
             }
         }
        return null;
    }

    @Transactional
    public void addAsksAboutProduct(Shop shop){
        List<String> asks = new ArrayList<>(11);
        asks.add("товар");
        asks.add("пред");
        asks.add("сколько стоит");
        asks.add("стоимость");
        asks.add("сколь");
        asks.add("какова");
        asks.add("цена");
        asks.add("price");
        asks.add("прайс");
        asks.add("наличие");
        asks.add("товар");
        shop.setAsksAboutProduct(asks);
        sessionFactory.getCurrentSession().merge(shop);
    }
}
