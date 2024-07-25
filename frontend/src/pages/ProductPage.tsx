import axios from "axios";
import {useParams} from "react-router-dom";
import {Product} from "../components/ShopSchema.ts";
import {useEffect, useState} from "react";
import ShowDetailProduct from "../components/ShowDetailProduct.tsx";

export default function ProductPage() {
    const {id} = useParams<{ id: string }>();
    const [product, setProduct] = useState<Product>();

    useEffect(() => {
        axios.get(`/api/product/${id}`)
            .then(response => {
                setProduct(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error.message);
            });
    }, [id]);

    return (
        <>
            <ShowDetailProduct product={product}/>
        </>
    )
}