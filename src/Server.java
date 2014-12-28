import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * Created by smjcm on 2014/12/19.
 */
public class Server {
    public static void main(String[] args){
        Server server = Server.getInstance();
        server.start();
    }

    public void start(){
        init();
        int i = 0;
        while(true) {
            try{
                inComing = s.accept();
                ThreadHander hander = new ThreadHander(inComing);
                Thread thread = new Thread(hander);
                thread.start();
                userCount++;
                System.out.println("SB" + userCount);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void init(){
        user = new HashMap<String,ThreadHander>();
        messages = new ArrayBlockingQueue<String>(100);
        userNameSet = new ConcurrentSkipListSet<String>();
        try{
            this.s = new ServerSocket(8189);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setMessage(String s,ThreadHander hander){
        String pushMessage = hander.toString()+","+ s;
        messages.offer(pushMessage);
        pushMessage();
    }

    public void pushMessage(){
        while(!messages.isEmpty()) {
            String message = messages.poll();
            String[] tmp = message.split(",");
            Set<String> targetUser = user.keySet();
            for(String i : targetUser){
                if (!i.equals(tmp[0])){
                    user.get(i).outData(user.get(tmp[0]).getUserName()+","+tmp[1]);
                }
            }
        }
    }

    public void setUser(ThreadHander th, String name){
        user.put(th.toString(),th);
        userNameSet.add(name);
    }

    public void delUser(ThreadHander th,String userName){
        user.remove(th.toString());
        this.userNameSet.remove(userName);
    }

    public boolean find(String name){
        return userNameSet.contains(name);
    }

    private Server(){}

    public static Server getInstance(){
        return server;
    }

    public void decUserCount(){
        userCount--;
    }

    public static ConcurrentSkipListSet<String> userNameSet;
    public static HashMap<String,ThreadHander> user;

    private static Server server = new Server();
    private Socket inComing;
    private ServerSocket s;
    private static ArrayBlockingQueue<String> messages;
    private static int userCount = 0;
}

class ThreadHander implements Runnable{
    public ThreadHander(Socket inComing){
        this.inComing = inComing;
        server = Server.getInstance();
    }
    public void run(){
        try {
            try {
                in = new Scanner(inComing.getInputStream());
                out = new PrintWriter(inComing.getOutputStream(),true);
                inData();
            } finally {
                System.out.println(userName + "is out");
                server.delUser(this, userName);
                outData("close");
                server.decUserCount();
                inComing.close();
                }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void inData(){
        while(true) {
            userName = in.nextLine();
            if(server.find(userName)){
                out.println("exits");
            } else {
                out.println("done");
                break;
            }
        }
        server.setUser(this, userName);
        out.println("OK");
        //System.out.println("OK");
        while(in.hasNextLine()){
            String temp = in.nextLine();
            if (temp.trim().equals("BYE"))   {
                break;
            }
            server.setMessage(temp,this);
        }
    }
    public String getUserName(){
        return userName;
    }
    public void outData(String message){
        out.println(message);
    }
    private String userName;
    private Scanner in;
    private PrintWriter out;
    private Server server;
    private Socket inComing;
}
