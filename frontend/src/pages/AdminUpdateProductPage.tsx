import axios from "axios";
import {useParams} from "react-router-dom";
import {Product} from "../components/ShopSchema.ts";
import React, {useEffect, useState} from "react";
import '../components/styles.css';

type UpdateProductPageProps = {
    handleCloseSuccess: () => void
    showSuccess: boolean
    setShowSuccess: React.Dispatch<React.SetStateAction<boolean>>
    handleClickProduct: () => void
    unitType: string[]
}

export default function AdminUpdateProductPage(props: Readonly<UpdateProductPageProps>) {
    const {id} = useParams<{ id: string }>();
    const [product, setProduct] = useState<Product>();
    const [name, setName] = useState('');
    const [price, setPrice] = useState('');
    const [unit, setUnit] = useState('');
    const [amount, setAmount] = useState('');
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        getProduct()
    }, [id]);

    const getProduct = () =>
        axios.get(`/api/product/${id}`)
            .then(response => {
                setProduct(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error.message);
            });

    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        try {
            await axios.put(`/api/product/${product?.id}`, {
                name,
                price: parseFloat(price),
                quantity: {
                    amount,
                    unit
                }
            });
            await getProduct();

            setSuccess('Product updated successfully!');
            setError(null);
        } catch (err) {
            setError('Failed to update product. Please try again.');
            setSuccess(null);
        }
    };

    function handleDelete() {
        try {
            axios.delete(`/api/product/${product?.id}`)
                .then(r => console.log(r.data))
            setShowModal(false);
            props.setShowSuccess(true);

        } catch (error) {
            setError('Failed to delete product. Please try again.');
            setShowModal(false);
        }
    }

    return (
        <>
            <form onSubmit={handleSubmit}>
                <div>
                    <p>{product?.name}</p>
                    <label htmlFor="name">Product Name:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        placeholder={product?.name}
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
                        placeholder={product?.price.toString()}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                        step="0.01" // Für Dezimalwerte
                    />
                </div>
                <div>
                    <p>{product?.quantity.amount}</p>
                    <label htmlFor="amount">Product Amount:</label>
                    <input
                        type="number"
                        id="amount"
                        value={amount}
                        placeholder={product?.quantity.amount.toString()}
                        onChange={(e) => setAmount(e.target.value)}
                        required
                        step="1"
                    />
                </div>
                <div>
                    <p>{product?.quantity.unit}</p>
                    <label htmlFor="unit">Product Unit:</label>
                    <select
                        id="unit"
                        name="unit"
                        value={unit}
                        onChange={event => setUnit(event.target.value)}
                        required
                    >
                        {props.unitType.map(unit => (
                            <option key={unit} value={unit}>{unit}</option>
                        ))}
                    </select>
                </div>
                <button type="submit">Update Product</button>

                {error && <p style={{color: 'red'}}>{error}</p>}
                {success && <p style={{color: 'green'}}>{success}</p>}
            </form>
            <button onClick={() => setShowModal(true)}>
                Delete Product
            </button>

            {showModal && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={() => setShowModal(false)}>&times;</span>
                        <h2>Confirm Delete</h2>
                        <p>Are you sure you want to delete this product?</p>
                        <button onClick={() => setShowModal(false)}>Cancel</button>
                        <button onClick={handleDelete}>Delete</button>
                    </div>
                </div>
            )}

            {props.showSuccess && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={() => props.setShowSuccess(false)}>&times;</span>
                        <h2>Product Deleted</h2>
                        <p>The product was successfully deleted.</p>
                        <button onClick={props.handleCloseSuccess}>OK</button>
                    </div>
                </div>
            )}

            {error && <p style={{color: 'red'}}>{error}</p>}
            <button onClick={props.handleClickProduct}>Back to Product</button>

        </>
    )
}