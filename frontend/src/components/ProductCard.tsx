import {Link} from "react-router-dom";
import {Product} from "./ShopSchema.ts";
import '../App.css'

type ProductCardProps = {
    product: Product
}

export default function ProductCard(props: Readonly<ProductCardProps>) {

    return (
        <div className="product-card">
            <Link to={`/${props.product.id}`}>{props.product.name}</Link>
            <p></p>
            <img src={props.product.images.smallImageURL} alt={props.product.name}/>
            <p>Price: {props.product.price} €</p>

        </div>
    );
}