
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api";
import { decodeJWT } from "../utils/jwtHelper";
import "./Login.css";

export default function Login({ setUser }) {
  const [form, setForm] = useState({ userName: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    setLoading(true);
    setError("");

    try {
      const res = await api.post("/auth/login", form, {
        headers: { "Content-Type": "application/json" },
      });

      const token = res.data.token || res.data;
      localStorage.setItem("token", token);

      const decoded = decodeJWT(token);
      console.log("Decoded JWT:", decoded);

      const userInfo = {
        userName: decoded.sub || form.userName,
        role: decoded.role || "USER",
      };

      setUser(userInfo);
      localStorage.setItem("user", JSON.stringify(userInfo));

      navigate("/dashboard"); 
    } catch (err) {
      console.error("Login failed:", err);
      setError("Invalid username or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-form">
      <h2 className="auth-title">Login</h2>

      <input
        className="auth-input"
        placeholder="Username"
        value={form.userName}
        onChange={(e) => setForm({ ...form, userName: e.target.value })}
      />

      <input
        className="auth-input"
        type="password"
        placeholder="Password"
        value={form.password}
        onChange={(e) => setForm({ ...form, password: e.target.value })}
      />

      <button className="auth-button" onClick={handleLogin} disabled={loading}>
        {loading ? "Logging in..." : "Login"}
      </button>

      {error && <p className="error-text">{error}</p>}

      <p className="auth-footer">
        Don’t have an account? <Link to="/register">Register</Link>
      </p>
    </div>
  );
}












