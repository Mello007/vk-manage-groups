package ru.groups.entity;

import lombok.Getter;
import lombok.Setter;

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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) @JoinTable  private List<MessageVk> messagesOfGroup;
}
