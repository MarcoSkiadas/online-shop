import {Link} from "react-router-dom";
import {Product} from "./ShopSchema.ts";

type ProductCardProps = {
    product: Product
}

export default function ProductCard(props: Readonly<ProductCardProps>) {

    return (
        <div>
            <Link to={`/${props.product.id}`}>{props.product.name}</Link>
            <p>Id: {props.product.id}</p>
            <p>Price: {props.product.price} €</p>
            <img src={props.product.images.smallImageURL} alt={props.product.name}/>
        </div>
    );
}