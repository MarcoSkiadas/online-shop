import React, {useState} from "react";
import axios from "axios";


type AdminAddProductPage = {
    handleClickProduct: () => void
    unitType: string[]
}
export default function AdminAddProductPage(props: Readonly<AdminAddProductPage>) {

    const [name, setName] = useState('');
    const [unit, setUnit] = useState('');
    const [amount, setAmount] = useState('');
    const [price, setPrice] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);


    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        try {
            await axios.post(`/api/product`, {
                name,
                price: parseFloat(price),
                quantity: {
                    amount,
                    unit
                }
            });
            setSuccess('Product added successfully!');
            setError(null);
            setPrice('')
            setName('')
            setUnit('')
            setAmount('')
        } catch (err) {
            setError('Failed to add product. Please try again.');
            setSuccess(null);
        }
    };


    return (
        <>
            <form onSubmit={handleSubmit}>
                <div>
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
                <div>
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
                <div>
                    <label htmlFor="unit">Product Unit:</label>
                    <select
                        id="unit"
                        name="unit"
                        value={props.unitType}
                        onChange={event => setUnit(event.target.value)}
                        required
                    >
                        {props.unitType.map(unit => (
                            <option key={unit} value={unit}>{unit}</option>
                        ))}
                    </select>
                </div>
                <button type="submit">Add Product</button>

                {error && <p style={{color: 'red'}}>{error}</p>}
                {success && <p style={{color: 'green'}}>{success}</p>}
            </form>
            <button onClick={props.handleClickProduct}>Back to Product</button>
        </>
    )
}