import axios from "axios";
import {useParams} from "react-router-dom";
import {Product} from "../components/ShopSchema.ts";
import {useEffect, useState} from "react";

export default function ProductPage() {
    const { id } = useParams<{ id: string }>();
    const [product, setProduct] = useState<Product>();

    useEffect(() => {
        axios.get(`/api/product/${id}`)
            .then(response => {
                setProduct(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, [id]);

    if (!product) {
        return(
            <>
                <p>Product not found</p>
            </>)
    }

    return(
        <>
            <div>
            <h2>{product?.name}</h2>
                <p>Price: {product?.price} €</p>
            </div>

        </>
    )
}