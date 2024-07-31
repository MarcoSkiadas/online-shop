import axios from "axios";
import {useEffect, useState} from "react";
import {Order, Product, User} from "../components/ShopSchema.ts";

type OrderPageProps = {
    user: User
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
            <div>
                <h2>Order List</h2>

                {orderList
                    .filter((order) => order.userId === props.user.id)
                    .map((order) => (
                        <div key={order.id}>
                            <h3>Order ID: {order.id}</h3>
                            <p>Order Price: {order.price}</p>
                            <ul>
                                {order.orderedProducts.map((orderedProduct) => {
                                    const product = productList.find((product) => product.id === orderedProduct.productId)
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