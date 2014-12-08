package projeto.gui;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Printer {
	public static JTextPane output;
	public static JScrollPane scroll;
	public static StringBuilder text = new StringBuilder();
	public static void println(String s)
	{
		if(output!=null)
		{
			text.append(s);
			text.append("\n");
			output.setText(text.toString());
//			JScrollBar vertical = scroll.getVerticalScrollBar();
//			vertical.setValue( vertical.getMaximum() );
		}else
			System.out.println(s);
	}
}
