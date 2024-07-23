import {Product} from "./ShopSchema.ts";

type ShowDetailProductProps = {
    product:Product | undefined
}
export default function ShowDetailProduct(props:Readonly<ShowDetailProductProps>) {

    return (
        <>
        {!props.product ? <p>Product not found</p> :
        <div>
            <h2>{props.product?.name}</h2>
            <p>Price: {props.product?.price} €</p>
        </div>}
        </>
    )
}