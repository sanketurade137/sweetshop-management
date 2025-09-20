
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";

export default function Register() {
  const [form, setForm] = useState({ userName: "", password: "", role: "USER" }); 
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      await api.post("/auth/register", form, {
        headers: { "Content-Type": "application/json" }, 
      });
      alert("Registration successful!");
      navigate("/login"); // redirect to login page
    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <div className="auth-form">
      <h2>Register</h2>

      <input
        placeholder="Username"
        value={form.userName}
        onChange={e => setForm({ ...form, userName: e.target.value })}
      />

      <input
        type="password"
        placeholder="Password"
        value={form.password}
        onChange={e => setForm({ ...form, password: e.target.value })}
      />

      <select
        value={form.role}
        onChange={e => setForm({ ...form, role: e.target.value })}
      >
        <option value="USER">User</option>
        <option value="ADMIN">Admin</option>
      </select>

      <button onClick={handleRegister}>Register</button>

      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}

