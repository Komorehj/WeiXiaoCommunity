package biao.community.sensitive.resources;

import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;


public class Filterers {


    /**
     * 加载类路径下资源
     *
     * @return File
     * @throws FileNotFoundException FileNotFoundException
     */
    @PostConstruct
    public static File getFilterResource() throws FileNotFoundException {
        File file;
        try {
            file = ResourceUtils.getFile("classpath:SensitiveWord.txt");

        } catch (FileNotFoundException e) {
            System.out.println("资源不存在");
            throw e;
        }
        return file;
    }
}

