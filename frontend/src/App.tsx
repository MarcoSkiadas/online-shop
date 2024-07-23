import './App.css'
import Homepage from "./pages/Homepage.tsx";
import Navigation from "./components/Navigation.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";
import {Order, Product} from "./components/ShopSchema.ts";
import ProductPage from "./pages/ProductPage.tsx";
import OrderPage from "./pages/OrderPage.tsx";
import AdminUpdateProductPage from "./pages/AdminUpdateProductPage.tsx";
import AdminPage from "./pages/AdminPage.tsx";
import AdminOrderPage from "./pages/AdminOrderPage.tsx";
import AdminProductPage from "./pages/AdminProductPage.tsx";
import AdminDetailProductPage from "./pages/AdminDetailProductPage.tsx";
import AdminAddProductPage from "./pages/AdminAddProductPage.tsx";
import ShoppingCartPage from "./pages/ShoppingCartPage.tsx";



function App() {

    const [product, setProduct] = useState<Product[]>([]);
    const [showSuccess, setShowSuccess] = useState(false);
    const navigate = useNavigate();
    const [orderList, setOrderList] = useState<Order[]>();

    useEffect( () => {
        getAllProducts()
    },[])

    function getAllProducts() {
        axios.get("/api/product")
            .then(response => setProduct(response.data))
            .catch(error => console.log(error))
    }
    function getAllOrders() {
            axios.get(`/api/order`)
                .then(response => {
                    setOrderList(response.data);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
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

  return (
    <>
        <header>
            <Navigation/>
        </header>
        <Routes>
            <Route path={"/"} element={<Homepage product={product}/>}/>
            <Route path={"/:id"} element={<ProductPage/>}/>
            <Route path={"/order"} element={<OrderPage/>}/>
            <Route path={"/shoppingCart"} element={<ShoppingCartPage/>}/>
            <Route path={"/admin"} element={<AdminPage handleOrderButton={handleOrderButton} handleProductButton={handleProductButton}/>}/>
            <Route path={"/admin/product"} element={<AdminProductPage product={product}/>}/>
            <Route path={"/admin/order"} element={<AdminOrderPage orderList={orderList}/>}/>
            <Route path={"/admin/product/add"} element={<AdminAddProductPage handleClickProduct={handleClickProduct}/>}/>
            <Route path={"/admin/product/:id"} element={<AdminDetailProductPage/>}/>
            <Route path={"/admin/product/update/:id"} element={<AdminUpdateProductPage handleCloseSuccess={handleCloseSuccess} showSuccess={showSuccess} setShowSuccess={setShowSuccess}/>}/>
        </Routes>
    </>
  )
}

export default App
