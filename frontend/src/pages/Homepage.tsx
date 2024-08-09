import {Product} from "../components/ShopSchema.ts";
import Search from "../components/Search.tsx";


type HomepageProps = {
    product: Product[]
    login: () => void
    logout: () => void
    user: string | undefined | null
    searchTerm: string
    setSearchTerm: (x: string) => void
}
export default function Homepage(props: Readonly<HomepageProps>) {


    return (
        <>
            {<Search product={props.product} searchTerm={props.searchTerm} setSearchTerm={props.setSearchTerm}/>}
            <button onClick={props.login}>Login</button>
            <button onClick={props.logout}>Logout</button>
            <p>User: {props.user}</p>
        </>
    )
}