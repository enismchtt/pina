import { useEffect, useState } from 'react';
import CustomerService from '../services/customerService';
import { useNavigate } from 'react-router-dom';
import './CustomerManageProfile.css'; // Import the CSS
import API from '../api_access/api';

const CustomerManageProfile = () => {
  const [customer, setCustomer] = useState(null);
  const navigate = useNavigate();
  const [newPassword, setNewPassword] = useState("");
  const [oldPassword, setOldPassword] = useState("");






  useEffect(() => {
    const customerId = localStorage.getItem('customerid');
    if (!customerId) {
      console.error("No customer ID found");
      return;
    }

    CustomerService.getCustomer(customerId)
      .then(res => {
        setCustomer(res.data)


      })
      .catch(err => {
        console.error("Failed to fetch customer", err);
      });
  }, []);

  const handleChange = (e) => {
    setCustomer({
      ...customer,
      [e.target.name]: e.target.value
    });
  };

    const handleSave = async () => {
    try {

        // kontrol kısmı burada kullanıcın aldığı oldpassword confirm için kullanılıyor !!!
        const auth_json = {
        "username": customer.username,
        "password": oldPassword
        };
        await API.post('/auth/check-password', auth_json);



        const id = localStorage.getItem("customerid")


        setCustomer({
            ...customer,
            "password": oldPassword
        });
        

        // burada password hariç diğer değerler gönderilerek update yapılıyor password e ne gönderdiğniz bu aşamada önemsiz handleSave bitmeden zaten değişicek
        await CustomerService.updateCustomer(id, customer); 

 
        const finalNewPassword = newPassword === "" ? oldPassword : newPassword; // yeni password kısmını kullanıcı boş bıraktıysa otomatik eski passwordu atıyor.

        const chng_password_json = {
        "username": customer.username,
        "newPassword": finalNewPassword,
        };

        await API.post('/auth/change-password', chng_password_json);

        alert("Customer updated successfully");
    } catch (err) {
        console.error("Failed to update customer", err);
        alert("Failed to update customer");
    }
    };


  if (!customer) {
    return <div>Loading customer data...</div>;
  }

  return (
    <div className="profile-container">
  <div className="header">
  <div className="logo-group">
    <span className="logo-icon">🍍</span>
    <span className="logo-text">Pina</span>
    <span className="sub-title">Edit Profile</span>
  </div>
</div>


  <div className="form-wrapper">
    <div className="form-container">
      <div className="form-row">
        <div className="form-group">
          <label>Username:</label>
          <input name="username" value={customer.username} readOnly/>
        </div>
        <div className="form-group">
          <label>New Password:</label>
          <input name="newPassword" type="password" value={newPassword  } onChange={(e) => setNewPassword(e.target.value) } />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
          <label>First Name:</label>
          <input name="firstName" value={customer.firstName || ''} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Last Name:</label>
          <input name="lastName" value={customer.lastName} onChange={handleChange} />
        </div>
      </div>

      <div className="form-row">
        <div className="form-group">
        <label>Address:</label>
        <input name="address" value={customer.address || ''} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Phone Number:</label>
          <input name="phoneNumber" value={customer.phoneNumber || ''} onChange={handleChange} />
        </div>
        <div className="form-group" style={{ visibility: 'hidden' }}></div>
      </div>

    <div className="form-row align-center">
    <div className="form-group">
        <label>Confirm Current Password:</label>
       <input name="oldPassword" type="password" value={oldPassword} onChange={e => setOldPassword(e.target.value)} />
    </div>
    <div className="form-group button-group">
        <button className="save-button" onClick={handleSave}>Save</button>
    </div>
    </div>

    </div>
  </div>
</div>
  );
};

export default CustomerManageProfile;
