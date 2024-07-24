import axios from "axios";
import {useEffect, useState} from "react";
import {Order, Product} from "../components/ShopSchema.ts";

export default function OrderPage() {
    const [orderList, setOrderList] = useState<Order[]>();
    const [product, setProduct] = useState<Product[]>();

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
                const ordersWithProducts = await Promise.all(
                    fetchedOrders.map(async (order) => {
                        const products = await fetchProductsByOrderId(order.id);
                        setProduct(products);
                        return {...order, products};
                    })
                );
                setOrderList(ordersWithProducts);
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
                            {order.products.map((product) => (
                                <li key={product.id}>
                                    <h4>{product.name}</h4>
                                    <p>Price: ${product.price}</p>
                                </li>
                            ))}
                        </ul>
                    </div>
                ))}
            </div>
        </>

    )
}