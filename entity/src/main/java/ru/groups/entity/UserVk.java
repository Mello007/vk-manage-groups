package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter @Setter
@Entity
@Table(name = "UserVk")
public class UserVk extends BaseEntity{
    private String userName;
    private String userLastName;
    private String userAccessToken;
    private String userId;
}