package org.example.backend.model;

import lombok.With;
import org.springframework.data.annotation.Id;

@With
public record Product(
        @Id
        String id,
        String name,
        float price,
        Quantity quantity,
        String imageUrl,
        float rating,
        int ratingCount

) {
    public Product addRating(float newRating) {
        float totalRating = this.rating * this.ratingCount;
        totalRating += newRating;
        int newRatingCount = this.ratingCount + 1;
        float newAverageRating = totalRating / newRatingCount;
        return new Product(this.id, this.name, this.price, this.quantity, this.imageUrl, newAverageRating, newRatingCount);
    }
}
