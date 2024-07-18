import './App.css'
import Homepage from "./pages/Homepage.tsx";
import Navigation from "./components/Navigation.tsx";
import {Route, Routes} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";
import {Product} from "./components/ShopSchema.ts";
import ProductPage from "./pages/ProductPage.tsx";



function App() {

    const [product, setProduct] = useState<Product[]>([]);

    useEffect( () => {
        getAllProducts()
    },[])

    function getAllProducts() {
        axios.get("/api/product")
            .then(response => setProduct(response.data))
            .catch(error => console.log(error))
    }

  return (
    <>
        <header>
            <Navigation/>
        </header>
        <Routes>
            <Route path={"/"} element={<Homepage product={product}/>}/>
            <Route path={"/:id"} element={<ProductPage/>}/>
        </Routes>
    </>
  )
}

export default App
