import {Link} from "react-router-dom";
import {Product} from "./ShopSchema.ts";

type ProductCardProps = {
    product: Product
}

export default function ProductCard(props: Readonly<ProductCardProps>) {

    return (
        <div>
            <Link to={`/${props.product.id}`}>{props.product.name}</Link>
            <p></p>
            <img src={props.product.images.smallImageURL} alt={props.product.name}/>
            <p>Price: {props.product.price} €</p>

        </div>
    );
}