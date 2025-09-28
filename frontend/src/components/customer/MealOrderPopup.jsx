import React, { useState , useEffect } from 'react';
import './MealOrderPopup.css';
import MealComments from './MealComments';

const MealOrderPopup = ({ meal, onClose, onAddToCart }) => {

  const [customizationNote, setCustomizationNote] = useState('');
  const [quantity, setQuantity] = useState(1);






  const handleAddToCart = () => {
    onAddToCart(meal,quantity,customizationNote);
    onClose();
  };

  return (
    <div className="meal-popup-overlay">
      <div className="meal-popup">
        <button className="close-button" onClick={onClose}>✕</button>
        
        <div className="meal-name-gradient">
          <h2>{meal.name}</h2>
        </div>

        <div className="popup-content">
          <div className="image-section">
            <div className="meal-image">
              {meal.imageUrl && meal.imageUrl.startsWith('https') ? 
                (<img src={meal.imageUrl} alt={meal.name} /> ) : 
                (<div className="placeholder-image">No Image Available</div>)}
            </div>
          </div>

          <div className="meal-details">
            <div className="description">
              <p>{meal.description}</p>
            </div>
            {/* 
            <div className="rating">
              <span>
                <span className="star">★</span>
                {meal.rating}/5
              </span>
              <button className="heart-button">❤</button>
            </div>
           </div>
           */}
          </div>
          <div className="customization-section">
            <h3>Special Instructions</h3>
            <textarea
              placeholder="Add any special instructions for your order..."
              value={customizationNote}
              onChange={(e) => setCustomizationNote(e.target.value)}
            />
          </div>

          <div className="order-actions">
            <div className="quantity-selector">
              <button 
                onClick={() => setQuantity(q => Math.max(1, q - 1))}
                disabled={quantity <= 1}
              >
                -
              </button>
              <span>{quantity}</span>
              <button onClick={() => setQuantity(q => q + 1)}>
                +
              </button>
            </div>
            <div className="action-buttons">
              <button className="add-to-cart" onClick={handleAddToCart}>
                Add to cart
              </button>
              {/*
              <button className="order-now" onClick={handleAddToCart}>
                Order
              </button>
              */}
            </div>
          </div>

          {/* <MealComments mealId={meal.id} comments={meal.comments} />  Backend'de comment implementasyonu yok     */}
        </div>
      </div>
    </div>
  );
};

export default MealOrderPopup; 