// src/pages/Dashboard.jsx
import { useEffect, useState } from "react";
import api from "../api";
import ProductCard from "../components/ProductCard";
import Navbar from "../components/Navbar";
import { decodeJWT } from "../utils/jwtHelper";
import "./Dashboard.css";

export default function Dashboard({ user, setUser }) {
  const [products, setProducts] = useState([]);
  const [searchName, setSearchName] = useState("");
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");
  const [loadingPurchase, setLoadingPurchase] = useState(false);
  const [adminForm, setAdminForm] = useState({ name: "", catagory: "", price: "", stock: "" });
  const [currentUser, setCurrentUser] = useState(user);

  // Decode JWT and load user if not already set
  useEffect(() => {
    if (!currentUser) {
      const token = localStorage.getItem("token");
      if (token) {
        const decoded = decodeJWT(token);
        const userInfo = {
          userName: decoded.sub,
          role: decoded.role || "USER",
        };
        setCurrentUser(userInfo);
        setUser(userInfo);
        localStorage.setItem("user", JSON.stringify(userInfo));
      }
    }
  }, [currentUser, setUser]);

  const isAdmin = currentUser?.role?.toUpperCase() === "ADMIN";

  // Fetch products from backend
  const fetchProducts = async () => {
    try {
      const res = await api.get("/sweets");
      setProducts(res.data);
    } catch (err) {
      console.error(err);
      alert("Failed to load products.");
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  // Purchase function for normal users
  const handlePurchase = async (product) => {
    setLoadingPurchase(true);
    try {
      const token = localStorage.getItem("token");
      if (!token) return alert("Login required");

      const payload = {
        name: currentUser.userName,
        products: [{ id: product.id }],
        quantity: 1,
      };

      await api.post("/sweets/order/purchase", payload, {
        headers: { Authorization: `Bearer ${token}` },
      });
      alert(`Purchased ${product.name}`);
      fetchProducts();
    } catch {
      alert("Purchase failed");
    } finally {
      setLoadingPurchase(false);
    }
  };

  // Admin actions
  const handleAdminChange = (e) =>
    setAdminForm({ ...adminForm, [e.target.name]: e.target.value });

  const handleAddProduct = async () => {
    if (!isAdmin) return alert("Not authorized");
    try {
      await api.post("/sweets/add", adminForm, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      alert("Product added");
      setAdminForm({ name: "", catagory: "", price: "", stock: "" });
      fetchProducts();
    } catch {
      alert("Failed to add product");
    }
  };

  const handleUpdateProduct = async (id) => {
    if (!isAdmin) return alert("Not authorized");
    try {
      await api.put(`/sweets/update/${id}`, adminForm, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      alert("Product updated");
      fetchProducts();
    } catch {
      alert("Failed to update product");
    }
  };

  const handleRestockProduct = async (id) => {
    if (!isAdmin) return alert("Not authorized");
    const qty = prompt("Enter quantity to restock");
    if (!qty) return;
    try {
      await api.put(`/sweets/restock/${id}?quantity=${qty}`, null, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      alert("Product restocked");
      fetchProducts();
    } catch {
      alert("Failed to restock product");
    }
  };

  const handleDeleteProduct = async (id) => {
    if (!isAdmin) return alert("Not authorized");
    if (!window.confirm("Delete this product?")) return;
    try {
      await api.delete(`/sweets/delete/${id}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
      });
      alert("Product deleted");
      fetchProducts();
    } catch {
      alert("Failed to delete product");
    }
  };

  const handleSearchByName = async () => {
    if (!searchName.trim()) return fetchProducts();
    try {
      const res = await api.get(`/sweets/search/name?name=${searchName}`);
      setProducts(res.data);
    } catch {
      alert("Search failed");
    }
  };

  const handleSearchByPrice = async () => {
    if (!minPrice || !maxPrice) return alert("Enter min and max price");
    try {
      const res = await api.get(`/sweets/search/price?minPrice=${minPrice}&maxPrice=${maxPrice}`);
      setProducts(res.data);
    } catch {
      alert("Price search failed");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setUser(null);
    setCurrentUser(null);
  };

  if (!currentUser) return <div>Loading...</div>;

  return (
    <div>
      <Navbar user={currentUser} onLogout={handleLogout} />

      {/* Search */}
      <div className="search-bar">
        <input
          placeholder="Search by name..."
          value={searchName}
          onChange={(e) => setSearchName(e.target.value)}
        />
        <button onClick={handleSearchByName}>Search</button>
      </div>

      <div className="search-bar">
        <input
          type="number"
          placeholder="Min Price"
          value={minPrice}
          onChange={(e) => setMinPrice(e.target.value)}
        />
        <input
          type="number"
          placeholder="Max Price"
          value={maxPrice}
          onChange={(e) => setMaxPrice(e.target.value)}
        />
        <button onClick={handleSearchByPrice}>Search by Price</button>
      </div>

      {/* Admin Panel */}
      {isAdmin && (
        <div className="admin-panel">
          <h3>Admin Panel</h3>
          <input
            name="name"
            placeholder="Name"
            value={adminForm.name}
            onChange={handleAdminChange}
          />
          <input
            name="catagory"
            placeholder="Category"
            value={adminForm.catagory}
            onChange={handleAdminChange}
          />
          <input
            name="price"
            type="number"
            placeholder="Price"
            value={adminForm.price}
            onChange={handleAdminChange}
          />
          <input
            name="stock"
            type="number"
            placeholder="Stock"
            value={adminForm.stock}
            onChange={handleAdminChange}
          />
          <input
            name="imageurl"
             placeholder="Image URL"       // ✅ new field
             value={adminForm.imageurl || ""}
             onChange={handleAdminChange}
          />
          <button onClick={handleAddProduct}>Add Product</button>
        </div>
      )}

      {/* Products */}
      <div className="product-grid">
        {products.map((p) => (
          <ProductCard
            key={p.id}
            product={p}
            onPurchase={() => handlePurchase(p)}
            disabled={loadingPurchase}
            isAdmin={isAdmin}
            onUpdate={() => handleUpdateProduct(p.id)}
            onRestock={() => handleRestockProduct(p.id)}
            onDelete={() => handleDeleteProduct(p.id)}
          />
        ))}
      </div>
    </div>
  );
}















