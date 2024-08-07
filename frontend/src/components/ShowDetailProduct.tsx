import {Product, User} from "./ShopSchema.ts";
import axios from "axios";
import {useState} from "react";
import Rating from "./Rating.tsx";

type ShowDetailProductProps = {
    product: Product | undefined
    user: User
    fetchMe: () => void
}
export default function ShowDetailProduct(props: Readonly<ShowDetailProductProps>) {
    const [quantity, setQuantity] = useState(1);

    const putProductToShoppingCart = () => {
        axios.put(`api/appuser/shoppingCart/addProduct/${props.user.id}/${props.product?.id}/${quantity}`, {})
            .then(() => props.fetchMe())
            .catch(error => console.log(error.message))
    }
    const increaseQuantity = () => {
        setQuantity(quantity + 1);
    };

    const decreaseQuantity = () => {
        if (quantity > 1) {
            setQuantity(quantity - 1);
        }
    };


    return (
        <>
            {!props.product ? <p>Product not found</p> :
                <div>
                    <h2>{props.product?.name}</h2>
                    <p>{props.product?.id}</p>
                    <p>Price: {props.product?.price} €</p>
                    <img src={props.product.imageUrl} alt={props.product?.name}/>


                    {props.product.quantity.amount < 10 &&
                        <p>Only {props.product.quantity.amount} Products on stock!</p>}
                    <div>
                        <button onClick={decreaseQuantity}>-</button>
                        <span>{quantity}</span>
                        <button onClick={increaseQuantity}>+</button>
                    </div>
                    <button onClick={putProductToShoppingCart}>add to Shopping Cart</button>
                    <Rating product={props.product}/>

                </div>}
        </>
    )
}