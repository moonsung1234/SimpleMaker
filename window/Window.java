package window;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import window.*;
import transaction.*;

public class Window extends JFrame {
	public int width;
	public int height;
	public Color background_color;
	
	public DefaultStyledDocument default_styled_document;
	public JMenuBar top_menu_bar;
	public JTextPane code_text_area;
	public JTextArea line_text_area;
	
	public Color code_text_area_color;
	public Color line_text_area_color;
	
	public Color text_color;
	
	public TopMenuBar menu;
	public CodeTextArea code;
	public LineTextArea line;
	public Transaction trans;
	
	public Window(int width, int height, Color background_color) {
		this.width = width;
		this.height = height;
		this.background_color = background_color;
		
		this.setTitle("Simple Maker!");
		this.setSize(this.width, this.height);
		this.getContentPane().setBackground(this.background_color);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void setComponents() {
		this.menu = new TopMenuBar(this);
		this.code = new CodeTextArea(this);
		this.line = new LineTextArea(this);
		this.trans = new Transaction();
		
		this.setJMenuBar(this.menu.createTopMenuBar());
		this.add(this.code.createCodeTextPane(), BorderLayout.CENTER);
		this.add(this.line.createNumberLineTextArea(), BorderLayout.WEST);
		
		this.code.setEvent();
	}
	
	public static void main(String[] args) {
		Window window = new Window(800, 600, Color.GRAY);
		
		window.setComponents();
	}
}
