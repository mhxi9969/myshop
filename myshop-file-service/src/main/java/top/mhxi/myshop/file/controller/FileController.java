package top.mhxi.myshop.file.controller;



import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.file.utils.S3Utils;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Tag(name = "File管理")
@RestController
@RequestMapping("/file/file")
public class FileController {
    @Autowired
    S3Utils s3Utils;

    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

        File tempFile = File.createTempFile("s3upload-", null);
        // MultipartFile转成File
        file.transferTo(tempFile);

        String url = s3Utils.uploadFile(tempFile, "product/" + filename);
        tempFile.delete();
        return R.ok().data("url", url);
    }
}