import {Product} from "../components/ShopSchema.ts";
import AdminProductCard from "../components/AdminProductCard.tsx";
import {useNavigate} from "react-router-dom";

type AdminProductPageProps = {
    product:Product[]
}
export default function AdminProductPage(props:Readonly<AdminProductPageProps>) {

    const navigate = useNavigate();
    const product = props.product.map((product)=> <AdminProductCard key={product.id} product={product} />)
    const handleClick = () => {
        navigate(`/admin/product/add`);
    }
    return (
        <>
            {product}
            <button onClick={handleClick}>Add Product</button>
        </>
    )
}