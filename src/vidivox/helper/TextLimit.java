package vidivox.helper;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class will limit the JTextField input to a specified maximum length.
 * Reference: http://www.java2s.com/Tutorial/Java/0240__Swing/LimitJTextFieldinputtoamaximumlength.htm
 * 
 * Author: Jacky Mai - jmai871
 * Partner: Helen Zhao - hzha587
 */
public class TextLimit extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int limit;
	
	public TextLimit(int limit) {
		super();
		this.limit = limit;
	}
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if(str == null) {
			return;
		}
		
		if((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
