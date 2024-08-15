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
                    <img src="https://img.icons8.com/?size=100&id=74811&format=png&color=000000" alt="Homepage"/>
                    <span>Homepage</span>
                </Link>
                <Link to="/order" className="nav-link">
                    <img src="https://img.icons8.com/?size=100&id=13229&format=png&color=000000" alt="Orders"/>
                    <span>Orders</span>
                </Link>
                <Link to="/shoppingCart" className="nav-link">
                    <img src="https://img.icons8.com/?size=100&id=8chNl15hy6jY&format=png&color=000000"
                         alt="Shopping Cart"/>
                    <span>Shopping Cart</span>
                </Link>
                {props.currentRole === `ADMIN` && (
                    <Link to="/admin" className="nav-link">
                        <img src="https://img.icons8.com/?size=100&id=114317&format=png&color=000000" alt="Admin"/>
                        <span>Admin</span>
                    </Link>
                )}
            </div>
        </>
    )
}