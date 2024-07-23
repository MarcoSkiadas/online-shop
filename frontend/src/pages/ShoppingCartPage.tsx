import {useEffect, useState} from "react";
import axios from "axios";
import {ShoppingCart} from "../components/ShopSchema.ts";


export default function ShoppingCartPage() {
    const [shoppingCart, setShoppingCartList] = useState<ShoppingCart[]>();

    useEffect(() => {
        getShoppingCart()
    }, []);

    function getShoppingCart() {
        axios.get(`/api/shoppingCart`)
            .then(response => {
                setShoppingCartList(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }

    function removeProductFromShoppingCart(productId:string)  {
        console.log(`Remove product with id: ${productId}`)
        axios.put("/api/shoppingCart/removeProduct/233e77ed-1d3a-4ee5-ada0-5704129f34f6", { productIds: [productId] })
            .then(response => console.log(response.data))
            .then(getShoppingCart)
            .catch(error => console.log(error))
    }

    return (
        <>
            <header>
                <h2>Shopping Cart</h2>
            </header>
            {shoppingCart?.map(shoppingCart => (
                <div key={shoppingCart.id}>
                    <p>Id: {shoppingCart.id}</p>
                    <p>ProductIds:</p>
                    <ul>
                        {shoppingCart.productIds.map(productId => (
                            <li key={productId}>
                                {productId}
                                <button onClick={() => removeProductFromShoppingCart(productId)}>Remove Button</button>
                            </li>
                        ))}
                    </ul>
                </div>
            ))}
        </>
    )
}