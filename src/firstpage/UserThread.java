package firstpage;

import firstpage.com.Data;

import java.io.IOException;
import java.io.ObjectInputStream;

public class UserThread extends Thread {
    private ObjectInputStream ois;


    public UserThread(ObjectInputStream ois){
        this.ois = ois;
    }

    @Override
    public void run() {
        while (true){
            try {
                Data data = (Data)ois.readObject();
                System.out.println(data.getProtocol());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
