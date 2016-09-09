package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter @Setter
@Entity @Table(name = "GroupVk")
public class GroupVk extends BaseEntity{
    private String groupId;
    private String groupName;
    private String photo50px;
    private String photo100px;
}
