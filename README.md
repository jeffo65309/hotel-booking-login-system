# 🏨 Hotel Booking System (Java)

This is a Java-based hotel room booking system created as part of a university project. It includes a secure login system to ensure that only authorised users can access the booking features.

---

## ✅ Features

- 🛏️ Add and view room bookings
- 🔐 Secure login system
- 🔄 File handling with `rooms.txt` and `users.txt`
- 👤 Password hashing & user authentication
- ☕ Built using OOP principles (Java)

---

## 🔐 Security Feature

This version includes a custom-built **UserManager** class to handle:

- Password creation and storage
- Login attempt validation
- Password hashing logic (via `Passwords.java`)
- User credential storage in `users.txt`

Users are securely prompted to log in before accessing or modifying bookings.

---

## 📁 Project Structure

```
/src
├── roomBookingSystem/
│ └── Room.java
│ └── RoomBookingSystem.java
└── security/
└── UserManager.java
└── Passwords.java
└── securityUser.java
/rooms.txt
/users.txt
```

---

## 🛠 How to Run It

1. Open the project in Eclipse or VS Code (with Java extensions)
2. Make sure the files in `/src` are in the correct package
3. Compile using:

```bash
javac src/**/*.java

