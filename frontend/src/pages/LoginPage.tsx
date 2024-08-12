import {FormEvent, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

type LoginPageProps = {
    setUser: (username: string) => void
    login: () => void
}
export default function LoginPage(props: Readonly<LoginPageProps>) {

    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("")

    const nav = useNavigate();

    function submitLogin(e: FormEvent<HTMLFormElement>) {
        e.preventDefault()
        axios.post("/api/auth/login", undefined, {auth: {username, password}})
            .then(r => props.setUser(r.data))
            .then(() => nav("/"))
    }

    function register() {
        nav(`/register`)
    }

    return (
        <>
            <form onSubmit={submitLogin}>
                <input value={username} placeholder={"Please enter your Username"} type={"text"}
                       onChange={e => setUsername(e.target.value)}/>
                <input value={password} placeholder={"Please enter your Password"} type={"password"}
                       onChange={e => setPassword(e.target.value)}/>
                <button>Login</button>
            </form>
            <button onClick={register}>Register</button>
            <button onClick={props.login}>GitHub Login</button>
        </>
    )
}