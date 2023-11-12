package org.spielhagen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Vokabeltrainer extends JFrame {
    private Map<String, String> vokabeln;
    private JTextField deutschField;
    private JTextField englischField;
    private JButton hinzufuegenButton;
    private JButton abfragenButton;
    private JLabel frageLabel;
    private JTextField antwortField;
    private JButton pruefenButton;
    private JLabel ergebnisLabel;
    private Iterator<Map.Entry<String, String>> iterator;
    private Map.Entry<String, String> aktuelleVokabel;

    public Vokabeltrainer() {
        vokabeln = new HashMap<>();

        // GUI-Komponenten initialisieren
        JLabel deutschLabel = new JLabel("Deutsch:");
        deutschField = new JTextField(20);
        JLabel englischLabel = new JLabel("Englisch:");
        englischField = new JTextField(20);
        hinzufuegenButton = new JButton("Hinzufügen");
        abfragenButton = new JButton("Abfragen");
        frageLabel = new JLabel();
        antwortField = new JTextField(20);
        pruefenButton = new JButton("Prüfen");
        ergebnisLabel = new JLabel();

        // Layout festlegen
        setLayout(new GridLayout(5, 2));
        add(deutschLabel);
        add(deutschField);
        add(englischLabel);
        add(englischField);
        add(hinzufuegenButton);
        add(abfragenButton);
        add(frageLabel);
        add(antwortField);
        add(pruefenButton);
        add(ergebnisLabel);

        // ActionListener für den Hinzufügen-Button
        hinzufuegenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String deutsch = deutschField.getText();
                String englisch = englischField.getText();
                vokabelHinzufuegen(deutsch, englisch);
                deutschField.setText("");
                englischField.setText("");
                JOptionPane.showMessageDialog(Vokabeltrainer.this, "Die Vokabel wurde hinzugefügt.");
            }
        });

        // ActionListener für den Abfragen-Button
        abfragenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                starteAbfrage();
            }
        });

        // ActionListener für den Prüfen-Button
        pruefenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pruefeAntwort();
            }
        });

        // Fenster-Einstellungen
        setTitle("Vokabeltrainer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        ladeVokabeln();
    }

    public void vokabelHinzufuegen(String deutsch, String englisch) {
        vokabeln.put(deutsch, englisch);
        speichereVokabeln();
    }

    private void ladeVokabeln() {
        try (BufferedReader reader = new BufferedReader(new FileReader("vokabeln.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    vokabeln.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Vokabeln: " + e.getMessage());
        }
    }

    private void speichereVokabeln() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vokabeln.txt"))) {
            for (Map.Entry<String, String> entry : vokabeln.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Vokabeln: " + e.getMessage());
        }
    }

    private void starteAbfrage() {
        if (vokabeln.isEmpty()) {
            JOptionPane.showMessageDialog(Vokabeltrainer.this, "Es sind keine Vokabeln vorhanden.");
            return;
        }

        iterator = vokabeln.entrySet().iterator();
        if (iterator.hasNext()) {
            aktuelleVokabel = iterator.next();
            frageLabel.setText("Wie lautet die englische Übersetzung von \"" + aktuelleVokabel.getKey() + "\"?");
            antwortField.setText("");
            antwortField.setEnabled(true);
            pruefenButton.setEnabled(true);
            ergebnisLabel.setText("");
        }
    }

    private void pruefeAntwort() {
        String antwort = antwortField.getText();
        if (antwort.equalsIgnoreCase(aktuelleVokabel.getValue())) {
            ergebnisLabel.setText("Richtig!");
        } else {
            ergebnisLabel.setText("Falsch. Die richtige Antwort ist: " + aktuelleVokabel.getValue());
        }

        if (iterator.hasNext()) {
            aktuelleVokabel = iterator.next();
            frageLabel.setText("Wie lautet die englische Übersetzung von \"" + aktuelleVokabel.getKey() + "\"?");
            antwortField.setText("");
        } else {
            antwortField.setEnabled(false);
            pruefenButton.setEnabled(false);
            JOptionPane.showMessageDialog(Vokabeltrainer.this, "Die Abfrage ist beendet.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Vokabeltrainer();
            }
        });
    }
}