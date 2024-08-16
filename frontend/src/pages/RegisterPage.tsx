import {FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {toast} from "react-toastify";

export default function RegisterPage() {

    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const nav = useNavigate();
    const backToLogin = () => {
        nav("/Login")
    }

    function submitRegister(e: FormEvent<HTMLFormElement>) {
        e.preventDefault()
        axios.post("/api/auth/register", {username, password})
            .then(() => toast.success("You have been registered with Username: " + username))
            .then(() => nav("/login"))
            .catch(error => toast.error(error.response.data.errorMsg))
    }

    return (
        <div className="register-page">
            <div className="register-form-container">
                <form onSubmit={submitRegister} className="register-form">
                    <input value={username} placeholder={"Please enter your Username"} type={"text"}
                           onChange={e => setUsername(e.target.value)} className="register-input"/>
                    <input value={password} placeholder={"Please enter your Password"} type={"password"}
                           onChange={e => setPassword(e.target.value)} className="register-input"/>
                    <button type={"submit"} className="register-button">Register</button>
                    <button type={"button"} onClick={backToLogin} className="back-to-login-button">Back to Login
                    </button>
                </form>
            </div>
        </div>
    )
}