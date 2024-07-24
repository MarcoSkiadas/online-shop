import {Product} from "./ShopSchema.ts";
import axios from "axios";

type ShowDetailProductProps = {
    product:Product | undefined
}
export default function ShowDetailProduct(props:Readonly<ShowDetailProductProps>) {

    const putProductToShoppingCart = () => {
        console.log(props.product?.id)
        axios.put("/api/shoppingCart/addProduct/233e77ed-1d3a-4ee5-ada0-5704129f34f6", { productIds: [props.product?.id] })
            .then(response => console.log(response.data))
            .catch(error => console.log(error))
    }

    return (
        <>
        {!props.product ? <p>Product not found</p> :
        <div>
            <h2>{props.product?.name}</h2>
            <p>{props.product?.id}</p>
            <p>Price: {props.product?.price} €</p>
            <button onClick={putProductToShoppingCart}>add to Shopping Cart</button>
        </div>}
        </>
    )
}