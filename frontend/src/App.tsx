import './App.css'
import Homepage from "./pages/Homepage.tsx";
import Navigation from "./components/Navigation.tsx";
import {Route, Routes} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";

export type Product = {
    id:string,
    name:string,
    price:number
}

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
        </Routes>
        <button onClick={getAllProducts}>getAllProducts</button>
    </>
  )
}

export default App
