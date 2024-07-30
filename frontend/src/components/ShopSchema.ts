export interface Product {
    id: string;
    name: string;
    price: number
    quantity: Quantity
}

export interface Order {
    id: string,
    productIds: string[],
    price: number,
    userId: string
}

export interface ShoppingCart {
    productIds: string[]
}

export interface User {
    id: string,
    username: string,
    role: string,
    shoppingCart: ShoppingCart
}

export interface Quantity {
    amount: number
    unit: Unit
}

export enum Unit {
    PIECE = "PIECE",
    KILOGRAM = "KILOGRAM",
    LITER = "LITER",
    GRAM = "GRAM",
    METER = "METER"
}