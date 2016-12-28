package ru.groups.service.help;


import org.springframework.beans.factory.annotation.Autowired;
import ru.groups.entity.GroupVk;
import ru.groups.service.ProductService;

import java.util.List;
import java.util.Random;

public class FindMessageHelper {

    @Autowired static ProductService productService;

    public static boolean messageFound(List<String> messagesToCheckAnswer, String message){

        String loverCaseMessage = message.toLowerCase();
        boolean messageFound = messagesToCheckAnswer.stream().
                anyMatch(badMessage -> loverCaseMessage.
                        contains(badMessage.
                                toLowerCase()));
        return messageFound;
    }

    public static String findMessageInListsWithAnswers(String message, GroupVk groupVk){
            Random random = new Random();
            if (messageFound(groupVk.getBadMessage(), message)) {
               return   groupVk.getStopWords().get(random.nextInt(groupVk.getStopWords().size()));
            } else if (messageFound(groupVk.getAnswerAtWelcomeMessage(), message)){
               return groupVk.getAnswerAtWelcomeMessage().get(random.nextInt(groupVk.getAnswerAtWelcomeMessage().size()));
            } else if(productService.findInformationAboutProduct(message, groupVk) != null){
                return productService.findInformationAboutProduct(message, groupVk);
            }
        return groupVk.getDefaultAnswer().get(random.nextInt(groupVk.getDefaultAnswer().size()));
    }
}
