import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Product} from "../components/ShopSchema.ts";
import axios from "axios";

export default function AdminDetailProductPage() {
    const { id } = useParams<{ id: string }>();
    const [product, setProduct] = useState<Product>();
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/admin/product/update/${id}`);
    }

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
                <button onClick={handleClick}>Update Product</button>
            </div>

        </>
    )
}