type AdminPageProps = {
    handleOrderButton: () => void
    handleProductButton: () => void
}
export default function AdminPage(props: Readonly<AdminPageProps>) {


    return (
        <>
            <div className="action-page">
                <button onClick={props.handleProductButton} className={"action-button"}>Product</button>
                <button onClick={props.handleOrderButton} className={"action-button"}>Order</button>
            </div>
        </>
    )
}