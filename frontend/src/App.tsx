import './App.css'
import Homepage from "./pages/Homepage.tsx";
import Navigation from "./components/Navigation.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";
import {Order, Product, User} from "./components/ShopSchema.ts";
import ProductPage from "./pages/ProductPage.tsx";
import OrderPage from "./pages/OrderPage.tsx";
import AdminUpdateProductPage from "./pages/AdminUpdateProductPage.tsx";
import AdminPage from "./pages/AdminPage.tsx";
import AdminOrderPage from "./pages/AdminOrderPage.tsx";
import AdminProductPage from "./pages/AdminProductPage.tsx";
import AdminDetailProductPage from "./pages/AdminDetailProductPage.tsx";
import AdminAddProductPage from "./pages/AdminAddProductPage.tsx";
import ShoppingCartPage from "./pages/ShoppingCartPage.tsx";
import ProtectedRoute from "./components/ProtectedRoute.tsx";
import ProtectedAdminRoute from "./components/ProtectedAdminRoute.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


function App() {

    const [product, setProduct] = useState<Product[]>([]);
    const [showSuccess, setShowSuccess] = useState(false);
    const navigate = useNavigate();
    const [orderList, setOrderList] = useState<Order[]>();
    const [user, setUser] = useState<User | null | undefined>(undefined)
    const currentRole = user?.role
    const unitType = ["PIECE", "KILOGRAM", "LITER", "GRAM", "METER"]
    const [searchTerm, setSearchTerm] = useState<string>("");

    useEffect(() => {
        getAllProducts()
        me()
    }, [])

    function getAllProducts() {
        axios.get("/api/product")
            .then(response => setProduct(response.data))
            .catch(error => console.log(error.message))
    }

    function getAllOrders() {
        axios.get(`/api/order`)
            .then(response => {
                setOrderList(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error.message);
            });
    }

    const handleCloseSuccess = () => {
        setShowSuccess(false);
        navigate(`/`)
        getAllProducts()
    }
    const handleOrderButton = () => {
        navigate(`/admin/order`)
        getAllOrders()
    }
    const handleProductButton = () => {
        navigate(`/admin/product`)
        getAllProducts()
    }
    const handleClickProduct = () => {
        navigate(`/admin/product`);
        getAllProducts()
    }
    const login = () => {
        const host = window.location.host === `localhost:5173` ? `http://localhost:8080` : window.location.origin
        window.open(host + `/oauth2/authorization/github`, `_self`)
    }

    function logout() {
        axios.get("/api/auth/logout")
            .then(() => setUser(null))
            .then(() => alert("you have been logged out"))
    }

    const me = () => {
        axios.get(`/api/auth/me`)
            .then(response => setUser(response.data))
            .catch(() => {
                setUser(null)
            })
    }

    if (user === undefined) {
        return <><p>Loading...</p></>
    }


    return (
        <>
            <header>
                <Navigation currentRole={currentRole}/>
            </header>
            <Routes>
                <Route element={<RegisterPage/>} path={"/register"}/>
                <Route element={<LoginPage setUser={setUser} me={me} login={login}/>} path={"/login"}/>
                <Route path={"/"}
                       element={<Homepage product={product} login={login} logout={logout}
                                          user={user?.username}
                                          searchTerm={searchTerm} setSearchTerm={setSearchTerm}/>}/>
                <Route path={"/:id"} element={<ProductPage user={user} fetchMe={me}/>}/>
                <Route element={<ProtectedRoute user={user?.username}/>}>
                    <Route path={"/order"} element={<OrderPage user={user}/>}/>
                    <Route path={"/shoppingCart"}
                           element={<ShoppingCartPage user={user} fetchMe={me}/>}/>
                    <Route element={<ProtectedAdminRoute user={user}/>}>
                        <Route path={"/admin"} element={<AdminPage handleOrderButton={handleOrderButton}
                                                                   handleProductButton={handleProductButton}/>}/>
                        <Route path={"/admin/product"} element={<AdminProductPage product={product}
                                                                                  searchTerm={searchTerm}
                                                                                  setSearchTerm={setSearchTerm}/>}/>
                        <Route path={"/admin/order"}
                               element={<AdminOrderPage orderList={orderList} productList={product}/>}/>
                        <Route path={"/admin/product/add"}
                               element={<AdminAddProductPage handleClickProduct={handleClickProduct}
                                                             unitType={unitType}/>}/>
                        <Route path={"/admin/product/:id"}
                               element={<AdminDetailProductPage user={user} fetchMe={me}/>}/>
                        <Route path={"/admin/product/update/:id"}
                               element={<AdminUpdateProductPage handleCloseSuccess={handleCloseSuccess}
                                                                showSuccess={showSuccess}
                                                                setShowSuccess={setShowSuccess}
                                                                handleClickProduct={handleClickProduct}
                                                                unitType={unitType}/>}/>
                    </Route>
                </Route>
            </Routes>
            <ToastContainer/>
        </>
    )
}

export default App
