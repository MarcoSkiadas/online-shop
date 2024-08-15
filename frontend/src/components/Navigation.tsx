import {Link} from "react-router-dom";
import '../App.css'

type NavigationProps = {
    currentRole: string | undefined
}
export default function Navigation(props: Readonly<NavigationProps>) {

    return (
        <>
            <div className="navigation">
                <Link to="/" className="nav-link">
                    <img src="/path/to/home-icon.png" alt="Homepage"/>
                    <span>Homepage</span>
                </Link>
                <Link to="/order" className="nav-link">
                    <img src="/path/to/order-icon.png" alt="Orders"/>
                    <span>Orders</span>
                </Link>
                <Link to="/shoppingCart" className="nav-link">
                    <img src="/path/to/cart-icon.png" alt="Shopping Cart"/>
                    <span>Shopping Cart</span>
                </Link>
                {props.currentRole === `ADMIN` && (
                    <Link to="/admin" className="nav-link">
                        <img src="/path/to/admin-icon.png" alt="Admin"/>
                        <span>Admin</span>
                    </Link>
                )}
            </div>
        </>
    )
}