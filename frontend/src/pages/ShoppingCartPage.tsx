import {useEffect, useState} from "react";
import axios from "axios";
import {Product, User} from "../components/ShopSchema.ts";

type ShoppingCartPageProps = {
    user: User
    fetchMe: () => void
}
export default function ShoppingCartPage(props: Readonly<ShoppingCartPageProps>) {
    const [product, setProduct] = useState<Product[]>([]);

    useEffect(() => {
        getProducts()
    }, [props.user.shoppingCart])

    function removeProductFromShoppingCart(productId: string) {
        console.log(`Remove product with id: ${productId}`)
        axios.put(`/api/appuser/shoppingCart/removeProduct/${props.user.id}/${productId}`, {})
            .then(() => {
                props.fetchMe()
                getProducts()
            })
            .catch(error => console.log(error.message))
    }

    function getProducts() {
        axios.get(`/api/product/shoppingCart?productIds=${props.user.shoppingCart.productIds}`)
            .then(response => setProduct(response.data))
            .catch(error => console.log(error.message))
    }

    return (
        <>
            <header>
                <h2>Shopping Cart</h2>
            </header>
            <>
                <p>Name: {props.user.username}</p>
                <ul>
                    {product?.map(product => (
                        <li key={product.id}>
                            <h3>{product.name}</h3>
                            <p>Price: ${product.price}</p>
                            <button onClick={() => removeProductFromShoppingCart(product.id)}>Remove Button</button>
                        </li>
                    ))}
                </ul>
            </>
        </>
    )
}