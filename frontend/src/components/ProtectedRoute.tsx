import {Navigate, Outlet} from "react-router-dom";

type ProtectedRouteProps = {
    user: string | undefined | null
}

export default function ProtectedRoute(props: ProtectedRouteProps) {

    if (props.user === undefined) {
        return <p>Loading ...</p>
    }

    return (
        props.user ? <Outlet/> : <Navigate to="/Login"/>
    )
}