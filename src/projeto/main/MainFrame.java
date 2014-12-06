package projeto.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainFrame extends JFrame{

    //elements
    private JTextArea textArea;
    private JButton betaButton, sinalphaButton;

    public MainFrame(){

        super("Q&A");

        textArea = new JTextArea(10,20);
        textArea.setText("Choose graph");
        JScrollPane sp = new JScrollPane(textArea);
        betaButton = new JButton("BETA Graph");
        betaButton.addActionListener(new EventListener());
        sinalphaButton = new JButton("Sinalpha Graph");
        sinalphaButton.addActionListener(new EventListener());
        

        Container c = getContentPane();
        c.add(sp,BorderLayout.NORTH);
        c.add(betaButton,BorderLayout.CENTER);
        c.add(sinalphaButton,BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    class EventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JButton btn = (JButton) e.getSource();

            //Action for select button
            if(btn == betaButton){
                BETA beta = new BETA();
                beta.addSourceId("a");
                beta.addSourceId("b");
                beta.addSourceId("c");
                beta.addSourceId("d");
                for (int i = 0; i < 15; i++) {
                    beta.addRecord(true, "a", Category.GENERAL);
                }
                String specialist = beta.chooseSource(Category.GENERAL);
                System.out.println(specialist);
                ArrayList<Double> chartValues = beta.getTrust(specialist, Category.GENERAL);
                Chart.drawSinalpha(chartValues);

            } else if(btn == sinalphaButton){
                Sinalpha sinalpha = new Sinalpha();
                sinalpha.addSourceId("a");
                sinalpha.addSourceId("b");
                sinalpha.addSourceId("c");
                sinalpha.addSourceId("d");
                String source="";
                for (int i = 0; i < 15; i++) {
                    source = sinalpha.chooseSource(Category.GENERAL);
                    sinalpha.addRecord(true, source, Category.GENERAL);
                }
                String specialist = sinalpha.chooseSource(Category.GENERAL);
                ArrayList<Double> chartValues = sinalpha.getTrust(specialist, Category.GENERAL);
                Chart.drawSinalpha(chartValues);
            }
        }
    }
}
