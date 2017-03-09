package ru.groups.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "callback")
public class VkCallBackController {

    @RequestMapping(value = "get", method = RequestMethod.POST)
    public String get() {
        return "a975253d";
    }
}
