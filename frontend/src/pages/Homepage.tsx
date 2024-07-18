import {Product} from "../App.tsx";
import ProductCard from "../components/ProductCard.tsx";

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