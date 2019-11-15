package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.reponse.AjaxResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 文章web控制类
 */
@Slf4j //打印日志
@RestController
@RequestMapping("/rest")
@Api(description = "文章操作api")
public class ArticleRestController {

    @ApiOperation(value = "添加文章", notes = "添加新的文章", tags = "Article",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "string"),
            @ApiImplicitParam(name = "content", value = "文章内容", required = true, dataType = "string"),
            @ApiImplicitParam(name = "author", value = "文章作者", required = true, dataType = "string")
    })
    @ApiResponses({
            @ApiResponse(code=200,message="成功",response=AjaxResponse.class),
    })
    @PostMapping("/article")
    public @ResponseBody AjaxResponse saveArticle(@RequestBody Article article) {

        log.info("saveArticle：{}",article);
        return  AjaxResponse.success(article);
    }

    @RequestMapping(value = "/article/{id}", method = DELETE, produces = "application/json")
    public AjaxResponse deleteArticle(@PathVariable Long id) {

        log.info("deleteArticle：{}",id);
        return AjaxResponse.success(id);
    }

    @RequestMapping(value = "/article/{id}", method = PUT, produces = "application/json")
    public AjaxResponse updateArticle(@PathVariable Long id, @RequestBody Article article) {
        article.setId(id);

        log.info("updateArticle：{}",article);
        return AjaxResponse.success(article);
    }

    @RequestMapping(value = "/article/{id}", method = GET, produces = "application/json")
    public AjaxResponse getArticle(@PathVariable Long id) {

        Article article1 = Article.builder().id(1L).author("luzhenning").content("springboot是最好的框架").createTime(new Date()).title("t1").build();
        return AjaxResponse.success(article1);//get 啥id都是返回article1
    }
}
