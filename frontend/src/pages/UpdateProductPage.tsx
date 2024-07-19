import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {Product} from "../components/ShopSchema.ts";
import {useEffect, useState} from "react";
import { Button, Modal } from 'react-bootstrap';


export default function UpdateProductPage() {
    const { id } = useParams<{ id: string }>();
    const [product, setProduct] = useState<Product>();
    const [name, setName] = useState('');
    const [price, setPrice] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [showSuccess, setShowSuccess] = useState(false);
    const navigate = useNavigate();

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

    function handleDelete() {
        try {
            axios.delete(`/api/product/${product?.id}`)
                .then(r=>console.log(r.data))
            setShowModal(false);
            setShowSuccess(true);
            navigate(`/`)
        } catch (error) {
            setError('Failed to delete product. Please try again.');
            setShowModal(false);
        }
    }

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
            <Button variant="danger" onClick={() => setShowModal(true)}>
                Delete Product
            </Button>

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Delete</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Are you sure you want to delete this product?
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal show={showSuccess} onHide={() => setShowSuccess(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Product Deleted</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    The product was successfully deleted.
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={() => setShowSuccess(false)}>
                        OK
                    </Button>
                </Modal.Footer>
            </Modal>

            {error && <p style={{ color: 'red' }}>{error}</p>}

        </>
    )
}