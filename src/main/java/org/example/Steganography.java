package org.example;
import java.awt.image.BufferedImage;

public class Steganography {

    // Method to encode a message in an image
    public static BufferedImage encode(BufferedImage image, String message) throws Exception {
        if (message.length() * 8 > image.getWidth() * image.getHeight()) {
            throw new Exception("Message is too long to encode in the image.");
        }

        // Convert message to binary
        StringBuilder binaryMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            String binaryChar = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            binaryMessage.append(binaryChar);
        }
        binaryMessage.append("11111111"); // End of message indicator

        int messageIndex = 0;

        // Encode the binary message into the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int newPixel = pixel;

                // Change the least significant bit if there is still a message to encode
                if (messageIndex < binaryMessage.length()) {
                    int bit = binaryMessage.charAt(messageIndex) - '0';
                    newPixel = (pixel & 0xFFFFFFFE) | bit; // Set LSB to the bit of the message
                    messageIndex++;
                }

                image.setRGB(x, y, newPixel);
                if (messageIndex >= binaryMessage.length()) {
                    return image; // Return the image if the message is fully encoded
                }
            }
        }
        return image; // Return the modified image
    }

    // Method to decode a message from an image
    public static String decode(BufferedImage image) {
        StringBuilder binaryMessage = new StringBuilder();

        // Read the least significant bits from the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int lsb = pixel & 1; // Get the least significant bit
                binaryMessage.append(lsb);
            }
        }

        // Convert the binary message to characters
        StringBuilder decodedMessage = new StringBuilder();
        for (int i = 0; i < binaryMessage.length(); i += 8) {
            String byteString = binaryMessage.substring(i, Math.min(i + 8, binaryMessage.length()));
            if (byteString.equals("11111111")) {
                break; // End of message indicator
            }
            decodedMessage.append((char) Integer.parseInt(byteString, 2));
        }
        return decodedMessage.toString();
    }
}