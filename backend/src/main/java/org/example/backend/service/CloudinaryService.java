package org.example.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;


    public ArrayList<String> uploadImage(MultipartFile image) throws IOException {
        File fileToUpload = File.createTempFile("file", null);
        image.transferTo(fileToUpload);
        Map<String, Object> originalResizeOptions = Map.of(
                "width", 1000,
                "height", 1000,
                "crop", "fit"
        );
        Map originalResponse = cloudinary.uploader().upload(fileToUpload, originalResizeOptions);
        String originalUrl = originalResponse.get("url").toString();

        String resizedUrl = cloudinary.url()
                .transformation(new Transformation().width(200).height(100).crop("fit"))
                .generate(originalResponse.get("public_id").toString());

        ArrayList<String> urls = new ArrayList<>();
        urls.add(originalUrl);
        urls.add(resizedUrl);

        return urls;
    }
}
