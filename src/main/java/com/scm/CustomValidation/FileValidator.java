package com.scm.CustomValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class FileValidator implements ConstraintValidator<ImageValidation, MultipartFile> {


    private static final long MAX_FILE_SIZE = 1024 * 1024 * 2; // 5 MB


    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {


        if (file == null || file.isEmpty()) {
//            constraintValidatorContext.disableDefaultConstraintViolation();
//            constraintValidatorContext.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();
            return true;
        }

        // size
        if (file.getSize() > MAX_FILE_SIZE) {

            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("File size should be less than 2 MB ").addConstraintViolation();

            return false;
        }

        // resolution -->>
//        try {
//            BufferedImage read = ImageIO.read(file.getInputStream());
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        return true ;
    }
}
