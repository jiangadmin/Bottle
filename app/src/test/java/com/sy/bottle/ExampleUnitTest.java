package com.sy.bottle;

import com.sy.bottle.utils.NumberToCN;

import org.junit.Test;

import java.math.BigDecimal;

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

    @Test
    public void Num2Text() {
        double money = 123456;
        BigDecimal numberOfMoney = new BigDecimal(money);
        String s = NumberToCN.number2CNMontrayUnit(numberOfMoney);
        System.out.println(s.toString());

    }
}