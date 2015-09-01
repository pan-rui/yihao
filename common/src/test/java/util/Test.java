package util;

import java.lang.reflect.Array;

/**
 * Created by Administrator on 2015/9/1.
 */
public class Test {
    public <T> T[] mergeArray(T[] src, T[] src2) {
        T[] newB = (T[]) Array.newInstance(src[0].getClass(),src.length + src2.length);
        System.arraycopy(src,0,newB,0,src.length);
        System.arraycopy(src2,0,newB,src.length,src2.length);
        return newB;
    }
@org.junit.Test
    public void testMergeArray() {
        String[] str1 = {"a", "b", "c"};
        String[] str2 = {"f", "g"};
        System.out.println(mergeArray(str1, str2));
        for(String str:mergeArray(str1,str2))
            System.out.println(str);
    }
}
