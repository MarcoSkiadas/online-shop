import {Product, User} from "./ShopSchema.ts";
import axios from "axios";
import {useState} from "react";
import Rating from "./Rating.tsx";
import '../App.css'

type ShowDetailProductProps = {
    product: Product | undefined
    user: User | null
    fetchMe: () => void
    getProduct: () => void
}
export default function ShowDetailProduct(props: Readonly<ShowDetailProductProps>) {
    const [quantity, setQuantity] = useState(1);

    const putProductToShoppingCart = () => {
        if (props.user === null) {
            alert(`Please login to add ${props.product?.name} to shopping cart`)
        } else {
            const productAlreadyInShoppingCart: boolean =
                props.user?.shoppingCart.orderedProducts.every(orderedProduct => {
                    if (orderedProduct.productId === props.product?.id) {
                        alert(`${props.product.name} is already in your shopping cart`)
                        return false
                    }
                    return true
                });
            console.log(productAlreadyInShoppingCart)
            if (productAlreadyInShoppingCart) {
                props.product?.quantity?.amount && props.product.quantity.amount >= quantity ?
                    axios.put(`api/appuser/shoppingCart/addProduct/${props.user?.id}/${props.product?.id}/${quantity}`, {})
                        .then(() => alert(`${props.product?.name} has been added to shopping cart`))
                        .then(() => props.fetchMe())
                        .catch(error => console.log(error.response.data.errorMsg)) : alert(`only ${props.product?.quantity.amount} ${props.product?.name} on Stock left!`)
            }
        }
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
                <div className="product-detail-container">
                    <h2 className="product-name">{props.product?.name}</h2>
                    <img
                        src={props.product.images.largeImageURL}
                        alt={props.product?.name}
                        className="product-image"/>
                    <p className="product-price">Price: {props.product?.price} €</p>
                    {props.product.quantity.amount < 10 &&
                        <p className="low-stock">Only {props.product.quantity.amount} Products on stock!</p>}
                    <div className="quantity-controls">
                        <button onClick={decreaseQuantity} className="quantity-button">-</button>
                        <span className="quantity-display">{quantity}</span>
                        <button onClick={increaseQuantity} className="quantity-button">+</button>
                    </div>
                    <button onClick={putProductToShoppingCart} className="add-to-cart-button">add to Shopping Cart
                    </button>
                    <Rating product={props.product} getProduct={props.getProduct}/>

                </div>}
        </>
    )
}