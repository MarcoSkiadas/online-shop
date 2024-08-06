package org.example.backend.model;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@With
public record Product(
        @Id
        String id,
        String name,
        float price,
        Quantity quantity,
        String imageUrl,
        float rating,
        ArrayList<Review> reviewList

) {
    public Product addRating(float newRating) {
        float totalRating = this.rating * this.reviewList.size();
        totalRating += newRating;
        int newRatingCount = this.reviewList.size() + 1;
        float newAverageRating = totalRating / newRatingCount;
        return new Product(this.id, this.name, this.price, this.quantity, this.imageUrl, newAverageRating, this.reviewList);
    }
}
