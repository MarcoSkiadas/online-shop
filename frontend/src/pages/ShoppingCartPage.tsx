import {useEffect, useState} from "react";
import axios from "axios";
import {Product, User} from "../components/ShopSchema.ts";

type ShoppingCartPageProps = {
    user: User
    fetchMe: () => void
}
export default function ShoppingCartPage(props: Readonly<ShoppingCartPageProps>) {
    const [products, setProducts] = useState<Product[]>([]);
    const [quantities, setQuantities] = useState(
        props.user.shoppingCart.orderedProduct.reduce((acc, orderedProduct) => {
            acc[orderedProduct.productId] = orderedProduct.amount;
            return acc;
        }, {} as Record<string, number>) // Hinzufügen eines Typs
    );

    const increaseQuantity = (productId: string) => {
        setQuantities(prevQuantities => ({
            ...prevQuantities,
            [productId]: (prevQuantities[productId] || 0) + 1
        }));
    };

    const decreaseQuantity = (productId: string) => {
        setQuantities(prevQuantities => ({
            ...prevQuantities,
            [productId]: Math.max((prevQuantities[productId] || 0) - 1, 0)
        }));
    };

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
        axios.get(`/api/product/shoppingCart?productIds=${props.user.shoppingCart.orderedProduct.map(orderedProduct => orderedProduct.productId)}`)
            .then(response => setProducts(response.data))
            .catch(error => console.log(error.message))
    }

    const totalPrice = props.user.shoppingCart.orderedProduct.reduce((sum, orderedProduct) => {
        const product = products.find(p => p.id === orderedProduct.productId);
        return product ? sum + (product.price * orderedProduct.amount) : sum;
    }, 0);

    async function handlePurchase() {
        if (props.user?.shoppingCart && props.user.shoppingCart.orderedProduct.length > 0) {
            axios.post(`/api/order`, {
                orderedProduct: props.user?.shoppingCart.orderedProduct,
                price: totalPrice,
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
                    {products?.map(product => {
                        const quantity = quantities[product.id] || 0;
                        return (
                            <li key={product.id}>
                                <h3>{product.name}</h3>
                                <p>Price: ${product.price}</p>
                                <div>
                                    <button onClick={() => decreaseQuantity(product.id)}>-</button>
                                    <span>{quantity}</span>
                                    <button onClick={() => increaseQuantity(product.id)}>+</button>
                                </div>
                                <button onClick={() => removeProductFromShoppingCart(product.id)}>Remove Button</button>
                            </li>
                        )
                    })}
                </ul>
                <button onClick={handlePurchase}>Create Order</button>
            </>
        </>
    )
}