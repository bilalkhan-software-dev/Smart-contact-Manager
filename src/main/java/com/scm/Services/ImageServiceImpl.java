package com.scm.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.Helpers.AppConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService{

    private Cloudinary cloudinary ;

    @Override
    public String uploadImage(MultipartFile file, String publicId) {


        try {
            byte[] data = new byte[file.getInputStream().available()];
            file.getInputStream().read(data) ;
            cloudinary.uploader().upload(data , ObjectUtils.asMap(
                    "public_id",publicId)
            ) ;

            return this.getUrlFromPublicId(publicId);

        } catch (IOException e) {
            log.info("File Not Upload {}", e.getMessage());
            return null;
        }
    }

//    method provide Url

    @Override
    public String getUrlFromPublicId(String publicId) {
        return cloudinary
                .url().transformation(
                       new Transformation<>()
                               .width(AppConstant.CONTACT_IMG_WIDTH)
                               .height(AppConstant.CONTACT_IMG_SIZE)
                               .crop(AppConstant.CONTACT_IMG_CROP))
                .generate(publicId);
    }
}
