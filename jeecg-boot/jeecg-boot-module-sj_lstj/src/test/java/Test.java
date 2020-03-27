/**
 * @author wg
 * @Package PACKAGE_NAME
 * @date 2020/3/26 16:12
 * @Copyright
 */
public class Test {

    @org.junit.Test
    public void test(){

        Integer i= 1;

        Integer s= i<<2;

        System.out.println(s);
    }

    @org.junit.Test
    public void test2(){

        String s= "asdf_sdfsdf";
        String[] s1 = s.split("_");
        System.out.println(s1[0]);
        System.out.println(s1[1]);


    }
}
