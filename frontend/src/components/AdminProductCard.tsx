import {Link} from "react-router-dom";
import {Product} from "./ShopSchema.ts";

type ProductCardProps = {
    product: Product
}

export default function AdminProductCard(props: Readonly<ProductCardProps>) {

    return (
        <div className="product-card">
            <Link to={`/admin/product/update/${props.product.id}`}>{props.product.name}</Link>
            <p>Id: {props.product.id}</p>
            <img src={props.product.images.smallImageURL} alt={props.product.name}/>
            <p>Price: {props.product.price} €</p>
        </div>
    );
}