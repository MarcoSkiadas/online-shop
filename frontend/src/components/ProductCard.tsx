import {Product} from "../App.tsx";

type ProductCardProps = {
    product: Product
}

export default function ProductCard(props: Readonly<ProductCardProps>) {

    return (
        <div>
            <h3>{props.product.name}</h3>
            <p>Id: {props.product.id}</p>
            <p>Price: {props.product.price}</p>
        </div>
    );
}