import {Product, User} from "./ShopSchema.ts";
import axios from "axios";

type ShowDetailProductProps = {
    product: Product | undefined
    user: User
    fetchMe: () => void
}
export default function ShowDetailProduct(props: Readonly<ShowDetailProductProps>) {

    const putProductToShoppingCart = () => {
        axios.put(`api/appuser/shoppingCart/addProduct/${props.user.id}/${props.product?.id}`, {})
            .then(() => props.fetchMe())
            .catch(error => console.log(error.message))
    }

    return (
        <>
            {!props.product ? <p>Product not found</p> :
                <div>
                    <h2>{props.product?.name}</h2>
                    <p>{props.product?.id}</p>
                    <p>Price: {props.product?.price} €</p>
                    {props.product.quantity.amount < 10 &&
                        <p>Only {props.product.quantity.amount} Products on stock!</p>}
                    <button onClick={putProductToShoppingCart}>add to Shopping Cart</button>
                </div>}
        </>
    )
}