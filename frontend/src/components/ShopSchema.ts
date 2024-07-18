export interface Product {
    id: string;
    name: string;
    price: number
}
export interface Order {
    id: string,
    productIds:string[] ,
        price:number
}