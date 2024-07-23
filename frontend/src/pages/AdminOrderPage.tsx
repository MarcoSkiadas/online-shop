import {Order} from "../components/ShopSchema.ts";

type AdminProductPageProps = {
    orderList:Order[] | undefined
}

export default function AdminOrderPage(props:Readonly<AdminProductPageProps>) {

    return (
        <>
            <header>
                <h2>Order Lists</h2>
            </header>
            {props.orderList?.map(orderList => (
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