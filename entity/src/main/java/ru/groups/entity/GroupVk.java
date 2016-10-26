package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.typeOfMessages.AnswerAndAsk;


import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity @Table(name = "GroupVk")
public class GroupVk extends BaseEntity{

    private String groupId;
    private String groupName;
    private String photo50px;
    private String photo100px;
    private String accessToken;
    private String tempKeyOfPollingServer;
    private String addressOfPollingServer;
    private String numberOfLastAction;

    @ElementCollection private List<String> question;
    @ElementCollection private List<String> badMessage;
    @ElementCollection private List<String> answer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) @JoinTable private List<MessageVk> messagesOfGroup;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) @JoinTable private List<AnswerAndAsk> answerAndAsksMessages;
}