import {Order, Product} from "../components/ShopSchema.ts";

type AdminProductPageProps = {
    orderList: Order[] | undefined
    productList: Product[]
}

export default function AdminOrderPage(props: Readonly<AdminProductPageProps>) {

    if (!props.orderList) {
        return (
            <>
                <p>Orders not found</p>
            </>)

    }

    return (
        <>
            <div className="order-page">
                <h2 className="order-header">Order List</h2>

                {props.orderList.map((order) => (
                    <div key={order.id} className="order-item">
                        <h3>Order ID: {order.id}</h3>
                        <p>Order Price: {order.price}€</p>
                        <ul className="ordered-products-list">
                            {order.orderedProducts.map((orderedProduct) => {
                                const product = props.productList.find((product) => product.id === orderedProduct.productId)
                                return <li key={product?.id} className="ordered-product">
                                    <h4>{product?.name}</h4>
                                    <p>Price: {product?.price}€</p>
                                    <p>Amount: {orderedProduct.amount}</p>
                                </li>
                            })}
                        </ul>
                    </div>
                ))}
            </div>
        </>
    )
}