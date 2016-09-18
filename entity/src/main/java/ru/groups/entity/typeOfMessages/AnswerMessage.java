package ru.groups.entity.typeOfMessages;


import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "answermessage")
public class AnswerMessage  extends BaseEntity{
    private String message;
}

