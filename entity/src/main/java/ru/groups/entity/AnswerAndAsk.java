package ru.groups.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter @Setter
@Entity @Table(name = "answer_and_ask")
public class AnswerAndAsk extends BaseEntity {
    private String messageAnswer;
    private String messageAsk;
}