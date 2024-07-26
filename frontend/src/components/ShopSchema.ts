export interface Product {
    id: string;
    name: string;
    price: number
}

export interface Order {
    id: string,
    productIds: string[],
    price: number
}

export interface ShoppingCart {
    id: string,
    productIds: string[]
}

export interface User {
    id: string,
    username: string,
    role: string
}