import React, {useState} from "react";
import "./Rating.css"
import axios from "axios";
import {Product} from "./ShopSchema.ts";

const DEFAULT_COUNT = 5;
const DEFAULT_ICON = `★`;

type RatingProps = {
    product: Product
    getProduct: () => void
}

export default function Rating(props: Readonly<RatingProps>) {
    const [rating, setRating] = useState<number>(props.product.rating)
    const [temporaryRating, setTemporaryRating] = useState(0);
    const [commentary, setCommentary] = useState(``)
    const [showAll, setShowAll] = useState(false);
    const reviews = props.product.reviewList;
    const visibleReviews = showAll ? reviews : reviews.slice(-5).reverse();

    const stars = Array(DEFAULT_COUNT).fill(DEFAULT_ICON);
    const handleReviewClick = (rating: number) => {
        setRating(rating);
        localStorage.setItem("starRating", String(rating))
    }
    const handleReviewSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        const formData = new FormData();
        formData.append('newRating', rating.toString());
        formData.append('commentary', commentary);

        axios.post(`/api/product/${props.product?.id}/rate`,
            formData)
            .then(response => {
                    console.log(response.data)
                    setCommentary(``)
                    setRating(0)
                    alert(`your review has been submitted`)
                }
            )
            .then(props.getProduct)
            .catch(error => console.log(error.message))
    }
    const getStarElements = (ratingCount: number) => {
        const stars = [];
        const maxRating = 5;

        for (let i = 0; i < maxRating; i++) {
            stars.push(
                <span key={i} className={`star ${i < ratingCount ? 'filled' : ''}`}>
                ★
            </span>
            );
        }

        return stars;
    };

    return (

        <>
            <form onSubmit={handleReviewSubmit}>
                <div className={"starsContainer"}>
                    {stars.map((_item, index) => {
                        const isActiveColor = (rating || temporaryRating) &&
                            (index < rating || index < temporaryRating);
                        return (
                            <div
                                className={`star ${isActiveColor ? 'filled' : ''}`}
                                key={index}
                                onMouseEnter={() => setTemporaryRating(index + 1)}
                                onMouseLeave={() => setTemporaryRating(0)}
                                onClick={() => handleReviewClick(index + 1)}
                                onKeyDown={() => handleReviewClick(index + 1)}
                            >
                                {DEFAULT_ICON}
                            </div>

                        );
                    })}
                </div>
                <div>
                    <textarea
                        className={"commentary"}
                        id="commentary"
                        value={commentary}
                        placeholder={"Write your review"}
                        onChange={(e) => setCommentary(e.target.value)}
                        rows={1}
                        maxLength={100}
                    />
                </div>
                <button type={"submit"}>Submit review</button>
            </form>
            <p>Total rating: {props.product.rating}</p>
            <div>
                {visibleReviews.map((review, index) => (
                    <div key={index}>
                        <p>{review.commentary}</p>
                        <div className="stars">
                            {getStarElements(review.ratingCount)}
                        </div>
                    </div>
                ))}
                {reviews.length > 5 && (
                    <button
                        className="toggle-button"
                        onClick={() => setShowAll(!showAll)}
                    >
                        {showAll ? 'Show Less' : 'Show More'}
                    </button>
                )}
            </div>
        </>
    )
}