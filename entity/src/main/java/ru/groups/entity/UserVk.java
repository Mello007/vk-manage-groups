package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
@Entity @Table(name = "UserVk")
public class UserVk extends BaseEntity{
    private String userName;
    private String userLastName;
    private String userAccessToken;
    private String userId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) @JoinTable  private List<GroupVk> userGroups;
}
