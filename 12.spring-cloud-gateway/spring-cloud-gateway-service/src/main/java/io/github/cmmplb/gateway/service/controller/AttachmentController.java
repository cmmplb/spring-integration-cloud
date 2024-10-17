package io.github.cmmplb.gateway.service.controller;

import io.github.cmmplb.core.result.Result;
import io.github.cmmplb.core.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author penglibo
 * @date 2023-06-14 16:29:44
 * @since jdk 1.8
 */

@Slf4j
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @PostMapping("/upload")
    public Result<String> fileUpload(@RequestPart("files") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            log.info("files:{}", file);
        }
        return ResultUtil.success();
    }
}
