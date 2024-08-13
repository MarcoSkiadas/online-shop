import {Product} from "../components/ShopSchema.ts";
import Search from "../components/Search.tsx";
import {useNavigate} from "react-router-dom";


type HomepageProps = {
    product: Product[]
    login: () => void
    logout: () => void
    user: string | undefined | null
    searchTerm: string
    setSearchTerm: (x: string) => void
}
export default function Homepage(props: Readonly<HomepageProps>) {


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
            {<Search product={props.product} searchTerm={props.searchTerm} setSearchTerm={props.setSearchTerm}/>}
        </>
    )
}