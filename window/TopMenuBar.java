package window;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import window.*;

public class TopMenuBar {
	private Window main;
	
	public TopMenuBar(Window main) {
		this.main = main;
	}
	
	public JMenuBar createTopMenuBar() {
		this.main.top_menu_bar = new JMenuBar();
		
		String[] menu_names = { "File", "Theme", "Run" }; // static infor
		String[][] item_names = { { "load", "save" }, { "dark", "light" }, { "process", "compile" } }; // static infor
		
		for(int i=0; i<menu_names.length; i++) {
			JMenu menu = new JMenu(menu_names[i]);
			
			for(int j=0; j<item_names[i].length; j++) {
				JMenuItem menu_item = new JMenuItem(item_names[i][j]);
				
				final int static_i = i;
				final int static_j = j;
				
				menu_item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switch(item_names[static_i][static_j]) {
							case "dark" :
								main.code_text_area_color = new Color(70, 70, 70);
								main.text_color = Color.LIGHT_GRAY;

								main.code_text_area.setBackground(main.code_text_area_color);
								main.code_text_area.setForeground(main.text_color);
									
								main.code.setReservedTextColor();
								
								break;
								
							case "light" :
								main.code_text_area_color = Color.LIGHT_GRAY;
								main.text_color = Color.BLACK;

								main.code_text_area.setBackground(main.code_text_area_color);
								main.code_text_area.setForeground(main.text_color);
								
								main.code.setReservedTextColor();
								
								break;
								
							case "process" :
								main.trans.setCode(main.code_text_area.getText());
				    			main.trans.parse();
				    			main.trans.execute();
				    		
				    			break;
						}
					}
				});
				
				menu.add(menu_item);
			}
			
			this.main.top_menu_bar.add(menu);
		}
		
		return this.main.top_menu_bar;
	}
}
