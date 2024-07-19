package org.example.backend.model;

import lombok.With;
import org.springframework.data.annotation.Id;

@With
public record Product(
        @Id
        String id,
        String name,
        int price

) {
}
