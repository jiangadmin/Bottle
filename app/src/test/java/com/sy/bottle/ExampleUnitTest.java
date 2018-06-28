package com.sy.bottle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    public void te() {
        String dsda = "江苏省 连云港港 海州区 靠近什么是多少大撒大声地";
        System.out.println(dsda.substring(dsda.lastIndexOf(" ")));

    }
}