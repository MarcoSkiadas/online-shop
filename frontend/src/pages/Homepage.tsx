import ProductCard from "../components/ProductCard.tsx";
import {Product} from "../components/ShopSchema.ts";
import {useEffect} from "react";
import {useNavigate} from "react-router-dom";


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
    const nav = useNavigate();

    const routeLogin = () => {
        nav("/login");
    }

    return (
        <>
            <button onClick={routeLogin}>Login</button>
            {props.user != undefined &&
                (<button onClick={props.logout}>Logout</button>)}
            <p>User: {props.user}</p>
            {product}
        </>
    )
}