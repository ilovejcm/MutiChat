import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Created by smjcm on 2014/12/23.
 */
public class Client {
    public static void main(String[] args){
        Client c = new Client();
        c.handle();
    }
    public void setName(){
        Scanner out = new Scanner(System.in);
        do {
            System.out.print("Please entry your name: ");
            name = out.nextLine();
            if(!name.isEmpty()) break;
        }while(true);
    }
    public void handle() {
        System.out.println("Welcome to Sm's chat system!");
        Scanner out = new Scanner(System.in);
        setName();
        try {
            try {
                cli = new Socket("127.0.0.1", 8189);
                InputStream inStream = cli.getInputStream();
                Scanner in = new Scanner(inStream);
                ThreadListen listener = new ThreadListen(in,cli);
                PrintWriter push = new PrintWriter(cli.getOutputStream(),true);

                push.println(name);
                String temp = in.nextLine();
                while(!temp.equals("done")){
                    if (temp.equals("exits")){
                        new NameExitsErr().printErr();
                        setName();
                        push.println(name);
                        temp = in.nextLine();
                    }
                }
                temp = in.nextLine();
                while(!temp.equals("OK")){
                }
                System.out.println("Hello " + name + " Let's ROCK!");
                System.out.println("######################################");

                Thread t = new Thread(listener);
                t.start();
                HeartBeat hb = new HeartBeat(cli);
                Thread t1 = new Thread(hb);
                t1.start();
                while ( !cli.isClosed() && out.hasNextLine()) {
                    String s = out.nextLine();
                    push.println(s);
                }
            }
            finally {
                cli.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    private Socket cli;
    private String name;
}

class ThreadListen implements Runnable{
    public ThreadListen(Scanner in,Socket cli){
        this.in = in;
        this.cli = cli;
    }
    public void run(){
        while(in.hasNextLine()){
            String message = in.nextLine();
            if (message.equals("close"))    {
                System.out.println("Press Any Key To Stop This Program!!");
                break;
            }
            String[] display = message.split(",");
            System.out.println(display[0] + " says: " + display[1]);
        }
    }
    private Scanner in;
    private Socket cli;
}

class HeartBeat implements Runnable{
    public HeartBeat(Socket cli){
        this.cli = cli;
    }
    public void run(){
        try {
            while (true) {
                cli.sendUrgentData(0xFF);
                Thread.sleep(3*1000);
            }
        }catch(Exception e){
            try{
                cli.close();
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }
    }
    private Socket cli;
}

interface ServerErr{
    public void printErr();
}

class NameExitsErr implements ServerErr{
    public void printErr(){
        System.out.println(errName);
    }
    public static final String errName = "NameExitsErr";
}