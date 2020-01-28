package feige.pojo;

import javax.persistence.*;

@Table(name = "my_friends")
public class MyFriends {
    @Id
    private String id;

    @Column(name = "my_user_id")
    private String myUserId;

    @Column(name = "my_friends_user_id")
    private String myFriendsUserId;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return my_user_id
     */
    public String getMyUserId() {
        return myUserId;
    }

    /**
     * @param myUserId
     */
    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    /**
     * @return my_friends_user_id
     */
    public String getMyFriendsUserId() {
        return myFriendsUserId;
    }

    /**
     * @param myFriendsUserId
     */
    public void setMyFriendsUserId(String myFriendsUserId) {
        this.myFriendsUserId = myFriendsUserId;
    }
}