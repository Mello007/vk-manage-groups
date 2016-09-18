package ru.groups.entity.typeOfMessages;


import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Entity @Table(name = "badmessages")
public class BadMessages extends BaseEntity{
    private String badMessage;
}