export default function ProductCard({
  product,
  onPurchase,
  disabled,
  isAdmin,
  onUpdate,
  onRestock,
  onDelete
}) {
  return (
    <div className="product-card">
      <img src={product.imageurl} alt={product.name} />
      <h3>{product.name}</h3>
      <p>Category: {product.catagory}</p>
      <p>Price: ₹{product.price}</p>
      <p>Stock: {product.stock}</p>

      {!isAdmin && (
        <button disabled={disabled || product.stock === 0} onClick={onPurchase}>
          Purchase
        </button>
      )}

      {isAdmin && (
        <div style={{ marginTop: "10px" }}>
          <button onClick={onUpdate}>Update</button>
          <button onClick={onRestock}>Restock</button>
          <button onClick={onDelete}>Delete</button>
        </div>
      )}
    </div>
  );
}




