package biao.community.sensitive;

import biao.community.sensitive.resources.Filterers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 */
public class SensitiveWordInit {


    public SensitiveWordInit(){
    }
    /*
     * 初始化敏感词库
     */


    @SuppressWarnings("rawtypes")
    private HashMap sensitiveWordMap;  //敏感词库

    @SuppressWarnings("rawtypes")
    public Map initKeyWord(){
        try {
            //读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key ;
        Map nowMap ;
        Map<String, String> newWorMap ;
        //迭代keyWordSet
        for (String s : keyWordSet) {
            key = s;    //关键字
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                // 判断是否已经有一个map树，只有在一个词的首字符有用
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {
                    //不存在则，则新建一个map
                    newWorMap = new HashMap<>();
                    nowMap.put("isEnd", i == key.length() - 1 ? "1" : "0");
                    // 给keyChar该字符设置状态位
                    nowMap.put(keyChar, newWorMap);
                    // 将状态位map赋值给nowMap，表示下一个字符的指针和状态位在同一个map里。
                    nowMap = newWorMap;
                }
            }
        }
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     */


    private Set<String> readSensitiveWordFile() throws Exception{
        Set<String> set;
        File file = Filterers.getFilterResource();
        //修改字符编码为UTF-8
        //字符编码
        String ENCODING = "UTF-8";
        try (
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING)) {
            if (file.isFile() && file.exists()) {      //文件流是否存在
                set = new HashSet<>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt ;
                while ((txt = bufferedReader.readLine()) != null) {    //读取文件，将文件内容放入到set中
                    set.add(txt);
                }
            } else {         //不存在抛出异常信息
                throw new Exception("敏感词库文件不存在");
            }

        }
        //关闭文件流
        return set;
    }
}