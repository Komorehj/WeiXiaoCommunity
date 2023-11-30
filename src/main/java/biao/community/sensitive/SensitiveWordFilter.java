package biao.community.sensitive;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description: 敏感词过滤
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class SensitiveWordFilter {
    @SuppressWarnings("rawtypes")
    private StringBuilder replaceAll;//初始化
    public static Map sensitiveWordMap;
    public static final int minMatchTYpe = 1;      //最小匹配规则,查询到敏感词就返回
    public static final int maxMatchType = 2;      //最大匹配规则

    /**
     * 私有静态变量
     */
    private static SensitiveWordFilter instance;

    static {
        sensitiveWordMap = new SensitiveWordInit().initKeyWord();
    }


    /**
     * 获取实例（双重检测锁）
     *
     * @return SensitivityFilter
     */
    public static SensitiveWordFilter getInstance() {
        if (instance == null) {
            synchronized (SensitiveWordFilter.class) {
                if (instance == null) {
                    instance = new SensitiveWordFilter();
                }
            }
        }
        return instance;
    }


    /**
     * 判断是否是要忽略的字符(忽略所有特殊字符以及空格)
     * @param specificChar 指定字符
     * @return 特殊字符或空格true否则false
     */
    private static boolean isIgnore(char specificChar){
        String regex = "[`~!@#$%^&*()+=|{}':;,\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(specificChar));
        return matcher.matches();
    }


    /**
     * 判断文字是否包含敏感字符
     */
    public boolean isContainSensitiveWord(String txt,int matchType){
        boolean flag = false;
        for(int i = 0 ; i < txt.length() ; i++){
            int matchFlag = this.CheckSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
            if(matchFlag > 0){    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }


    public Set<String> getSensitiveWordMinMatch(String content)
    {
        return getSensitiveWordSets(content,minMatchTYpe);
    }


    public  Set<String> getSensitiveWordMaxMatch(String content)
    {
        return getSensitiveWordSets(content,maxMatchType);
    }



    public Set<String> getSensitiveWords(String txt, int matchType) {
        return getSensitiveWordSets(txt, matchType);
    }


    /**
     * 获取文字中的敏感词
     */
    public Set<String> getSensitiveWordSets(String txt , int matchType){
        Set<String> sensitiveWordList = new HashSet<>();

        for(int i = 0 ; i < txt.length() ; i++){
            int length = CheckSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
            if(length > 0){    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i+length)); //跳过敏感词部分

                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    /**
     * 替换敏感字字符
     */
    public String replaceSensitiveWord(String txt,int matchType,String replaceChar){
        String resultTxt = txt;
        Set<String> set = getSensitiveWordSets(txt, matchType);     //获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word ;
        String replaceString ;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString); //替换获取到的敏感词 ***
        }

        return resultTxt;
    }




    /**
     * 获取替换字符串
     */
    private String getReplaceChars(String replaceChar,int length){
        String resultReplace = replaceChar;
        for(int i = 1 ; i < length ; i++){
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：
     */
    public int CheckSensitiveWord(String txt,int beginIndex,int matchType){
        boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word ;
        Map nowMap = sensitiveWordMap;
        for(int i = beginIndex; i < txt.length() ; i++){
            word = txt.charAt(i);
            //获取指定key
            //解决因空格等特殊字符照成的漏匹配
            if (isIgnore(word)) {
                matchFlag++;
                continue;
            }

            nowMap = (Map) nowMap.get(word);     //获取指定key
            if(nowMap != null){     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if("1".equals(nowMap.get("isEnd"))){
                    //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if(SensitiveWordFilter.minMatchTYpe == matchType){    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }

            }else{     //不存在，直接返回
                break;
            }
        }
        if(matchFlag < 2 || !flag){        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }

}