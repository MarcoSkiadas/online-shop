import axios from "axios";
import {useParams} from "react-router-dom";
import {Product, ProductDTO, Unit} from "../components/ShopSchema.ts";
import React, {ChangeEvent, useEffect, useState} from "react";
import '../App.css';

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
    const [unit, setUnit] = useState<Unit>(Unit.PIECE);
    const [amount, setAmount] = useState('');
    const [price, setPrice] = useState('');
    const [image, setImage] = useState<File | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

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


    function onFileChange(event: ChangeEvent<HTMLInputElement>) {
        if (event.target.files) {
            setImage(event.target.files[0])
        }
    }

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        const updatedProduct: ProductDTO = {
            name,
            price: parseFloat(price),
            quantity: {
                amount: parseInt(amount),
                unit
            }
        }
        const data = new FormData();
        if (image) {
            data.append("file", image)
        }
        data.append("product", new Blob([JSON.stringify(updatedProduct)], {'type': "application/json"}))
        axios.put(`/api/product/upload/${product?.id}`, data, {headers: {"Content-Type": "multipart/form-data"}})
            .then((response) => {
                setProduct(response.data)
                setSuccess('Product updated successfully!');
                setError(null);
            })
            .catch(() => {
                setError('Failed to update product. Please try again.');
                setSuccess(null);
            });
    }

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
            <form onSubmit={handleSubmit} className="update-form">
                <div className="form-group">
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
                <div className="form-group">
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
                <div className="form-group">
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
                <div className="form-group">
                    <p>{product?.quantity.unit}</p>
                    <label htmlFor="unit">Product Unit:</label>
                    <select
                        id="unit"
                        name="unit"
                        value={unit}
                        onChange={event => setUnit(event.target.value as Unit)}
                        required
                    >
                        {props.unitType.map(unit => (
                            <option key={unit} value={unit}>{unit}</option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="image">Product Image:</label>
                    <input type='file' onChange={onFileChange}/>
                </div>

                <button type="submit" className="submit-button">Update Product</button>
                <button onClick={() => setShowModal(true)} className={"delete-button"}>
                    Delete Product
                </button>
                {error && <p className="error-message">{error}</p>}
                {success && <p className="success-message">{success}</p>}
            </form>


            {showModal && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={() => setShowModal(false)}>&times;</span>
                        <h2>Confirm Delete</h2>
                        <p>Are you sure you want to delete this product?</p>
                        <button onClick={() => setShowModal(false)}>Cancel</button>
                        <button onClick={handleDelete} className="delete-confirm-button">Delete</button>
                    </div>
                </div>
            )}

            {props.showSuccess && (
                <div className="modal">
                    <div className="modal-content">
                        <span className="close" onClick={() => props.setShowSuccess(false)}>&times;</span>
                        <h2>Product Deleted</h2>
                        <p>The product was successfully deleted.</p>
                        <button onClick={props.handleCloseSuccess} className="ok-button">OK</button>
                    </div>
                </div>
            )}

            {error && <p className="error-message">{error}</p>}
            <button onClick={props.handleClickProduct} className="back-button">Back to Product</button>

        </>
    )
}