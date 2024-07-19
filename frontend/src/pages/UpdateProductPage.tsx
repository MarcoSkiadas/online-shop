import axios from "axios";
import {useParams} from "react-router-dom";
import {Product} from "../components/ShopSchema.ts";
import {useEffect, useState} from "react";


export default function UpdateProductPage() {
    const { id } = useParams<{ id: string }>();
    const [product, setProduct] = useState<Product>();
    const [name, setName] = useState('');
    const [price, setPrice] = useState('');

    useEffect(() => {
        getProduct()
    }, [id]);

    const getProduct = () =>
        axios.get(`/api/product/${id}`)
            .then(response => {
                setProduct(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });

    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    // Funktion zum Umgang mit dem Formulartick
    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        try {
            await axios.put(`/api/product/${product?.id}`, {
                name,
                price: parseFloat(price),
            });
            await getProduct();

            setSuccess('Product updated successfully!');
            setError(null);
        } catch (err) {
            setError('Failed to update product. Please try again.');
            setSuccess(null);
        }
    };

    return(
        <>
            <form onSubmit={handleSubmit}>
                <div>
                    <p>{product?.name}</p>
                    <label htmlFor="name">Product Name:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div>
                <p>{product?.price}</p>
                    <label htmlFor="price">Product Price:</label>
                    <input
                        type="number"
                        id="price"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                        step="0.01" // Für Dezimalwerte
                    />
                </div>
                <button type="submit">Update Product</button>

                {error && <p style={{color: 'red'}}>{error}</p>}
                {success && <p style={{color: 'green'}}>{success}</p>}
            </form>

        </>
    )
}