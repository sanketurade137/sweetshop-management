
import { useState } from "react";
import "./Navbar.css";

export default function Navbar({ user, onLogout }) {
  const [open, setOpen] = useState(false);

  const isAdmin = user?.role?.toUpperCase() === "ADMIN";

  return (
    <nav className="navbar">
      <div className="nav-left">
        <h2>🍬 Sweet Shop</h2>
      </div>

      {/* Admin toggle menu */}
      {isAdmin && (
        <div className="nav-center">
          <button className="admin-toggle" onClick={() => setOpen(!open)}>
            ☰ Admin
          </button>
          {open && (
            <div className="admin-dropdown">
              <p>
                Use the admin panel below to Add / Update / Restock / Delete products
              </p>
            </div>
          )}
        </div>
      )}

      {/* Right side: always show logout */}
      <div className="nav-right">
        {user ? (
          <>
            <span style={{ marginRight: "10px" }}>Hello, {user.userName}</span>
            <button className="logout-btn" onClick={onLogout}>
              Logout
            </button>
          </>
        ) : (
          <span>Please login</span>
        )}
      </div>
    </nav>
  );
}












