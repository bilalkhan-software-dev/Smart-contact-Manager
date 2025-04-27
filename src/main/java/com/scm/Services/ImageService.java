package com.scm.Services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage (MultipartFile file, String publicId) ;


    String getUrlFromPublicId(String publicId) ;
}
