package ru.groups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.groups.entity.Shop;
import ru.groups.entity.Product;
import ru.groups.service.shops.ShopService;

@RestController
@RequestMapping(value = "shop")
public class ShopsController {

    @Autowired ShopService shopService;

    @RequestMapping(value = "addProduct", method = RequestMethod.POST, consumes = "application/json")
    public void addProduct(@RequestBody Product product, String groupId){
        shopService.addNewProductToShopInGroup(product, groupId);
    }

    @RequestMapping(value = "addShop", method = RequestMethod.POST, consumes = "application/json")
    public void addShop(@RequestBody Shop shop, String groupId){
        shopService.addNewShopToGroup(shop, groupId);
    }
}
