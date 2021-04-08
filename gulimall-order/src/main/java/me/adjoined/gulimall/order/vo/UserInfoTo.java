package me.adjoined.gulimall.order.vo;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserInfoTo {
    private String userId;
    private String userKey;

    private boolean newlyGeneratedUserKey = false;
}
