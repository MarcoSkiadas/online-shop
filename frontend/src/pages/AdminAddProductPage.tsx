import React, {ChangeEvent, useState} from "react";
import axios from "axios";
import {ProductDTO, Unit} from "../components/ShopSchema.ts";


type AdminAddProductPage = {
    handleClickProduct: () => void
    unitType: string[]
}
export default function AdminAddProductPage(props: Readonly<AdminAddProductPage>) {

    const [name, setName] = useState('');
    const [unit, setUnit] = useState<Unit>(Unit.PIECE);
    const [amount, setAmount] = useState('');
    const [price, setPrice] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const [image, setImage] = useState<File | null>(null);

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
        axios.post(`/api/product/upload`, data, {headers: {"Content-Type": "multipart/form-data"}})
            .then((response) => {
                console.log(response.data)
                setSuccess('Product updated successfully!');
                setError(null);
            })
            .catch(() => {
                setError('Failed to update product. Please try again.');
                setSuccess(null);
            });
    }


    return (
        <>
            <form onSubmit={handleSubmit} className="update-form">
                <div className="form-group">
                    <label htmlFor="name">Product Name:</label>
                    <input
                        type="text"
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="price">Product Price:</label>
                    <input
                        type="number"
                        id="price"
                        value={price}
                        onChange={(e) => setPrice(e.target.value)}
                        required
                        step="0.01"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="amount">Product Amount:</label>
                    <input
                        type="number"
                        id="amount"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        required
                        step="1"
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="unit">Product Unit:</label>
                    <select
                        id="unit"
                        name="unit"
                        value={props.unitType}
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
                <button type="submit" className="submit-button">Add Product</button>
                {error && <p style={{color: 'red'}}>{error}</p>}
                {success && <p style={{color: 'green'}}>{success}</p>}
            </form>
            <button onClick={props.handleClickProduct} className="back-button">Back to Product</button>

        </>
    )
}