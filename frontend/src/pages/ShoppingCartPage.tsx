import {useEffect, useState} from "react";
import axios from "axios";
import {Product, User} from "../components/ShopSchema.ts";
import {toast} from "react-toastify";

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
        const removedProduct = products.find(product => product.id === productId)
        console.log(`Remove product with id: ${productId}`)
        axios.put(`/api/appuser/shoppingCart/removeProduct/${props.user?.id}/${productId}`, {})
            .then(() => {
                props.fetchMe()
                getProducts()
                if (removedProduct) {
                    toast.success(`${removedProduct.name} has been removed from your shopping cart`)
                }
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
                    toast.error(`not enough ${product.name} on Stock! only ${product.quantity.amount} on Stock!`)
                    return false;
                } else {
                    return true;
                }
            });
        console.log(enoughProductsOnStock)
        if (!enoughProductsOnStock.some(product => !product)) {
            if (props.user?.shoppingCart && props.user.shoppingCart.orderedProducts.length > 0) {
                await addOrder();
                await reduceProductOnStock();
                toast.success(`Order has been created`)
            } else {
                toast.error(`Order cannot be created without Products`)
            }
        }
    }

    return (
        <div className="shopping-cart-page">
            <header className="shopping-cart-header">
                <h2>Shopping Cart</h2>
            </header>
            <div className="user-info">
                <p>Name: {props.user?.username}</p>
            </div>
            <ul className="product-list">
                {products?.map(product => {
                    const quantity = quantities[product.id] || 0;
                    return (
                        <li key={product.id} className="product-item">
                            <h3>{product.name}</h3>
                            <img src={product.images.smallImageURL} alt={product.name} className="product-image"/>
                            <p>Price: {product.price} €</p>
                            <div className="quantity-controls">
                                <button onClick={() => decreaseQuantity(product.id)} className="quantity-button">-
                                </button>
                                <span className="quantity-display">{quantity}</span>
                                <button onClick={() => increaseQuantity(product.id)} className="quantity-button">+
                                </button>
                            </div>
                            <button onClick={() => removeProductFromShoppingCart(product.id)}
                                    className="remove-button">Remove Button
                            </button>
                        </li>
                    )
                })}
            </ul>
            <button onClick={handlePurchase} className="purchase-button">Create Order</button>
        </div>
    )
}