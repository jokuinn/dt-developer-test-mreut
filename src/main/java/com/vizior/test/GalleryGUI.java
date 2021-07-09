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
import javax.swing.filechooser.FileNameExtensionFilter;

public class GalleryGUI extends JFrame {
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    //создание массива файлов
    private final File folder = new File("assets");
    private final File[] files = folder.listFiles();
    private final ArrayList<File> listOfImages;

    {
        assert files != null;
        listOfImages = new ArrayList<>(Arrays.asList(files));
    }

    //компоненты GUI
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

        //описание кнопки поиска изображения
        searchImageButton.setBackground(Color.BLACK);
        searchImageButton.setForeground(Color.WHITE);
        searchImageButton.addActionListener(new SearchImageButtonActionListener());

        //описание кнопки добавления изображения
        addImageButton.setBackground(Color.BLACK);
        addImageButton.setForeground(Color.WHITE);
        addImageButton.addActionListener(new AddImageButtonActionListener());

        //добавление на панель всех компонентов
        panel.add(searchText);
        panel.add(searchImage);
        panel.add(searchImageButton);
        panel.add(addImageButton);
        frame.add(panel, BorderLayout.PAGE_START);

        //реализация добавления изображений в массив изображений
        for (File file: listOfImages){
            if (file.isFile()){
                addImage(file);
            }
        }
        //пагинация
        JScrollPane scrollPane = new JScrollPane(images);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(0, 0, 1000, 700);
        frame.add(scrollPane, FlowLayout.CENTER);

    }

    //функция добавления изображений в JPanel
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
            button.addActionListener(new ShowImageButtonActionListener(image, file.getName()));
            images.add(button);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //функция поиска изображения
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
                button.addActionListener(new ShowImageButtonActionListener(image, file.getName()));
                images.add(button);
            }

        } catch (IOException es) {
            es.printStackTrace();
        }
    }

    //Listener для кнопки добавления изображения
    public class AddImageButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FileNameExtensionFilter pngFilter = new FileNameExtensionFilter(".png", "png");
            FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter(".jpeg", "jpeg");
            FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter(".jpg", "jpg");
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.setAcceptAllFileFilterUsed(false);
            fileOpen.setFileFilter(pngFilter);
            fileOpen.setFileFilter(jpegFilter);
            fileOpen.setFileFilter(jpgFilter);
            int ret = fileOpen.showDialog(null, "Add Image");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File addedImage = fileOpen.getSelectedFile();
                listOfImages.add(addedImage);
                addImage(addedImage);
                images.updateUI();
            }
        }

    }

    //Listener для отображения полного размера изображения по нажатию на него
    public static class ShowImageButtonActionListener implements ActionListener{

        private final BufferedImage image;
        private final String imageName;

        public ShowImageButtonActionListener(BufferedImage image, String imageName){
            this.image = image;
            this.imageName = imageName;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            ImageIcon icon = new ImageIcon(image);
            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(label, "Image title: \n"
                            + imageName,
                    "Message with Image",
                    JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }

    //Listener для кнопки поиска изображения
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
