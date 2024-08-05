package org.example.backend;

import com.cloudinary.Cloudinary;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        Cloudinary cloudinary = new Cloudinary(System.getenv("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        System.out.println(cloudinary.config.cloudName);
    }

}
