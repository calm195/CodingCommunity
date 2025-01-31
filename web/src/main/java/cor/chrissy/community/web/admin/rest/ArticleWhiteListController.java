package cor.chrissy.community.web.admin.rest;

import cor.chrissy.community.common.entity.BaseUserInfoDTO;
import cor.chrissy.community.common.result.Result;
import cor.chrissy.community.core.permission.Permission;
import cor.chrissy.community.core.permission.UserRole;
import cor.chrissy.community.service.user.service.ArticleWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wx128
 * @createAt 2025/1/14
 */
@RestController
@Permission(role = UserRole.ADMIN)
@RequestMapping(path = {"/api/admin/whitelist"})
public class ArticleWhiteListController {
    @Autowired
    private ArticleWhiteListService articleWhiteListService;

    @GetMapping(path = "/get")
    public Result<List<BaseUserInfoDTO>> whiteList() {
        return Result.ok(articleWhiteListService.queryAllArticleWhiteListAuthors());
    }

    @RequestMapping(path = "/add")
    public Result<Boolean> addAuthor(@RequestParam("authorId") Long authorId) {
        articleWhiteListService.addAuthor2ArticleWhitList(authorId);
        return Result.ok(true);
    }

    @RequestMapping(path = "/remove")
    public Result<Boolean> removeAuthor(@RequestParam("authorId") Long authorId) {
        articleWhiteListService.removeAuthorFromArticelWhiteList(authorId);
        return Result.ok(true);
    }
}

