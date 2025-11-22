package bunlisugo.client.view;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class Button {
	private String[] TrashImage;

	 
	public JButton getbutton(String text, AreaCheck ac, String ImagePath) {
		//생성 
		JButton btn = new JButton(text);
		btn.setBounds(200+(int) (800*(Math.random())), 200+(int) (300*(Math.random())), 85, 85);

		final Point initialClick = new Point();
		
		//움직임 드래그앤드롭
		btn.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point p = SwingUtilities.convertPoint(btn, e.getPoint(), btn.getParent());
				btn.setLocation(p);
				
			}
		});
		
		btn.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseReleased(e);
				ac.Check(btn);
			}
			
		});
		
		//이미지 설정
		ImageIcon icon = new ImageIcon(ImagePath);
		if(icon.getImageLoadStatus()==MediaTracker.COMPLETE	) {
			Image TrashImage = icon.getImage();
			btn.setIcon(new ImageIcon(TrashImage));
		}else{btn.setText(text);
		
	
	}
		return btn;

	}}
