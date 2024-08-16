import {Product} from "../components/ShopSchema.ts";
import {useNavigate} from "react-router-dom";
import Search from "../components/Search.tsx";

type AdminProductPageProps = {
    product: Product[]
    searchTerm: string
    setSearchTerm: (x: string) => void
}
export default function AdminProductPage(props: Readonly<AdminProductPageProps>) {

    const navigate = useNavigate();
    const handleClick = () => {
        navigate(`/admin/product/add`);
    }
    return (
        <div className="container">
            <div className="search-container">
                {<Search product={props.product} searchTerm={props.searchTerm}
                         setSearchTerm={props.setSearchTerm}/>}
            </div>
            <button onClick={handleClick}>Add Product</button>
        </div>
    )
}