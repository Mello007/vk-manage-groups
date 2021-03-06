package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity @Table(name = "MessageVk")
public class MessageVK extends BaseEntity{
    private String messageId;
    private String messageDate;
    private String messageBody;
    private String messageUserId;
    private String messageReply;
    private boolean messageAnswered;
}