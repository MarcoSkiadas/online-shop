import {Navigate, Outlet} from "react-router-dom";
import {User} from "./ShopSchema.ts";

type ProtectedAdminRouteProps = {
    user: User | null | undefined
}

export default function ProtectedAdminRoute(props: ProtectedAdminRouteProps) {

    if (props.user === undefined) {
        return <p>Loading ...</p>
    }

    return (
        props.user?.role === "ADMIN" ? <Outlet/> : <Navigate to="/"/>
    )
}