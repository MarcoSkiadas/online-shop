import {Link} from "react-router-dom";

export default function Navigation() {

    return(
        <>
            <div>
                <Link to={"/"}>Homepage</Link>
                <Link to={"/order"}>Orders</Link>
                <Link to={"/shoppingCart"}>Shopping Cart</Link>
                <Link to={"/admin"}>Admin</Link>

            </div>
        </>
    )
}