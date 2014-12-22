import java.util.ArrayList;
import java.util.List;
import java.util.*;
/**
 * Created by smjcm on 2014/12/21.
 */
public class testLala {
    public static void main(String[] args) {
        new testLala().test();
        System.out.println("done");
    }
    void test(){
        in = new ArrayList<A>();
        int i = 0;
        while (i < 10) {
            A a = new A();
            in.add(a);
            i++;
        }
        System.out.println(in.size());
    }
    List<A> in;
}

class A {
    public A(){
        b = new testLala();
    }
    testLala b;
}