package ru.groups.service.help;


import org.springframework.beans.factory.annotation.Autowired;
import ru.groups.entity.GroupVk;
import ru.groups.service.shops.ProductService;
import ru.groups.service.shops.ShopService;

import java.util.List;
import java.util.Random;

public class FindMessageHelper {

    @Autowired static ProductService productService;
    @Autowired static ShopService shopService;

    public static boolean messageFound(List<String> messagesToCheckAnswer, String message){
        String loverCaseMessage = message.toLowerCase();
        return messagesToCheckAnswer.stream().
                anyMatch(badMessage -> loverCaseMessage.
                        contains(badMessage.
                                toLowerCase()));
    }

    public static String findMessageInListsWithAnswers(String message, GroupVk groupVk){
        Random random = new Random();
        boolean foundedBadMessage = messageFound(groupVk.getBadMessage(), message);
        boolean foundedWelcomeMessage = messageFound(groupVk.getWelcomeMessage(), message);
        String defaultAnswer = groupVk.getDefaultAnswer().get(random.nextInt(groupVk.getDefaultAnswer().size()));
        String askAboutProduct = productService.findAskAboutProduct(message, groupVk);
        String infoAboutProduct = productService.findInformationAboutProduct(message, groupVk);
        // I need add info about shop String askAboutShop =

        if (foundedBadMessage){
            return groupVk.getStopWords().get(random.nextInt(groupVk.getStopWords().size()));
        } else if (foundedWelcomeMessage){
            return groupVk.getAnswerAtWelcomeMessage().get(random.nextInt(groupVk.getAnswerAtWelcomeMessage().size()));
        } else if (askAboutProduct != null){

            //There is I need to add else note about product-asking (I don't know what, maybe Id) - its
            // need me that to catch other messages about this product
            return askAboutProduct;
        } else if (infoAboutProduct != null){
            return infoAboutProduct;
        }
        return defaultAnswer;
    }
}
