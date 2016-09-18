package ru.groups.entity.DTO;

import lombok.Getter;
import lombok.Setter;
import ru.groups.entity.UserVk;

@Getter @Setter


public class UserDTO {

    private String userName;
    private String userLastName;

    public UserDTO(UserVk userVk){
        this.userName = userVk.getUserName();
        this.userLastName = userVk.getUserLastName();
    }
}
