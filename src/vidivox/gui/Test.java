package vidivox.gui;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class Test {

    public Test() {
        initComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Test();
            }
        });
    }

    private void initComponents() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField jtf = new JTextField();
        jtf.setText("00:00");
        //add filter to document
        ((AbstractDocument) jtf.getDocument()).setDocumentFilter(new AudioEditFilter());

        frame.add(jtf);
        frame.setBounds(10, 10, 300, 300);
//        frame.pack();
        frame.setVisible(true);
    }
}

class AudioEditFilter extends DocumentFilter {
//    public static final String startTimeEditFormat = "^([0-9]{0,2}):([0-5])?([0-9])?$";
//    private String enteredText;
    
//    public AudioEditFilter(String enteredText) {
//    	super();
//    	this.enteredText = enteredText;
//    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException {
    	// an inserted string may be more than a single character i.e a copy and paste of 'aaa123d', also we iterate 
    	// from the back as super.XX implementation will put last inserted string first and so on thus 'aa123d' would be 'daa',
    	// but because we iterate from the back its 'aad' like we want
        for (int i=text.length(); i>0; i--) {
        	char c = text.charAt(i - 1);//get a single character of the string
        	System.out.println(text);
            System.out.println(c);
            
            //if its an alphabetic character or white space
            if (Character.isAlphabetic(c) || c == ' ') {
            	//allow update to take place for the given character
                super.replace(fb, offset, length, String.valueOf(c), attr);
            } else {
//            	it was not an alphabetic character or white space
                System.out.println("Not allowed");
            }
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, text, attr);

    }
}