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
        Images images,
        float rating,
        ArrayList<Review> reviewList

) {
    public Product addRating(float newRating, String commentary) {
        float totalRating = this.rating * this.reviewList.size();
        totalRating += newRating;
        int newRatingCount = this.reviewList.size() + 1;
        float newAverageRating = totalRating / newRatingCount;
        Review review = new Review(newRating, commentary);
        this.reviewList.add(review);
        return new Product(this.id, this.name, this.price, this.quantity, this.images, newAverageRating, this.reviewList);
    }
}
