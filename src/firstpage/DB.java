package firstpage;

import java.util.ArrayList;

public final class DB {
    private static ArrayList<User> users = new ArrayList<>();

    public static ArrayList<User> getUsers(){
        return users;
    }

    public static void addUser(User user){
        users.add(user);
    }

    public static void removeUser(User user){
        users.remove(user);
    }
}
