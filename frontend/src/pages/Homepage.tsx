import ProductCard from "../components/ProductCard.tsx";
import {Product} from "../components/ShopSchema.ts";

type HomepageProps = {
    product:Product[]
}
export default function Homepage(props:Readonly<HomepageProps>) {

    const product = props.product.map((product)=> <ProductCard key={product.id} product={product} />);
    return(
        <>
            {product}
        </>
    )
}