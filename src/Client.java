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
    public void handle() {
        try {
            try {
                cli = new Socket("127.0.0.1", 8189);
                Scanner out = new Scanner(System.in);
                ThreadListen listen = new ThreadListen(cli.getInputStream());
                PrintWriter push = new PrintWriter(cli.getOutputStream());
                Thread t = new Thread(listen);
                t.start();
                boolean done = false;
                while (!done && out.hasNextLine()) {
                    String s = out.nextLine();
                    if (s.equals("Q")) break;
                    push.println(s);
                    push.flush();
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
}

class ThreadListen implements Runnable{
    public ThreadListen(InputStream inStream){
        this.inStream = inStream;
    }
    public void run(){
        Scanner in = new Scanner(inStream);
        while(true){
            while(in.hasNextLine()){
                System.out.println(in.nextLine());
            }
        }
    }
    private InputStream inStream;
}