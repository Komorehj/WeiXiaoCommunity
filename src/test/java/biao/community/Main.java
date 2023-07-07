package biao.community;
/*****************************************
 *  Function：实现MD5消息摘要算法
 *  Author：  Qiu Yihao
 *  Date：    2018-12-04
 *  Contact:  576261090@qq.com
 *****************************************/

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

class Main{
    public static void main(String[] args)throws IOException,  NoSuchAlgorithmException{


        for(int i=0;i<110;i++) {
            System.out.println(randInt(90,100));
        }
    }

    public static int randInt(int min,int max){
//        PageHelper.startPage(1,4);
        Random rd = new Random();

        int temp = rd.nextInt();

        return  (temp>0?temp:temp*-1) %(max+1-min) + min;

    }
}