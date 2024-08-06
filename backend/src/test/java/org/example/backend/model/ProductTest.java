package org.example.backend.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product product;

    @Test
    public void testAddRating() {
        Product initialProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test", 0, new ArrayList<>());

        ArrayList<Review> expectedReviews = new ArrayList<>();
        expectedReviews.add(new Review(3, "Good product"));
        Product expectedProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test", 3, expectedReviews);

        Product actualProduct = initialProduct.addRating(3, "Good product");

        assertEquals(expectedProduct, actualProduct);
    }

    @Test
    public void testAddMultipleRatings() {
        ArrayList<Review> initialReviews = new ArrayList<>();
        initialReviews.add(new Review(3, "Good product"));
        Product initialProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test", 3, initialReviews);

        ArrayList<Review> expectedReviews = new ArrayList<>(initialReviews);
        expectedReviews.add(new Review(5, "Excellent product"));
        float expectedRating = (3 + 5) / 2.0f; // Average rating
        Product expectedProduct = new Product("1", "Rasenmäher", 22, new Quantity(2, Unit.PIECE), "Test", expectedRating, expectedReviews);

        Product actualProduct = initialProduct.addRating(5, "Excellent product");

        assertEquals(expectedProduct, actualProduct);
    }
}