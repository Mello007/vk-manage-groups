package ru.groups.entity.typeOfMessages;


import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter @Setter
@Entity @Table(name = "answerandask")
public class AnswerAndAsk extends BaseEntity {
    private String messageAnswer;
    private String messageAsk;
}