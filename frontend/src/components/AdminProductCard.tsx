import {Link} from "react-router-dom";
import {Product} from "./ShopSchema.ts";

type ProductCardProps = {
    product: Product
}

export default function AdminProductCard(props: Readonly<ProductCardProps>) {

    return (
        <div>
            <Link to={`/admin/product/update/${props.product.id}`}>{props.product.name}</Link>
            <p>Id: {props.product.id}</p>
            <p>Price: {props.product.price} €</p>
            <img src={props.product.imageUrl} alt={props.product.name}/>
        </div>
    );
}