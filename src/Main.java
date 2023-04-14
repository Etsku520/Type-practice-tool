import java.awt.Color;

import javax.imageio.plugins.tiff.ExifTIFFTagSet;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

import javax.swing.border.LineBorder;

public class Main {
    public static void main (String[] args) {
        JFrame frame = new JFrame("Type practice!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);

        TypePracticeTool tpt = new TypePracticeTool();

        JPanel panel = new JPanel();
        JLabel cpm = new JLabel("0.0");
        JLabel wpm = new JLabel("0");
        JLabel accuracy = new JLabel("0.0");

        JTextField pool = new JTextField(65);
        pool.setText(tpt.getCurrentCharacterPool());
        pool.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                setCurrentCharacterPool();
            }
            public void removeUpdate(DocumentEvent e) {
                setCurrentCharacterPool();
            }
            public void insertUpdate(DocumentEvent e) {
                setCurrentCharacterPool();
            }
        
            public void setCurrentCharacterPool() {
                tpt.setCurrentCharacterPool(pool.getText());
            }
        });

        JTextField practiceArea = new JTextField(20);
        practiceArea.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                checkCorrectness();
            }
              public void removeUpdate(DocumentEvent e) {
                checkCorrectness();
            }
              public void insertUpdate(DocumentEvent e) {
                checkCorrectness();
            }
            
            public void checkCorrectness() {
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        boolean match = tpt.checkWord(practiceArea.getText());

                        if (match) {
                            boolean moreWords = tpt.nextWord();
                            practiceArea.setText("");
                            
                            if (!moreWords) {
                                wpm.setText(String.valueOf(tpt.getWpm()));
                                accuracy.setText(String.format("%3.3f", tpt.getAccuracy()));
                                cpm.setText(String.format("%3.3f", tpt.getCpm()));
                                panel.remove(panel.getComponentCount()-1);
                                panel.remove(panel.getComponentCount()-1);
                            } else {
                                JPanel textPanel = (JPanel) panel.getComponent(panel.getComponentCount()-2);
                                JLabel previousWord = (JLabel) textPanel.getComponent(tpt.getCurrentWord()-1);
                                previousWord.setBorder(null);
                                JLabel nextWord = (JLabel) textPanel.getComponent(tpt.getCurrentWord());
                                nextWord.setBorder(new LineBorder(Color.RED, 2, true));
                            }

                            panel.revalidate();
                            panel.repaint();
                        }
                    }
                };
                SwingUtilities.invokeLater(r);
            }
        });

        JButton resetPool = new JButton("Reset pool");
        resetPool.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tpt.resetCharacterPool();
                pool.setText(tpt.getCurrentCharacterPool());
            }
        });

        JButton startButton = new JButton("Start!");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                practiceArea.setText("");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception ex) {
                    // TODO: handle exception
                    System.exit(1);
                } {

                }
                tpt.startPractice();

                JPanel textPanel = new JPanel();
                for (int i = 0; i < tpt.getCurrentText().length; i++) {
                    JLabel word = new JLabel(tpt.getCurrentText()[i]);
                    if (i == 0) {
                        word.setBorder(new LineBorder(Color.RED, 2, true));
                    }

                    textPanel.add(word);
                }

                panel.add(textPanel);
                panel.add(practiceArea);

                panel.revalidate();
                panel.repaint();
                practiceArea.requestFocus();
            }
        });

        panel.add(pool);
        panel.add(resetPool);
        panel.add(cpm);
        panel.add(wpm);
        panel.add(accuracy);
        panel.add(startButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}