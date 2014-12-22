import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Created by smjcm on 2014/12/21.
 */
public class test {
    public static void main(String[] args){
        try {
            ServerSocket s = new ServerSocket(8190);
            Socket incoming = s.accept();
            InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream();
            Scanner in = new Scanner(inStream);
            PrintWriter out = new PrintWriter(outStream,true);
            out.print("hello");
            in.nextLine();
            incoming.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
