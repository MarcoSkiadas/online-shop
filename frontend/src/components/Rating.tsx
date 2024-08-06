import React, {useState} from "react";
import "./Rating.css"
import axios from "axios";
import {Product} from "./ShopSchema.ts";

const DEFAULT_COUNT = 5;
const DEFAULT_ICON = `☆`;
const DEFAULT_UNSELECTED_COLOR = "grey";
const DEFAULT_COLOR = "yellow";

type RatingProps = {
    product: Product
}

export default function Rating(props: Readonly<RatingProps>) {
    const [rating, setRating] = useState<number>(props.product.rating)
    const [temporaryRating, setTemporaryRating] = useState(0);
    const [commentary, setCommentary] = useState(``)

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
                }
            )
            .catch(error => console.log(error.message))
    }

    return (

        <>
            <form onSubmit={handleReviewSubmit}>
                <div className={"starsContainer"}>
                    {stars.map((_item, index) => {
                        const isActiveColor = (rating || temporaryRating) &&
                            (index < rating || index < temporaryRating);

                        let elementColor = "";

                        if (isActiveColor) {
                            elementColor = DEFAULT_COLOR;
                        } else {
                            elementColor = DEFAULT_UNSELECTED_COLOR
                        }
                        return (

                            <div
                                className={"star"}
                                key={index}
                                style={{
                                    fontSize: "28px",
                                    color: elementColor,
                                    filter: `${isActiveColor ? "grayscale(0%)" : "grayscale(100%)"}`
                                }}
                                onMouseEnter={() => setTemporaryRating(index + 1)}
                                onMouseLeave={() => setTemporaryRating(0)}
                                onClick={() => handleReviewClick(index + 1)}
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
                        onChange={(e) => setCommentary(e.target.value)}
                        rows={1}
                        maxLength={100}
                    />
                </div>
                <button type={"submit"}>Submit review</button>
            </form>
            <p>Total rating: {props.product.rating}</p>
        </>
    )
}