import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Product, User} from "../components/ShopSchema.ts";
import axios from "axios";
import ShowDetailProduct from "../components/ShowDetailProduct.tsx";

type AdminDetailProductPageProps = {
    user: User | null
    fetchMe: () => void
}

export default function AdminDetailProductPage(props: Readonly<AdminDetailProductPageProps>) {
    const {id} = useParams<{ id: string }>();
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
                console.error('Error fetching data:', error.message);
            });
    }, [id]);

    return (
        <>
            <ShowDetailProduct product={product} user={props.user} fetchMe={props.fetchMe}/>
            {product &&
                <button onClick={handleClick}>Update Product</button>}
        </>
    )
}