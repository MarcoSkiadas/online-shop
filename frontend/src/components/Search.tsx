import {Product} from "./ShopSchema.ts";
import {ChangeEvent} from "react";
import ProductCard from "./ProductCard.tsx";
import AdminProductCard from "./AdminProductCard.tsx";
import {useLocation} from "react-router-dom";

type SearchProps = {
    product: Product[]
    searchTerm: string
    setSearchTerm: (x: string) => void
}

export default function Search(props: Readonly<SearchProps>) {

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        props.setSearchTerm(event.target.value);
    };

    const filteredItems = props.product.filter(product =>
        product.name.toLowerCase().includes(props.searchTerm.toLowerCase())
    );
    const location = useLocation()
    let productList
    if (location.pathname === (`/`)) {
        productList = filteredItems.map((product) => <ProductCard key={product.id} product={product}/>);
    } else {
        productList = filteredItems.map((product) => <AdminProductCard key={product.id} product={product}/>);
    }
    return (
        <>
            <input
                type="text"
                placeholder="Search..."
                value={props.searchTerm}
                onChange={handleChange}
            />
            {productList}
        </>
    )
}