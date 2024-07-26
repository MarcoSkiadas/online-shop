import {Link} from "react-router-dom";

type NavigationProps = {
    currentRole: string | undefined
}
export default function Navigation(props: Readonly<NavigationProps>) {

    return (
        <>
            <div>

                <Link to={"/"}>Homepage</Link>
                <Link to={"/order"}>Orders</Link>
                <Link to={"/shoppingCart"}>Shopping Cart</Link>
                {props.currentRole === `ADMIN` && (<Link to={"/admin"}>Admin</Link>)}


            </div>
        </>
    )
}