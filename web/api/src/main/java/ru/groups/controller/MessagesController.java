package ru.groups.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.groups.entity.typeOfMessages.AnswerAndAsk;
import ru.groups.service.messages.AsqAndAnswerService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "messages", method = RequestMethod.POST, consumes = "application/json")
public class MessagesController {


    @Autowired AsqAndAnswerService asqAndAnswerService;

    @RequestMapping(value = "add_an_as")
    public void addAnAndAss(@RequestBody AnswerAndAsk answerAndAsk, @PathVariable String groupId) throws IOException {
        asqAndAnswerService.addAnswerAndAsk(answerAndAsk, groupId);
    }

    @RequestMapping(value = "delete_an_as")
    public void deleteAnAndAs(@RequestBody AnswerAndAsk answerAndAsk, @PathVariable String groupId) throws IOException {
        asqAndAnswerService.deleteAnswerAndAsk(answerAndAsk, groupId);
    }

    @RequestMapping(value = "change_an_as")
    public void changeAnAs(@RequestBody AnswerAndAsk answerAndAsk, @PathVariable String groupId) throws IOException {
        asqAndAnswerService.deleteAnswerAndAsk(answerAndAsk, groupId);
    }
}
