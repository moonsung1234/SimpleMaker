package window;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import window.*;

public class LineTextArea {
	private Window main;
	
	public LineTextArea(Window main) {
		this.main = main;
	}
	
	public void setNumberLine() {
		int line_count = this.main.code_text_area.getText().split("\n").length;
		
		this.main.line_text_area_color = Color.WHITE; // default color
		this.main.line_text_area.setBackground(this.main.line_text_area_color);
		this.main.line_text_area.setForeground(Color.LIGHT_GRAY); // static color
		this.main.line_text_area.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		this.main.line_text_area.setText(""); // init text
		
		for(int i=0; i<line_count; i++) {
			if(i != 0) {
				this.main.line_text_area.append("\n");
			}
			
			this.main.line_text_area.append(" " + Integer.toString(i + 1) + " ");
		}
	}
	
	public JTextArea createNumberLineTextArea() {
		this.main.line_text_area = new JTextArea();
		this.setNumberLine();
		
		return this.main.line_text_area;
	}
}
