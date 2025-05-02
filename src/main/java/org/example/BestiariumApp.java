package org.example;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class BestiariumApp {
    private JFrame frame;
    private JTree tree;
    private DefaultMutableTreeNode rootNode;
    private DefaultMutableTreeNode jsonNode;
    private DefaultMutableTreeNode xmlNode;
    private DefaultMutableTreeNode yamlNode;
    private Map<String, List<Monster>> monsterMap = new HashMap<>();
    private Set<String> importedFiles = new HashSet<>();

    public void init() throws Exception {
        frame = new JFrame("Bestiarium");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);

        JButton loadBtn = new JButton("Загрузить файл");
        loadBtn.addActionListener(e -> loadMonsters());

        JPanel exportPanel = new JPanel();
        JButton exportJsonBtn = new JButton("Экспорт JSON");
        JButton exportXmlBtn = new JButton("Экспорт XML");
        JButton exportYamlBtn = new JButton("Экспорт YAML");
        exportJsonBtn.addActionListener(e -> exportFormat("json"));
        exportXmlBtn.addActionListener(e -> exportFormat("xml"));
        exportYamlBtn.addActionListener(e -> exportFormat("yaml"));
        exportPanel.add(exportJsonBtn);
        exportPanel.add(exportXmlBtn);
        exportPanel.add(exportYamlBtn);

        rootNode = new DefaultMutableTreeNode("Чудовища");
        jsonNode = new DefaultMutableTreeNode("JSON");
        xmlNode = new DefaultMutableTreeNode("XML");
        yamlNode = new DefaultMutableTreeNode("YAML");
        rootNode.add(jsonNode);
        rootNode.add(xmlNode);
        rootNode.add(yamlNode);

        monsterMap.put("json", new ArrayList<>());
        monsterMap.put("xml", new ArrayList<>());
        monsterMap.put("yaml", new ArrayList<>());

        tree = new JTree(rootNode);
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected != null && selected.getUserObject() instanceof Monster) {
                editMonster((Monster) selected.getUserObject());
            }
        });

        frame.add(loadBtn, BorderLayout.NORTH);
        frame.add(new JScrollPane(tree), BorderLayout.CENTER);
        frame.add(exportPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void loadMonsters() {
        JFileChooser chooser = new JFileChooser(".");
        int res = chooser.showOpenDialog(frame);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            if (importedFiles.contains(filePath)) {
                JOptionPane.showMessageDialog(frame, "Этот файл уже был импортирован.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ImportHandler json = new JsonImportHandler();
            ImportHandler xml = new XmlImportHandler();
            ImportHandler yaml = new YamlImportHandler();
            json.setNext(xml);
            xml.setNext(yaml);

            try {
                List<Monster> newMonsters = json.handle(file);
                String ext = getExtension(file.getName());

                List<Monster> targetList = monsterMap.get(ext);
                DefaultMutableTreeNode formatNode = switch (ext) {
                    case "json" -> jsonNode;
                    case "xml" -> xmlNode;
                    case "yaml", "yml" -> yamlNode;
                    default -> null;
                };

                if (targetList != null && formatNode != null) {
                    targetList.addAll(newMonsters);
                    for (Monster m : newMonsters) {
                        formatNode.add(new DefaultMutableTreeNode(m));
                    }
                    ((DefaultTreeModel) tree.getModel()).reload();
                    importedFiles.add(filePath);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Ошибка загрузки: " + ex.getMessage());
            }
        }
    }

    private void exportFormat(String format) {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setSelectedFile(new File("Bestiarium_out." + format));
        int res = chooser.showSaveDialog(frame);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                List<Monster> list = monsterMap.get(format);
                if (list == null || list.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Нет данных для экспорта");
                    return;
                }

                switch (format) {
                    case "json" -> Exporter.exportJson(list, file);
                    case "xml" -> Exporter.exportXml(list, file);
                    case "yaml", "yml" -> Exporter.exportYaml(list, file);
                }

                JOptionPane.showMessageDialog(frame, "Экспорт завершён: " + file.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Ошибка экспорта: " + ex.getMessage());
            }
        }
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return (dot == -1) ? "" : filename.substring(dot + 1).toLowerCase();
    }

    private void editMonster(Monster m) {
        JTextField nameField = new JTextField(m.name);
        JTextField dangerField = new JTextField(String.valueOf(m.dangerLevel));
        JTextField heightField = new JTextField(
            m.characteristics != null && m.characteristics.height != null ? m.characteristics.height.toString() : ""
        );
        JTextField activeTimeField = new JTextField(
            m.characteristics != null ? m.characteristics.activeTime : ""
        );

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Имя:"));
        panel.add(nameField);
        panel.add(new JLabel("Уровень опасности:"));
        panel.add(dangerField);
        panel.add(new JLabel("Рост:"));
        panel.add(heightField);
        panel.add(new JLabel("Активное время:"));
        panel.add(activeTimeField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Редактировать чудовище", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            m.name = nameField.getText();

            try {
                m.dangerLevel = Integer.parseInt(dangerField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Неверный уровень опасности (ожидалось целое число)");
            }

            if (m.characteristics == null) m.characteristics = new Monster.Characteristics();
            try {
                String hText = heightField.getText().trim();
                m.characteristics.height = hText.isEmpty() ? null : Double.parseDouble(hText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Неверный рост (ожидалось число)");
            }

            m.characteristics.activeTime = activeTimeField.getText();
            tree.updateUI();
        }
    }
}
