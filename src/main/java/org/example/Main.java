package org.example;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.Result;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {

    private static SecretKey secretKey;

    private JTextField messageField;

    private JLabel inputImageLabel;

    private File inputImageFile;

    private BufferedImage randomImage;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main mainFrame = new Main();
            mainFrame.createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Encryption, QR Code, and Steganography Tool");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel textPanel = createTextEncryptionPanel();
        JPanel qrPanel = createQRCodePanel();
        JPanel steganographyPanel = createSteganographyPanel();

        tabbedPane.addTab("Text Encryption", textPanel);
        tabbedPane.addTab("QR Code", qrPanel);
        tabbedPane.addTab("Steganography", steganographyPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);

        // Generate SecretKey
        secretKey = generateKey(128);
    }

    private static JPanel createTextEncryptionPanel() {
        JPanel textPanel = new JPanel(null);

        JLabel plaintextLabel = new JLabel("Plaintext:");
        plaintextLabel.setBounds(10, 20, 80, 25);
        textPanel.add(plaintextLabel);

        JTextField plaintextField = new JTextField();
        plaintextField.setBounds(100, 20, 350, 25);
        textPanel.add(plaintextField);

        JLabel encryptedLabel = new JLabel("Encrypted:");
        encryptedLabel.setBounds(10, 60, 80, 25);
        textPanel.add(encryptedLabel);

        JTextField encryptedField = new JTextField();
        encryptedField.setBounds(100, 60, 350, 25);
        textPanel.add(encryptedField);

        JLabel decryptedLabel = new JLabel("Decrypted:");
        decryptedLabel.setBounds(10, 100, 80, 25);
        textPanel.add(decryptedLabel);

        JTextField decryptedField = new JTextField();
        decryptedField.setBounds(100, 100, 350, 25);
        textPanel.add(decryptedField);

        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(10, 140, 150, 25);
        textPanel.add(encryptButton);

        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setBounds(170, 140, 150, 25);
        textPanel.add(decryptButton);

        JButton clearTextButton = new JButton("Clear");
        clearTextButton.setBounds(330, 140, 150, 25);
        textPanel.add(clearTextButton);

        // Action listeners for Text Encryption/Decryption
        encryptButton.addActionListener(e -> {
            try {
                String plaintext = plaintextField.getText();
                String encryptedText = encrypt(plaintext, secretKey);
                encryptedField.setText(encryptedText);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        decryptButton.addActionListener(e -> {
            try {
                String encryptedText = encryptedField.getText();
                String decryptedText = decrypt(encryptedText, secretKey);
                decryptedField.setText(decryptedText);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        clearTextButton.addActionListener(e -> {
            plaintextField.setText("");
            encryptedField.setText("");
            decryptedField.setText("");
        });

        return textPanel;
    }

    private static JPanel createQRCodePanel() {
        JPanel qrPanel = new JPanel(null);

        // Label for QR text input
        JLabel qrLabel = new JLabel("Text:");
        qrLabel.setBounds(10, 20, 80, 25);
        qrPanel.add(qrLabel);

        // Text field for QR input
        JTextField qrTextField = new JTextField();
        qrTextField.setBounds(100, 20, 350, 25);
        qrPanel.add(qrTextField);

        // Label to display the generated QR code
        JLabel qrCodeDisplay = new JLabel();
        qrCodeDisplay.setBounds(100, 60, 350, 200);
        qrPanel.add(qrCodeDisplay);

        // Button to generate QR code
        JButton generateQRButton = new JButton("Generate QR");
        generateQRButton.setBounds(10, 280, 150, 25);
        qrPanel.add(generateQRButton);

        // Button to load an existing QR code image
        JButton loadQRButton = new JButton("Load QR");
        loadQRButton.setBounds(170, 280, 150, 25);
        qrPanel.add(loadQRButton);

        // Button to clear the input and display
        JButton clearQRButton = new JButton("Clear");
        clearQRButton.setBounds(330, 280, 150, 25);
        qrPanel.add(clearQRButton);

        // Action listener for generating QR code
        generateQRButton.addActionListener(e -> {
            try {
                // Get the text from the QR text field
                String qrText = qrTextField.getText();

                // Generate the QR code image
                BufferedImage qrCode = generateQRCodeImage(qrText);

                // Display the QR code in the UI
                qrCodeDisplay.setIcon(new ImageIcon(qrCode));

                // Define the output directory and file naming conventions
                String outputDirPath = "C:/Users/gunjan thakre/Pictures/";
                String baseName = "qrcode";
                String extension = ".png";
                int count = 1;

                // Create a new file object for saving the QR code
                File outputImageFile = new File(outputDirPath + baseName + count + extension);

                // Check for existing files and increment count for the new output file name
                while (outputImageFile.exists()) {
                    count++;
                    outputImageFile = new File(outputDirPath + baseName + count + extension);
                }

                // Save the QR code to a file
                saveQRCodeToFile(qrCode, outputImageFile);

                // Alert the user with the saved file name
                JOptionPane.showMessageDialog(qrPanel, "QR Code saved as: " + outputImageFile.getName());

            } catch (Exception ex) {
                // Print stack trace for debugging purposes
                ex.printStackTrace();

                // Show an error message dialog to inform the user of the error
                JOptionPane.showMessageDialog(qrPanel, "An error occurred while generating or saving the QR Code: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for loading an existing QR code image
        loadQRButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select QR Code Image");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "bmp"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    String decodedText = decodeQRCodeFromFile(selectedFile.getAbsolutePath());
                    qrTextField.setText(decodedText);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(qrPanel, "Failed to decode QR Code: " + ex.getMessage());
                }
            }
        });

        // Action listener for clearing input and display
        clearQRButton.addActionListener(e -> {
            qrTextField.setText("");
            qrCodeDisplay.setIcon(null); // Clear displayed QR code
        });

        return qrPanel;
    }

    // Method to save the QR code to a file
    private static void saveQRCodeToFile(BufferedImage qrCode, File outputImageFile) throws IOException {
        // Save the BufferedImage (QR Code) to the file as a PNG image
        ImageIO.write(qrCode, "png", outputImageFile);
    }


    private JPanel createSteganographyPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10)); // Increased rows to accommodate the preview
        inputImageLabel = new JLabel("No image selected");

        JButton selectImageButton = new JButton("Select Input Image");
        JButton encodeButton = new JButton("Encode Message");
        JButton clearButton = new JButton("Clear");
        JButton decodeButton = new JButton("Decode");

        messageField = new JTextField();
        messageField.setToolTipText("Enter the message to hide in the image");

        // Create a JLabel for displaying the encoded image
        JLabel encodedImagePreview = new JLabel();
        encodedImagePreview.setHorizontalAlignment(SwingConstants.CENTER);
        encodedImagePreview.setPreferredSize(new Dimension(800, 800)); // Set preferred size for the preview

        // Style the buttons
        JButton[] buttons = {selectImageButton, encodeButton, clearButton, decodeButton};
        for (JButton button : buttons) {
            button.setBackground(new Color(144, 238, 144)); // Light green
            button.setForeground(Color.BLACK);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(button); // Add buttons to the panel
        }

        panel.add(new JLabel("Enter Message:"));
        panel.add(messageField);

        // Add a label to display the selected input image
        panel.add(inputImageLabel);

        // Add the encoded image preview label to the panel
        panel.add(encodedImagePreview);

        // Action to select input image from a folder
        selectImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select an Image");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "bmp"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                inputImageFile = fileChooser.getSelectedFile();
                inputImageLabel.setText("Selected Image: " + inputImageFile.getName());
                randomImage = null; // Reset the random image if a real image is selected
            }
        });

        // Action to encode message into the image
        encodeButton.addActionListener(e -> {
            String message = messageField.getText();
            if (inputImageFile == null && randomImage == null) {
                randomImage = generateRandomImage(300, 300); // Generate a random image if none is selected
                inputImageLabel.setText("Random image generated for encoding");
            }

            if (!message.isEmpty()) {
                String outputDirPath = "C:/Users/gunjan thakre/Pictures/";
                String baseName = "output";
                String extension = ".png";
                int count = 1;

                File outputImageFile = new File(outputDirPath + baseName + count + extension);

                // Check for existing files and increment count for the new output file name
                while (outputImageFile.exists()) {
                    count++;
                    outputImageFile = new File(outputDirPath + baseName + count + extension);
                }

                // Encode the message in the image
                BufferedImage img = (inputImageFile != null) ? loadImage(inputImageFile) : randomImage;

                try {
                    BufferedImage encodedImage = Steganography.encode(img, message); // Assuming Steganography class is implemented

                    // Save the encoded image to a file
                    ImageIO.write(encodedImage, "png", outputImageFile);
                    JOptionPane.showMessageDialog(panel, "Message encoded successfully in " + outputImageFile.getName());

                    // Update preview with encoded image
                    ImageIcon icon = new ImageIcon(encodedImage);
                    icon = new ImageIcon(icon.getImage().getScaledInstance(800, 800, Image.SCALE_SMOOTH)); // Scale to fit 800x800
                    encodedImagePreview.setIcon(icon); // Set icon to JLabel

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Encoding failed: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please enter a message to encode.");
            }
        });

        // Action to decode message from the image
        decodeButton.addActionListener(e -> {
            if (inputImageFile != null || randomImage != null) {
                BufferedImage img = (inputImageFile != null) ? loadImage(inputImageFile) : randomImage;
                String decodedMessage = Steganography.decode(img); // Assuming Steganography class is implemented

                if (decodedMessage != null) {
                    messageField.setText(decodedMessage);
                } else {
                    JOptionPane.showMessageDialog(panel, "No message found in the image.");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select an image.");
            }
        });

        // Action to clear fields
        clearButton.addActionListener(e -> {
            messageField.setText("");
            inputImageLabel.setText("No image selected");
            randomImage = null;
            encodedImagePreview.setIcon(null); // Clear the displayed encoded image
        });

        return panel;
    }

    private static SecretKey generateKey(int n) {
        try {
            KeyGenerator keyGenerator=KeyGenerator.getInstance("AES");
            keyGenerator.init(n);
            return keyGenerator.generateKey();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String encrypt(String data , SecretKey key) throws Exception {
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] encryptedData=cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    private static String decrypt(String encryptedData , SecretKey key) throws Exception {
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] decryptedData=cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }

    private static BufferedImage generateQRCodeImage(String text) throws Exception {
        QRCodeWriter qrCodeWriter=new QRCodeWriter();
        BitMatrix bitMatrix=qrCodeWriter.encode(text , BarcodeFormat.QR_CODE ,200 ,200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private static void saveQRCodeToFile(BufferedImage qrCode) throws IOException {
        String outputDirPath = "C:/Users/gunjan thakre/Pictures/";
        String baseName = "qrcode";
        String extension = ".png";
        int count = 1;

        File outputImageFile = new File(outputDirPath + baseName + count + extension);

        // Check for existing files and increment count for the new output file name
        while (outputImageFile.exists()) {
            count++;
            outputImageFile = new File(outputDirPath + baseName + count + extension);
        }

        // Create the parent directories if they do not exist
        outputImageFile.getParentFile().mkdirs();

        // Save the QR code image as a PNG file
        ImageIO.write(qrCode, "png", outputImageFile);
    }

    private static String decodeQRCodeFromFile(String filePath) throws Exception {
        File qrFile=new File(filePath);

        // Check if the file exists
        if(!qrFile.exists()) { throw new IOException("QR code file does not exist: "+filePath); }

        // Read the image from the file
        BufferedImage bufferedImage= ImageIO.read(qrFile);

        if(bufferedImage==null){ throw new IOException ("Failed to decode image,image might be unsupported or corrupted.");}

        // Decode the QR code
        BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        Result result=new MultiFormatReader().decode(binaryBitmap);

        return result.getText();
    }

    private BufferedImage loadImage(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage generateRandomImage(int width,int height){
        BufferedImage image=new BufferedImage(width,height ,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d=image.createGraphics();

        Random random=new Random();

        g2d.setColor(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
        g2d.fillRect(0 ,0,width,height );
        g2d.dispose();

        return image;
    }
}