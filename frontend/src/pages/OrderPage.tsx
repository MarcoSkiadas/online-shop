import axios from "axios";
import {useEffect, useState} from "react";
import {Order} from "../components/ShopSchema.ts";

export default function OrderPage() {
    const [orderList, setOrderList] = useState<Order[]>();

    useEffect(() => {
        axios.get(`/api/order`)
            .then(response => {
                setOrderList(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, []);

    if (!orderList) {
        return(
            <>
                <p>Orders not found</p>
            </>)

    }

    return (
        <>
            <header>
            <h2>Order Lists</h2>
            </header>
            {orderList.map(orderList => (
                <div key={orderList.id}>
                    <p>Id: {orderList.id}</p>
                    <p>ProductIds: {orderList.productIds.map(productIds => (
                        <li>{productIds}</li>
                    ))}</p>
                    <p>Price: {orderList.price} €</p>
                </div>
            ))}
        </>
    )
}