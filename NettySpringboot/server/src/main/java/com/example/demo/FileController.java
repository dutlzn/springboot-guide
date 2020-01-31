package com.example.demo;


import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file){
        try{
            //判断文件是否存在
            if (file == null){
                throw new RuntimeException("文件不存在");
            }
            //获取文件的完整名称
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)){
                throw new RuntimeException("文件不存在");
            }
            //获取文件的扩展名称  abc.jpg   jpg
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //获取文件内容
            byte[] content = file.getBytes();
            //创建文件上传的封装实体类
            FastDfsFile fastDfsFile = new FastDfsFile(originalFilename,content,extName);
            //基于工具类进行文件上传,并接受返回参数  String[]
            String[] uploadResult = FastDfsClient.upload(fastDfsFile);
            //封装返回结果
            return FastDfsClient.getTrackerUrl()+uploadResult[0]+"/"+uploadResult[1];
        }catch (Exception e){
            e.printStackTrace();
        }
        return "文件上传失败";
    }
}
