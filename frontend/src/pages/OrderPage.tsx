import axios from "axios";
import {useEffect, useState} from "react";
import {Order, Product} from "../components/ShopSchema.ts";

export default function OrderPage() {
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
            <div>
                <h2>Order List</h2>
                {orderList.map((order) => (
                    <div key={order.id}>
                        <h3>Order ID: {order.id}</h3>
                        <ul>
                            {order.productIds.map((productId) => {
                                const product = productList.find((product) => product.id === productId)
                                return <li key={product?.id}>
                                    <h4>{product?.name}</h4>
                                    <p>Price: ${product?.price}</p>
                                </li>
                            })}
                        </ul>
                    </div>
                ))}
            </div>
        </>

    )
}