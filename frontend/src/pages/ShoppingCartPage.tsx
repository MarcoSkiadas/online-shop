import {useEffect, useState} from "react";
import axios from "axios";
import {Product, User} from "../components/ShopSchema.ts";

type ShoppingCartPageProps = {
    user: User | null
    fetchMe: () => void
}
export default function ShoppingCartPage(props: Readonly<ShoppingCartPageProps>) {
    const [products, setProducts] = useState<Product[]>([]);
    const [quantities, setQuantities] = useState<Record<string, number>>(() => {
        if (props.user && props.user.shoppingCart && Array.isArray(props.user.shoppingCart.orderedProducts)) {
            const initialQuantities: Record<string, number> = {};
            for (const orderedProduct of props.user.shoppingCart.orderedProducts) {
                initialQuantities[orderedProduct.productId] = orderedProduct.amount;
            }
            return initialQuantities;
        }
        return {};
    });

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
    }, [props.user?.shoppingCart])

    function removeProductFromShoppingCart(productId: string) {
        console.log(`Remove product with id: ${productId}`)
        axios.put(`/api/appuser/shoppingCart/removeProduct/${props.user?.id}/${productId}`, {})
            .then(() => {
                props.fetchMe()
                getProducts()
            })
            .catch(error => console.log(error.message))
    }

    function getProducts() {
        axios.get(`/api/product/shoppingCart?productIds=${props.user?.shoppingCart.orderedProducts.map(orderedProduct => orderedProduct.productId)}`)
            .then(response => setProducts(response.data))
            .catch(error => console.log(error.message))
    }


    const calculateTotalPrice = () => {
        if (props.user && props.user.shoppingCart && Array.isArray(props.user.shoppingCart.orderedProducts)) {
            let total = 0;
            for (const orderedProduct of props.user.shoppingCart.orderedProducts) {
                const product = products.find(p => p.id === orderedProduct.productId);
                if (product) {
                    total += product.price * quantities[product.id];
                }
            }
            return total;
        }
        return 0;
    };

    const totalPrice = calculateTotalPrice();

    const addOrder = async () => {
        axios.post(`/api/order`, {
            orderedProducts: props.user?.shoppingCart.orderedProducts.map(product => ({
                productId: product.productId,
                amount: quantities[product.productId]
            })),
            price: totalPrice,
            userId: props.user?.id
        })
            .then(response => {
                console.log("Order submitted successfully:", response.data);
                axios.put(`api/appuser/shoppingCart/removeProduct/${props.user?.id}`, {})
                    .then(() => {
                        props.fetchMe();
                        getProducts()
                    })
                    .catch(error => console.log(error.message))
                console.log("Updated shoppingCart:", props.user?.shoppingCart);
            })
            .catch(error => console.log(error.message))
    }
    const reduceProductOnStock = async () => {
        props.user?.shoppingCart.orderedProducts.map(orderedProduct =>
            axios.put(`/api/product/shoppingCart/${orderedProduct.productId}/${quantities[orderedProduct.productId]}`)
                .then(response => console.log(response.data))
                .catch(error => console.log(error.message))
        )
    }

    async function handlePurchase() {
        const enoughProductsOnStock =
            products.map(product => {
                if (product.quantity.amount < quantities[product.id]) {
                    alert(`not enough ${product.name} on Stock! only ${product.quantity.amount} on Stock!`)
                    return true;
                } else {
                    return false;
                }
            });
        if (!enoughProductsOnStock) {
            if (props.user?.shoppingCart && props.user.shoppingCart.orderedProducts.length > 0) {
                await addOrder();
                await reduceProductOnStock();
            } else {
                console.log("Order cannot be created without Products")
            }
        }
    }

    return (
        <>
            <header>
                <h2>Shopping Cart</h2>
            </header>
            <>
                <p>Name: {props.user?.username}</p>
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