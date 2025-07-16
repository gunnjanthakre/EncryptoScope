# 🔐 EncryptoScope-Steganography

EncryptoScope-Steganography is a Java-based application that combines **encryption**, **QR code generation**, and **steganography** to securely hide messages inside images.  

This project demonstrates how modern cryptography techniques can be combined with steganography to protect sensitive data, making it almost invisible to unintended recipients.

---

## ✨ Features

✅ AES-based text encryption and decryption  
✅ QR code generation for encrypted messages  
✅ Image-based steganography to hide or extract messages  
✅ Java Swing GUI for user-friendly interaction  
✅ Supports secure key generation and message hiding workflows

---

## 💻 Technologies Used

- **Java (Swing)** — For building the graphical user interface
- **Java Cryptography (javax.crypto)** — For AES encryption
- **ZXing (Zebra Crossing)** — For QR code generation
- **BufferedImage & ImageIO** — For image handling and processing

---

## 🚀 How It Works

1️⃣ User enters a message and a secret key.  
2️⃣ The message is encrypted using AES encryption.  
3️⃣ An encrypted message is converted to a QR code.  
4️⃣ The QR code is embedded inside a random image using steganography.  
5️⃣ The hidden message can be extracted and decrypted using the same key.

---

## ⚙️ Setup and Run

### Prerequisites

- Java JDK 11 or higher
- Maven (if using from source)

### Running

```bash
git clone https://github.com/gunjanthakre/EncryptoScope-Steganography.git
cd EncryptoScope-Steganography
# Build and run using your IDE or run:
mvn compile
