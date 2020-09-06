package com.jugaru.pathshala.classInterface;

public final class UserProfile {
    String FirstName ;
    String LastName ;
    String profile_Url ;
    String username ;
    String userId ;

    public UserProfile() {
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getProfile_Url() {
        return profile_Url;
    }

    public void setProfile_Url(String profile_Url) {
        this.profile_Url = profile_Url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", profile_Url='" + profile_Url + '\'' +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
