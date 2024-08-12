import {FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

export default function RegisterPage() {

    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const nav = useNavigate();

    function submitRegister(e: FormEvent<HTMLFormElement>) {
        e.preventDefault()
        axios.post("/api/auth/register", {username, password})
            .then(() => nav("/login"))
            .catch(error => alert(error.response.data.errorMsg))
    }

    return (
        <form onSubmit={submitRegister}>
            <input value={username} placeholder={"Please enter your Username"} type={"text"}
                   onChange={e => setUsername(e.target.value)}/>
            <input value={password} placeholder={"Please enter your Password"} type={"password"}
                   onChange={e => setPassword(e.target.value)}/>
            <button>Register</button>
        </form>
    )
}