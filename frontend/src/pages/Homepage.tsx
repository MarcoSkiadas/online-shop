import ProductCard from "../components/ProductCard.tsx";
import {Product} from "../components/ShopSchema.ts";
import {useEffect} from "react";

type HomepageProps = {
    product: Product[]
    login: () => void
    logout: () => void
    me: () => void
    user: string | undefined | null
}
export default function Homepage(props: Readonly<HomepageProps>) {


    const product = props.product.map((product) => <ProductCard key={product.id} product={product}/>);

    useEffect(() => {
        props.me()
    }, []);
    return (
        <>
            {product}
            <button onClick={props.login}>Login</button>
            <button onClick={props.logout}>Logout</button>
            <button onClick={props.me}>Me</button>
            <p>User: {props.user}</p>
        </>
    )
}