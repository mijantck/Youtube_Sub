package com.mrsoftit.youtubesub.modle;

public class UserModle {
    String userName;
    String userId;
    String email;
    String UserImageUrl;
    String refarId;
    int cPaymentAmont;
    int coin;



    public UserModle() {
    }

    public UserModle(String userName, String userId, String email, String userImageUrl, String refarId, int cPaymentAmont, int coin) {
        this.userName = userName;
        this.userId = userId;
        this.email = email;
        UserImageUrl = userImageUrl;
        this.refarId = refarId;
        this.cPaymentAmont = cPaymentAmont;
        this.coin = coin;
    }
}
