
type AdminPageProps = {
    handleOrderButton:()=> void
    handleProductButton:()=> void
}
export default function AdminPage(props:Readonly<AdminPageProps>) {


    return (
        <>
        <button onClick={props.handleProductButton}>Product</button>
        <button onClick={props.handleOrderButton}>Order</button>
        </>
    )
}