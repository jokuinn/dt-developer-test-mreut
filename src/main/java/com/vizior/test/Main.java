package com.vizior.test;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GalleryGUI()::run);
    }
}
