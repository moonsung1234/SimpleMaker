package window;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import window.*;

public class CodeTextArea {
	private Window main;
	
	private static class TabDocument extends DefaultStyledDocument {
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        	str = str.replaceAll("\t", "    ");
            super.insertString(offset, str, a);
        }
    }
	
	public DefaultStyledDocument default_styled_document;
	private boolean is_run;
	
	public CodeTextArea(Window main) {
		this.main = main;
		this.is_run = true;
	}
	
	public void setEvent() {
		this.main.code_text_area.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				int font_size = main.code_text_area.getFont().getSize();
				
				switch(e.getKeyCode()) {
					case 122 :
						main.code_text_area.setFont(new Font("Consolas Italic", Font.PLAIN, font_size - 5));
						main.line_text_area.setFont(new Font("Arial", Font.PLAIN, font_size - 5));
						
						break;
						
					case 123 :
						main.code_text_area.setFont(new Font("Consolas Italic", Font.PLAIN, font_size + 5));
						main.line_text_area.setFont(new Font("Arial", Font.PLAIN, font_size + 5));
						
						break;
				}
			}
			
			@Override
            public void keyReleased(KeyEvent e) {}
            
			@Override
            public void keyTyped(KeyEvent e) {}     
		});
		this.main.code_text_area.getDocument().addDocumentListener(new DocumentListener() {
	        @Override
	        public void removeUpdate(DocumentEvent e) {
	        	main.line.setNumberLine();
	        	setReservedTextColor();
	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	main.line.setNumberLine();
	    	    setReservedTextColor();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent e) {}
	    });
	}
	
	public void addColorText(String msg, Color color) {
        StyleContext style_context = StyleContext.getDefaultStyleContext();
        AttributeSet aset = style_context.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        int text_length = this.main.code_text_area.getDocument().getLength();
        
        aset = style_context.addAttribute(aset, StyleConstants.FontFamily, "Consolas Italic");
        aset = style_context.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        this.main.code_text_area.setCaretPosition(text_length);
        this.main.code_text_area.setCharacterAttributes(aset, false);
        this.main.code_text_area.replaceSelection(msg);
    }
	
	public void setReservedTextColor() {
		String[] reserved_words = { 
				"Plus",
				"Minus",
				"Print"
		};
		Color[] reserved_words_colors = {
				new Color(150,0,0),
				new Color(100, 20, 100),
				this.main.text_color.equals(Color.BLACK)? Color.WHITE : Color.BLACK 
		};
		String[] code_texts = this.main.code_text_area.getText().replaceAll("\n", "").split("");
		
		this.is_run = true;
		
    	SwingUtilities.invokeLater(new Runnable() {
    		@Override
    		public void run() {
    			if(is_run) {
    				for(int i=0; i<code_texts.length; i++) {
    					SimpleAttributeSet asset = new SimpleAttributeSet();
			            StyleConstants.setForeground(asset, main.text_color); 
			            		
			            default_styled_document.setCharacterAttributes(i, 1, asset, true);
    				}
    				
    				for(int i=0; i<code_texts.length; i++) {
    					for(String reserved_word : reserved_words) {
    						int index = String.join("", code_texts).indexOf(reserved_word, i);
    						
    						if(index != -1) {
    							SimpleAttributeSet asset = new SimpleAttributeSet();
					            StyleConstants.setForeground(asset, reserved_words_colors[Arrays.asList(reserved_words).indexOf(reserved_word)]); // static color
					            
					            default_styled_document.setCharacterAttributes(index, reserved_word.split("").length, asset, true);
    						}
    					}
    				}   			
    				
    				is_run = false;
    			}
    		}
		});
	}
	
	public JTextPane createCodeTextPane() {
		this.default_styled_document = new TabDocument();
		this.main.code_text_area = new JTextPane(this.default_styled_document);
		this.main.code_text_area_color = Color.LIGHT_GRAY; // default color
		this.main.text_color = Color.BLACK; // default color
		this.main.code_text_area.setBackground(this.main.code_text_area_color);
		this.main.code_text_area.setForeground(this.main.text_color); 
		this.main.code_text_area.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
		this.addColorText("Hello ", Color.BLACK);
		this.addColorText("SimpleMaker", Color.RED);
		this.addColorText("!", Color.BLACK);
		
		return this.main.code_text_area;
	}
}
