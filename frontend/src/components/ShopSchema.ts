export interface Product {
    id: string,
    name: string,
    price: number,
    quantity: Quantity,
    imageUrl: string
}

export interface Order {
    id: string,
    orderedProducts: OrderedProduct[],
    price: number,
    userId: string
}

export interface ShoppingCart {
    orderedProducts: OrderedProduct[]
}

export interface OrderedProduct {
    productId: string,
    amount: number
}

export interface User {
    id: string,
    username: string,
    role: string,
    shoppingCart: ShoppingCart
}

export interface Quantity {
    amount: number,
    unit: Unit
}

export enum Unit {
    PIECE = "PIECE",
    KILOGRAM = "KILOGRAM",
    LITER = "LITER",
    GRAM = "GRAM",
    METER = "METER"
}