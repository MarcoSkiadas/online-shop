import {Product} from "./ShopSchema.ts";
import {ChangeEvent, useState} from "react";
import ProductCard from "./ProductCard.tsx";

type SearchProps = {
    product: Product[]
}

export default function Search(props: Readonly<SearchProps>) {
    const [searchTerm, setSearchTerm] = useState<string>("");


    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(event.target.value);
    };

    const filteredItems = props.product.filter(product =>
        product.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
    const productList = filteredItems.map((product) => <ProductCard key={product.id} product={product}/>);

    return (
        <>
            <input
                type="text"
                placeholder="Search..."
                value={searchTerm}
                onChange={handleChange}
            />
            {productList}
        </>
    )
}