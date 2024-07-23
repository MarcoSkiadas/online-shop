import {useEffect, useState} from "react";
import axios from "axios";
import {ShoppingCart} from "../components/ShopSchema.ts";

export default function ShoppingCartPage() {
    const [shoppingCart, setShoppingCartList] = useState<ShoppingCart[]>();

    useEffect(() => {
        axios.get(`/api/shoppingCart`)
            .then(response => {
                setShoppingCartList(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, []);

    return (
        <>
            <header>
                <h2>Shopping Cart</h2>
            </header>
            {shoppingCart?.map(shoppingCart => (
                <div key={shoppingCart.id}>
                    <p>Id: {shoppingCart.id}</p>
                    <p>ProductIds: {shoppingCart.productIds.map(productIds => (
                        <li>{productIds}</li>
                    ))}</p>
                </div>
            ))}
        </>
    )
}