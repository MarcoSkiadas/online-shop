import {Product} from "../components/ShopSchema.ts";
import {useEffect} from "react";
import Search from "../components/Search.tsx";


type HomepageProps = {
    product: Product[]
    login: () => void
    logout: () => void
    me: () => void
    user: string | undefined | null
}
export default function Homepage(props: Readonly<HomepageProps>) {

    useEffect(() => {
        props.me()
    }, []);

    return (
        <>
            {<Search product={props.product}/>}
            <button onClick={props.login}>Login</button>
            <button onClick={props.logout}>Logout</button>
            <p>User: {props.user}</p>
        </>
    )
}