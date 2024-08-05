package org.example.backend.model;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Image {
    @Id
    String id;
    String url;
    String name;
}
