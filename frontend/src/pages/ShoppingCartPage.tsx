import {useEffect, useState} from "react";
import axios from "axios";
import {Product, ShoppingCart} from "../components/ShopSchema.ts";

export default function ShoppingCartPage() {
    const [shoppingCart, setShoppingCartList] = useState<ShoppingCart[]>();
    const [product, setProduct] = useState<Product[]>();

    useEffect(() => {
        getShoppingCart()
    }, []);

    function getShoppingCart() {
        axios.get(`/api/shoppingCart`)
            .then(response => {
                setShoppingCartList(response.data);
                console.log(response.data[0].id)
                axios.get(`/api/shoppingCart/${response.data[0].id}/products`)
                    .then(response => setProduct(response.data))
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }

    function removeProductFromShoppingCart(productId: string) {
        console.log(`Remove product with id: ${productId}`)
        axios.put("/api/shoppingCart/removeProduct/233e77ed-1d3a-4ee5-ada0-5704129f34f6", {productIds: [productId]})
            .then(response => console.log(response.data))
            .then(getShoppingCart)
            .catch(error => console.log(error))
    }

    return (
        <>
            <header>
                <h2>Shopping Cart</h2>
            </header>
            {shoppingCart && (
                <>
                    <p>Id: {shoppingCart.map(shoppingCart => (shoppingCart.id))}</p>
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
            )}
        </>
    )
}