package com.server.Puzzle.global.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AwsS3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String putS3(MultipartFile files){
        String filename = null;

        try{
            if (files.getSize() == 0) throw new IllegalArgumentException("null");

            filename = createFilename(files.getOriginalFilename()); // 원래의 파일 명을 UUID로 변환

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(files.getSize());
            objectMetadata.setContentType(files.getContentType());

            try(InputStream inputStream = files.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }

        } catch (NullPointerException e){
            return null;
        }

        return filename;
    }

    private String createFilename(String filename) {
        return UUID.randomUUID().toString().concat(getFileExtension(filename));
    }

    private String getFileExtension(String filename){
        try {
            return filename.substring(filename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식의 파일입니다.");
        }
    }

}
