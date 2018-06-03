package org.chao.action.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.chao.action.Checker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xiezhengchao
 * @since 2018/6/3 14:31
 */
@Controller
public class ConversionController {

    private Checker checker = new Checker();

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @PostMapping("conversion/upload")
    public String conversion(String originCoding, String newCoding, @RequestParam("file") MultipartFile multipartFile,
            HttpServletResponse response) throws IOException {

        String newFileName = createNewName(newCoding, multipartFile.getOriginalFilename());

        if (!checker.check(newFileName)) {
            return "tip";
        }

        InputStream is = null;
        try {
            is = multipartFile.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.setHeader("content-disposition", "attachment;filename=\"" + newFileName + "\"");

            byte[] buff = new byte[4096];
            while (is.read(buff) != -1) {
                out.write(buff);
            }

            response.getOutputStream().write(out.toString(originCoding).getBytes(newCoding));

            response.getOutputStream().flush();
            response.getOutputStream().close();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    private String createNewName(String newCoding, String originName) throws UnsupportedEncodingException {
        int index = originName.lastIndexOf(".");
        String suffix = originName.substring(index);
        String prefix = originName.substring(0, index);
        String newFileName = prefix + "-" + newCoding + suffix;
        return URLEncoder.encode(newFileName, "UTF8");
    }

}
