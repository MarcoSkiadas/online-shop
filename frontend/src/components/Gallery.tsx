export default function Gallery() {

    return (
        <>
            <div>
                <form action="#" th:action={"@{/upload}"}
                      method={"post"}
                      encType={"multipart/form-data"}>
                    <input type={"file"} name={"image"} placeholder={"upload file"}/>
                    <input type={"submit"} value={"Submit"}/>

                </form>
            </div>
        </>
    )
}