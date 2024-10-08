import axios from "axios";
import {useEffect, useState} from "react";
import {Order, Product, User} from "../components/ShopSchema.ts";

type OrderPageProps = {
    user: User | null
}
export default function OrderPage(props: Readonly<OrderPageProps>) {
    const [orderList, setOrderList] = useState<Order[]>();
    const [productList, setProductList] = useState<Product[]>([]);

    const fetchOrders = async (): Promise<Order[]> => {
        const response = await axios.get(`api/order`);
        return response.data
    }
    const fetchProductsByOrderId = async (orderId: string): Promise<Product[]> => {
        const response = await axios.get(`/api/order/${orderId}/products`);
        return response.data;
    };

    useEffect(() => {
        const loadOrders = async () => {
            try {
                const fetchedOrders = await fetchOrders();
                fetchedOrders.forEach((order) => {
                    fetchProductsByOrderId(order.id).then((products) => setProductList([...productList, ...products]))
                });
                setOrderList(fetchedOrders);
            } catch (err) {
                console.log('Failed to load orders');
            }
        };
        loadOrders();
    }, []);

    if (!orderList) {
        return (
            <>
                <p>Orders not found</p>
            </>)

    }

    return (
        <>
            <div className="order-page">
                <h2 className="order-header">Order List</h2>

                {orderList
                    .filter((order) => order.userId === props.user?.id)
                    .map((order) => (
                        <div key={order.id} className="order-item">
                            <h3>Order ID: {order.id}</h3>
                            <p>Order Price: {order.price}€</p>
                            <ul className="ordered-products-list">
                                {order.orderedProducts.map((orderedProduct) => {
                                    const product = productList.find((product) => product.id === orderedProduct.productId)
                                    if (product) {
                                        return <li key={product?.id} className="ordered-product">
                                            <h4>{product?.name}</h4>
                                            <p>Price: {product?.price}€</p>
                                            <p>Amount: {orderedProduct.amount}</p>
                                            <p>Total: {product?.price * orderedProduct.amount} € </p>
                                        </li>
                                    }
                                    return <p>Product not Found</p>
                                })}
                            </ul>
                        </div>
                    ))}
            </div>
        </>

    )
}