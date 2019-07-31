package com.ekozah.chatmock.data;


/**
 * this class contains the data of a chat user
 * first name, last name, id and thumb info
 */
public class ChatUser {

    long     id;
    String  firstName;
    String  lastName;
    String  thumb;



    public ChatUser() {
    }

    public ChatUser(long id, String firstName, String lastName, String thumb) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.thumb = thumb;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return firstName+" "+lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
