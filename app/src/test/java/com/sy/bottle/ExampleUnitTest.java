package com.sy.bottle;

import com.sy.bottle.utils.LogUtil;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void te(){
        try {
            System.out.println(URLEncoder.encode("伟大领袖", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}