import {useEffect, useState} from "react";
import axios from "axios";
import {Product, User} from "../components/ShopSchema.ts";

type ShoppingCartPageProps = {
    user: User
    fetchMe: () => void
    handlePurchase: () => void
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

    async function handlePurchase() {
        if (props.user?.shoppingCart && props.user.shoppingCart.productIds.length > 0) {
            axios.post(`/api/order`, {
                productIds: props.user?.shoppingCart.productIds,
                price: parseFloat("22"),
                userId: props.user?.id
            })
                .then(response => {
                    console.log("Order submitted successfully:", response.data);
                    axios.put(`api/appuser/shoppingCart/removeProduct/${props.user.id}`, {})
                        .then(() => {
                            props.fetchMe();
                            getProducts()
                        })
                        .catch(error => console.log(error.message))
                    console.log("Updated shoppingCart:", props.user.shoppingCart);
                })
                .catch(error => console.log(error.message))
        } else {
            console.log("Order cannot be created without Products")
        }
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
                <button onClick={handlePurchase}>Create Order</button>
            </>
        </>
    )
}