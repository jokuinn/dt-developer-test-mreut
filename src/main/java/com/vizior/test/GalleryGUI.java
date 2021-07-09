package com.vizior.test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryGUI extends JFrame {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    private final File folder = new File("assets");
    private final File[] files = folder.listFiles();
    private final ArrayList<File> listOfImages;

    {
        assert files != null;
        listOfImages = new ArrayList<>(Arrays.asList(files));
    }

    private final JFrame frame = new JFrame("DT Developer Test");
    private final JLabel searchText = new JLabel("Search ");
    private final JTextField searchImage = new JTextField(30);
    private final JButton searchImageButton = new JButton("Search");
    private final JButton addImageButton = new JButton("Add Image");
    private final JPanel panel = new JPanel(new FlowLayout());
    private final JPanel images = new JPanel(new GridLayout(10, 10));
    private JButton button;


    public void run(){

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());


        searchImageButton.setBackground(Color.BLACK);
        searchImageButton.setForeground(Color.WHITE);
        searchImageButton.addActionListener(new SearchImageButtonActionListener());


        addImageButton.setBackground(Color.BLACK);
        addImageButton.setForeground(Color.WHITE);
        addImageButton.addActionListener(new AddImageButtonActionListener());


        panel.add(searchText);
        panel.add(searchImage);
        panel.add(searchImageButton);
        panel.add(addImageButton);
        frame.add(panel, BorderLayout.PAGE_START);


        for (File file: listOfImages){
            if (file.isFile()){
                addImage(file);
            }
        }
        JScrollPane scrollPane = new JScrollPane(images);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 0, 1000, 700);
        frame.add(scrollPane, FlowLayout.CENTER);

    }

    public void addImage(File file){
        button = new JButton();
        try {
            BufferedImage image = ImageIO.read(file);
            Image scaleImage = image.getScaledInstance(80,90,Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(scaleImage);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            button.setName(file.getName());
            button.setIcon(icon);
            button.addActionListener(new ShowImageButtonActionListener(image));
            images.add(button);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchImage(File file, String searchText){
        button = new JButton();
        try {
            BufferedImage image = ImageIO.read(file);
            Image scaleImage = image.getScaledInstance(80,90,Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(scaleImage);
            if (file.getName().contains(searchText)){
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setName(file.getName());
                button.setIcon(icon);
                button.addActionListener(new ShowImageButtonActionListener(image));
                images.add(button);
            }

        } catch (IOException es) {
            es.printStackTrace();
        }
    }

    public class AddImageButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = JOptionPane.showInputDialog("Input file path");
            System.out.println(filePath);
            File addedImage = new File(filePath);
            listOfImages.add(addedImage);
            addImage(addedImage);
            images.updateUI();
        }
    }

    public static class ShowImageButtonActionListener implements ActionListener{

        private final BufferedImage image;

        public ShowImageButtonActionListener(BufferedImage image){
            this.image = image;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ImageIcon icon = new ImageIcon(image);
            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(null, label);
        }
    }

    public class SearchImageButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            images.removeAll();
            String input = searchImage.getText();
            for (File file: listOfImages){
                if (file.isFile()){
                    searchImage(file, input);
                }
            }
            images.updateUI();
        }
    }
}
