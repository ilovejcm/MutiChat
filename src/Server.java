import java.net.*;
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by smjcm on 2014/12/19.
 */
public class Server {

    public Server(){
    }

    public void start(){
        init();
        int i = 0;
        while(true) {
            try{
                inComing = s.accept();
                ThreadHander hander = new ThreadHander(inComing);
                Thread thread = new Thread(hander);
                //System.out.println(hander);
                setUser(hander);
                System.out.println("user is:" + user.size());
               // System.out.println("lei is:" + Server.user.size());
                thread.start();
                i++;
                System.out.println("SB" + i);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void init(){
        user = new ArrayList<ThreadHander>();
        messages = new ArrayBlockingQueue<String>(100);
        try{
            this.s = new ServerSocket(8189);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setMessage(String s){
        //System.out.println("setMessage");
        //System.out.println(s);
        //System.out.println(messages.size());
        messages.offer(s);
        //System.out.println(messages.isEmpty());
        //System.out.println(messages.size());
        //System.out.println("!!!!!!!!!");
        pushMessage();
    }

    public void pushMessage(){
        while(!messages.isEmpty()) {
            String message = messages.poll();
           /* System.out.println(message);
            System.out.println("user is:" + user.size());*/
            for (ThreadHander i : user) {
                //System.out.println("push");
                i.getAndPushData(message);
            }
        }
    }

    public void setUser(ThreadHander th){
        user.add(th);
    }

    public void delUser(ThreadHander th){
        user.remove(th);
    }

    private Socket inComing;
    private ServerSocket s;
    public static List<ThreadHander> user;
    private static ArrayBlockingQueue<String> messages;
}

class ThreadHander implements Runnable{
    public ThreadHander(){
    }
    public ThreadHander(Socket inComing){
        this.inComing = inComing;
        server = new Server();
    }
    public void run(){
        try {
            try {
                inStream = inComing.getInputStream();
                outStream = inComing.getOutputStream();
                in = new Scanner(inStream);
                out = new PrintWriter(outStream,true);
                out.println("Hello Newer!!");
                admitData();
            } finally {
                inComing.close();
                //new Server().delUser(this);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void admitData(){
        boolean done = false;
        while(!false && in.hasNextLine()){
            //out.printf("%s","i am says:");
            String temp = in.nextLine();
            //out.println(temp);
            if (temp.trim().equals("BYE"))   {
                done = true;
                break;
            }
            //System.out.println("temp is:" + temp);
            server.setMessage(temp);
        }
    }
    public void getAndPushData(String message){
        out.println(message);
    }
    private Scanner in;
    private PrintWriter out;
    private Server server;
    private Socket inComing;
    private InputStream inStream;
    private OutputStream outStream;
}
