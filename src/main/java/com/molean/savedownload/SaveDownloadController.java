package com.molean.savedownload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class SaveDownloadController {

    private final SaveDownloadDao saveDownloadDao;

    @Autowired
    public SaveDownloadController(SaveDownloadDao saveDownloadDao) {
        this.saveDownloadDao = saveDownloadDao;
    }

    @RequestMapping("/{username}")
    public String download(HttpServletRequest request, HttpServletResponse response, @PathVariable String username) {

        String requestToken = request.getParameter("token");

        SaveBean save = saveDownloadDao.getSave(username);
        if (save == null) {
            return "请求的存档不存在。";
        }

        String token = save.getToken();

        if (!token.equals(requestToken)) {
            return "Token不正确或已过期，请在游戏中重新生成下载地址。";
        }

        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + username + ".zip");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(save.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "OK";
    }
}
