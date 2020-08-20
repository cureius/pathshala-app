package com.jugaru.pathshala.classInterface;

public class ProfileHolder {
    String DateOfBirth ;
    String FirstName ;
    String LastName ;
    String about ;
    String city ;
    String college ;
    String email ;
    String occupation ;
    String phone ;
    String profile_Url ;
    String school ;
    String username ;

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_Url() {
        return profile_Url;
    }

    public void setProfile_Url(String profile_Url) {
        this.profile_Url = profile_Url;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ProfileHolder{" +
                "DateOfBirth='" + DateOfBirth + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", about='" + about + '\'' +
                ", city='" + city + '\'' +
                ", college='" + college + '\'' +
                ", email='" + email + '\'' +
                ", occupation='" + occupation + '\'' +
                ", phone='" + phone + '\'' +
                ", profile_Url='" + profile_Url + '\'' +
                ", school='" + school + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
