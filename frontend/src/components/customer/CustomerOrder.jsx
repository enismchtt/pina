  import React, { useState, useEffect } from 'react';
  import { useNavigate } from 'react-router-dom';
  import './CustomerOrder.css';
  import MealOrderPopup from './MealOrderPopup';

  import CustomerService from '../services/customerService';
  import RestaurantService from '../services/restaurantService';
  import OrderService from '../services/orderService';

  const CustomerOrder = () => {


    const navigate = useNavigate(); // to go placing order page

    const [searchQuery, setSearchQuery] = useState('');
    const [customer , setCustomer] = useState({});
    const [restaurant , setRestaurant] = useState({});
    const [menuitems, setMenuItems] = useState([]);
    const [filteredItems, setFilteredItems] = useState([]); // what you actually render

    const [cartItems, setCartItems] = useState([]);  // keeping items on frontendside  count - reset , ekstradan quantity özelliğiyle ekleniyor

    const [selectedMeal, setSelectedMeal] = useState(null); // güncel olarak seçilmiş olan menu anlamında pop-up yani

    const[totalpriceandItem,setTotalPriceandItem] = useState([0,0])


    const restaurantid = localStorage.getItem('restaurantid');
    const customerId = localStorage.getItem('customerid');

    // getting customer 
    useEffect(() => {
      if (customerId) {
        CustomerService.getCustomer(customerId)
          .then(response => {
            setCustomer(response.data);
          })
          .catch(error => {
            console.error("Error fetching customer:", error);
          });
      }
    }, []);


    // getting restaurant
    useEffect(() => {
      if (restaurantid) {
        RestaurantService.getRestaurant(restaurantid)
          .then(response => {
            setRestaurant(response.data);
          })
          .catch(error => {
            console.error("Error fetching restaurant:", error);
          });
      }
    }, []);

    //getting meals
    useEffect(() => { 
      if (restaurantid) {
        RestaurantService.getRestaurantMenu(restaurantid)
          .then(response => {
            setMenuItems(response.data);  // this will act like master we are not changing that again
            setFilteredItems(response.data);
            console.log('Menu items data:', response.data);
          })
          .catch(error => {
            console.error("Error fetching customer:", error);
          });
      }
    }, []); 

    //updates current total and item Array when AddtoCartActivated so whenCartItems changed
    useEffect(() => {
        // Calculate total items and total price based on cartItems
        const totalItems = cartItems.reduce((total, item) => total + (item.quantity || 1), 0);
        const totalPrice = cartItems
          .reduce((total, item) => total + (item.price * (item.quantity || 1)), 0)
          .toFixed(2);

        // Update the state with the new values
        setTotalPriceandItem([parseFloat(totalPrice), totalItems]);
   

    },[cartItems])



    // Applying filter for mealitems based on search 
      //filters restaurant according to query
    useEffect(() => {
      const q = searchQuery.trim().toLowerCase();
      if (!q) {
        // reset to full list whenever the box is empty
        setFilteredItems(menuitems);
      } else {
        setFilteredItems(
          menuitems.filter(item =>
            item.name.toLowerCase().includes(q)
          )
        );
      }
    }, [searchQuery]);
    
      




    const handleAddToCart = (meal, quantity, customizationNote) => {
      const mealWithOptions = {
        ...meal,
        quantity: quantity || 1,
        customizationNote: customizationNote ?? '', // ensure it's always a string ub tcan be man. in the popup
      };
    
      setCartItems(prevItems => {
        const existingIndex = prevItems.findIndex(item => item.id === mealWithOptions.id);
    
        if (existingIndex !== -1) {
          // Update quantity and note for existing item
          const updatedItems = [...prevItems];
          updatedItems[existingIndex] = {
            ...updatedItems[existingIndex],
            quantity: updatedItems[existingIndex].quantity + mealWithOptions.quantity,
            customizationNote: mealWithOptions.customizationNote || '', // keep empty if not provided
          };
          return updatedItems;
        }
    
        // Add new item
        return [...prevItems, mealWithOptions];
      });

   
    };




    const handlePlaceOrder = () => {
      //Setting cartItems and navigating to localStorage also I already have customer and restaurant id's
      localStorage.setItem('cartItems', JSON.stringify(cartItems));
      localStorage.setItem('customer',JSON.stringify(customer));
      localStorage.setItem('restaurant',JSON.stringify(restaurant));
      localStorage.setItem('totalprice',totalpriceandItem[0]);
      navigate('/customer/place-order');

    };
   














    /*
    const navigate = useNavigate();

    const toggleFavorite = (mealId) => {
      const updatedMeals = mockMeals.map(meal => {
        if (meal.id === mealId) {
          return { ...meal, isFavorite: !meal.isFavorite };
        }
        return meal;
      });
    };
    */


    const handleClosePopup = () => {
      setSelectedMeal(null);
    };


    const handleMealClick = (meal) => {
      setSelectedMeal(meal);
    };

    const handleTrackOrder = () => {
      localStorage.setItem("customerid",customerId)
      navigate('/customer/track-order');
    }


    const resetCart = () => {
      setCartItems([]);
    };


    const getTotalItems = () => {
      return cartItems.reduce((total, item) => total + (item.quantity || 1), 0);
    };
    
    const getTotalPrice = () => {
      return cartItems
        .reduce((total, item) => total + (item.price * (item.quantity || 1)), 0)
        .toFixed(2);
    };
    

    return (
      <div className="customer-order">
        <header className="header">
          <div className="header-left">
            <h1>Pina</h1>
            <div className="user-info">
              <span className="username">{customer.firstName}</span>
              <span className="account-type">Customer</span>
            </div>
          </div>
          <div className="header-center">
            <div className="search-bar">
              <input 
                type="text" 
                placeholder="Name or Category Of the Meal"
                value={searchQuery}
                onChange={(e) => { setSearchQuery(e.target.value)}}
                className="search-input"
              />
              <span>🔍</span>
            </div>
          </div>
          <div className="header-right">
            <div className="order-now-section" onClick={handleTrackOrder}>
            <button>
              Track Order
            </button>
            </div>
            <div className="cart-section">
              <button className="cart-button" onClick={handlePlaceOrder}>
                <span>🛒</span>
                <span className="item-count">{totalpriceandItem[0]} tl | {totalpriceandItem[1]} item</span>
              </button>
              <button className="reset-cart-button" onClick={resetCart}>
              Reset Basket
              </button>
            </div>
          </div>
        </header>

        {/* Restaurant Name */}
        <div className="restaurant-h">
          <h2>Restaurant : {restaurant.name}</h2> 
          <h4>{restaurant.cuisineType}</h4>
        </div>

            {/* Main Content */}
        <main className="main-content">
        <section className="meals-grid">
          {filteredItems.map((meal) => (
            <div key={meal.id} className="meal-card" onClick={() => handleMealClick(meal)} >
              <div className="meal-image">
                {meal.imageUrl && meal.imageUrl.startsWith('https') ? 
                (<img src={meal.imageUrl} alt={meal.name} /> ) : 
                (<div className="placeholder-image">No Image Available</div>)}
              </div>
              <div className="meal-info">
                <div className="meal-name-heart">
                  <h3>{meal.name}</h3>
                  {/*   FAVORI İÇİN GUNCEL MENUITEM VEYA ORDERITEMLARDA Favori özelliği yok
                  <button 
                    className={`heart-button ${meal.isFavorite ? 'active' : ''}`}
                    onClick={(e) => {
                      e.stopPropagation();
                      toggleFavorite(meal.id);
                    }}
                  >
                    ♥
                  </button>
                  */}
                </div>
                <div className="meal-details">
                  <div className="meal-price-add">
                    <span className="price">{meal.price} TL</span>
                    <button 
                      className="meal-card-add-button"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleAddToCart(meal,1,"");
                      }}
                    >
                      +
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </section>
      </main>


      {selectedMeal && (
        <MealOrderPopup
          meal={selectedMeal}
          onClose={handleClosePopup}
          onAddToCart={handleAddToCart}
        />
      )}


      </div>
    );
  };

  export default CustomerOrder; 