import axios from "axios";
import {useEffect, useState} from "react";
import {Order} from "../components/ShopSchema.ts";

export default function OrderPage() {
    const [order, setOrder] = useState<Order>();

    useEffect(() => {
        axios.get(`/api/order`)
            .then(response => {
                setOrder(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, []);

    if (!order) {
        return(
            <>
                <p>Orders not found</p>
            </>)

    }

    return (
        <>
        <div>
                <h2>{order.productIds}</h2>
                <p>Price: {order.price} €</p>
            </div>
        </>
    )
}