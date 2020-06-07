import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;

import cloudrtree.RTree;
import cloudrtree.ILogger;
import cloudrtree.ILoggerPaint;
import cloudrtree.LocationItem;
import cloudrtree.Rectangle;

public class TestPaint extends JFrame implements KeyListener, ActionListener {

	final PaintPanel paintPan;
	RTree tree;
	List<LocationItem> points;
	private JTextField output;
	private Rectangle searchRectangle = null;
	JButton searchButton = new JButton("Search");
	JButton showTree = new JButton("Show Tree");
	JButton showTreeStructure = new JButton("Show Tree Structure");
	boolean showTreeOn = false;
	JTextArea info = new JTextArea();
	Map<Rectangle, List<LocationItem>> searchResults;
	JList list;
	int searchRange = 60;
	private ILogger logger;
	private ILoggerPaint paintLogger;
	
	private int paintInitialWidth = 800;
	private int paintInitialHeight = 650;
	private int listWidth = 150;
	private int padding = 100;
	
	private int minX = 0;
	private int minY = 0;
	private int maxX = paintInitialWidth;
	private int maxY = paintInitialHeight;
	
	public TestPaint(RTree tree, boolean showTreeOn, ILogger logger, ILoggerPaint paintLogger) {
		this.showTreeOn = showTreeOn;
		this.tree = tree;
		this.logger = logger;
		this.paintLogger = paintLogger;
//		points = tree.getPoints();
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.setFixedCellWidth(listWidth);
		
		
		setTitle("RTREE: " + tree.getName() +  " - MAX CHILREN: " + tree.getMaxChildrenVar() + " MAX ITEMS: " + tree.getMaxItems() + " - David Sergio");
		setSize(paintInitialWidth + list.getWidth() + info.getWidth() + padding, paintInitialHeight + padding);
		
//		FlowLayout layout = new FlowLayout();
		
		setLayout(new BorderLayout());

		paintPan = new PaintPanel();
		paintPan.setSize(paintInitialWidth + padding, paintInitialHeight + padding);
		
		if (showTreeOn) {
			showTree.setText("Hide Tree");
		} else {
			showTree.setText("Show Tree");
		}
		add(showTree, BorderLayout.PAGE_START);
		showTree.addActionListener(this);
		
		
		add(paintPan, BorderLayout.CENTER);
		
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		add(list, BorderLayout.LINE_START);
		String[] data = {""};
		list.setListData(data);
		info.setText(
					"  search [x] [y] [range]  " + "\n" + 
					"  print  " + "\n" +
					"  set range [range]  " + "\n" +
					"  delete [x] [y] [type]  " + "\n"
					
		);
		
		info.setEditable(false);
		add(info, BorderLayout.LINE_END);
		
		output = new JTextField("search 200 200 100");
		output.setColumns(100);
		DefaultCaret caret = (DefaultCaret) output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // JTextArea always set focus on the last message appended.
		add(output, BorderLayout.PAGE_END);

		setVisible(true);
		output.addKeyListener(this); // Adds the specified key listener to receive key events from this component.
		

		Component mouseClick = new MyComponent();
		addMouseListener((MouseListener) mouseClick);
		

		setVisible(true);

	}
	
	public TestPaint(RTree tree, ILogger logger, ILoggerPaint paintLogger) {
		this(tree, false, logger, paintLogger);
	}
	
//	public int convertScaleX(int input) {
//		if (maxX == minX) {
//			return input;
//		}
//		return (int) (((double) (input - minX) / (double) ( maxX - minX)) * (double) paintPan.getWidth());
//	}
//	public int convertScaleXWidth(int width) {
//		if (maxX == minX) {
//			return width;
//		}
//		return (int) (((double) (width) / (double) ( maxX - minX)) * (double) paintPan.getWidth());
//	}
//	public int reverseConvertScaleX(int input) {
//		if (maxX == minX) {
//			return input;
//		}
//		return minX + (int) ((double) (maxX - minX) * ((double) input / (double) paintPan.getWidth()));
//	}
//	public int convertScaleY(int input) {
//		if (maxY == minY) {
//			return input;
//		}
//		return (int) (((double) (input - minY) / (double) ( maxY - minY)) * (double) paintPan.getHeight());
//	}
//	public int convertScaleYHeight(int height) {
//		if (maxY == minY) {
//			return height;
//		}
//		return (int) (((double) (height) / (double) ( maxY - minY)) * (double) paintPan.getHeight());
//	}
//	public int reverseConvertScaleY(int input) {
//		if (maxY == minY) {
//			return input;
//		}
//		return minY + (int) ((double) (maxY - minY) * ((double) input / (double) paintPan.getHeight()));
//	}


	public class MyComponent extends JComponent implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			if (e.getButton() == MouseEvent.BUTTON3) {
				
				
				String[] data = {"Searching..."};
				list.setListData(data);
				list.repaint();
				paintPan.repaint();
				repaint();
				searchRectangle = new Rectangle(
						e.getX() - list.getWidth() - searchRange, 
						e.getX() - list.getWidth() + searchRange, 
						e.getY() - showTree.getHeight() - (int) (searchRange * 1.5), 
						e.getY() - showTree.getHeight() + (int) (searchRange / 1.5));
				
				searchResults = tree.search(searchRectangle);
				for (Rectangle r : searchResults.keySet()) {
					logger.log("search results: " + r);
					for (LocationItem i : searchResults.get(r)) {
						logger.log("..." + i);
					}
					
				}

				logger.log("searchRectangle: " + searchRectangle);
				paintPan.repaint();
				
			} else {
				
				int x = e.getX() - 10 - list.getWidth();
				int y = e.getY() - 30 - showTree.getHeight();
				
				if (JOptionPane.showConfirmDialog(null, "Really insert " + x + ", " + y + "?", "New Item",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					try {
						paintPan.updateGraphics(x, y);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		

		

	}

	class PaintPanel extends JPanel {

		private int x, y;
		private Color color = Color.RED;
		
		

		public PaintPanel() {
			setBackground(Color.LIGHT_GRAY);
		}

		public void setPoints(List<LocationItem> pointsToAdd) {
			points = pointsToAdd;
		}

		public void addPoint(LocationItem item) {
			points.add(item);
		}
		
		

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			color = Color.BLACK;
			
			Graphics2D drawImage = (Graphics2D) g;
			
			
			drawImage.drawLine(0, 10, paintPan.getWidth() - 50, 10);
			drawImage.drawLine(10, 0, 10, paintPan.getHeight() - 50);
			for (int i = 50; i < paintPan.getWidth() - 50; i += 50) {
				drawImage.drawLine(i, 5, i, 15);
				drawImage.drawString("" + i, i, 20);
				
				
				drawImage.setColor(Color.GRAY);
				Stroke existing = drawImage.getStroke();
				Stroke dashed = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				drawImage.setStroke(dashed);
				drawImage.drawLine(i, 25, i, paintPan.getHeight() - 50);
				drawImage.setColor(Color.BLACK);
				drawImage.setStroke(existing);
			}
			for (int i = 50; i < paintPan.getHeight() - 50; i += 50) {
				drawImage.drawLine(5, i, 15, i);
				drawImage.drawString("" + i, 20, i);
				
				drawImage.setColor(Color.GRAY);
				Stroke dashed = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				drawImage.setStroke(dashed);
				drawImage.drawLine(25, i, paintPan.getWidth() - 50, i);
				drawImage.setColor(Color.BLACK);
			}
			
			if (showTreeOn) {
				for (LocationItem item : tree.getPoints()) {
					int x = item.getX();
					int y = item.getY();
					
					if (x > maxX) {
						maxX = x;
					}
					if (x < minX) {
						minX = x;
					}
					if (y > maxY) {
						maxY = y;
					}
					if (y < minY) {
						minY = y;
					}
					drawImage = (Graphics2D) g;
					if (color != null) {
						drawImage.setColor(color);
						drawImage.fillOval(x, y, 5, 5);
						paintLogger.log("(" + x + ", " + y + ")", drawImage, x, y);
						paintLogger.log(item.getType(), drawImage, x, y + 20);
//						drawImage.drawString("(" + x + ", " + y + ")", x, y);
//						drawImage.drawString(item.getType(), x, y + 20);
						
					}
				}
				List<Rectangle> rectangles = new ArrayList<Rectangle>();
				rectangles = tree.getRectangles();
				for (Rectangle r : rectangles) {
					int x = r.getX1();
					int y = r.getY1();
					int width = r.width();
					int height = r.height();
					drawImage = (Graphics2D) g;
					if (color != null) {

						if (r.getLevel() == 0) {
							color = Color.RED;
						} else if (r.getLevel() == 1) {
							color = Color.ORANGE;
						} else if (r.getLevel() == 2) {
							color = Color.YELLOW;
						} else if (r.getLevel() == 3) {
							color = Color.GREEN;
						} else if (r.getLevel() == 4) {
							color = Color.CYAN;
						} else if (r.getLevel() == 5) {
							color = Color.BLUE;
						} else {
							color = Color.BLACK;
						}

						int extra = r.getLevel() + 1;
						x += extra;
						y += extra;
						width -= extra;
						height -= extra;

						drawImage.setStroke(new BasicStroke(2f));

						drawImage.setColor(color);
						drawImage.drawRect(x, y, width, height);
						paintLogger.log("area: " + r.getArea(), drawImage, x, y + height - 20);
//						drawImage.drawString("area: " + r.getArea(), x, y + height - 20);

					}
				}
			}
			

			if (searchRectangle != null) {
				
				drawImage = (Graphics2D) g;
				color = Color.BLACK;
				
				List<LocationItem> allItems = new ArrayList<LocationItem>();
				for (Rectangle r : searchResults.keySet()) {
					
					if (r.getX1() != searchRectangle.getX1() && 
							r.getX2() != searchRectangle.getX2() && 
							r.getY1() != searchRectangle.getY1() && 
							r.getY2() != searchRectangle.getY2()
							) {
						Stroke existing = drawImage.getStroke();
						Stroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
						drawImage.setStroke(dashed);
						
						logger.log("Rectangle traverse: " + r + ", level: " + r.getLevel());
						
						int offset = 0;
						if (r.getLevel() == 0) {
							color = Color.RED;
						} else if (r.getLevel() == 1) {
							color = Color.ORANGE;
							offset = 3;
						} else if (r.getLevel() == 2) {
							color = Color.YELLOW;
							offset = 5;
						} else if (r.getLevel() == 3) {
							color = Color.GREEN;
							offset = 7;
						} else if (r.getLevel() == 4) {
							color = Color.CYAN;
							offset = 9;
						} else if (r.getLevel() == 5) {
							color = Color.BLUE;
							offset = 11;
						} else {
							color = Color.BLACK;
							offset = 13;
						}
						drawImage.setColor(color);
						drawImage.drawRect(r.getX1() + offset, r.getY1(), r.width() - offset,
								r.height());
						drawImage.setStroke(existing);
					}
					
					for (LocationItem i : searchResults.get(r)) {
						
						if (color != null) {
							drawImage.setColor(Color.RED);
							drawImage.fillOval(i.getX(), i.getY(), 5, 5);
							drawImage.drawString("(" + i.getX() + ", " + i.getY() + ")", i.getX(), i.getY());
							drawImage.setColor(Color.BLACK);
							
							drawImage.setFont(new Font("default", Font.BOLD, 16));
							drawImage.drawString(i.getType(), i.getX(), i.getY() + 20);
							drawImage.setFont(new Font("default", Font.PLAIN, 12));
							
							drawImage.setColor(Color.RED);
						}
						allItems.add(i);
					}
				}
				if (allItems.isEmpty()) {
					String[] data = {"No Results"};
					list.setListData(data);
				} else {
					list.setListData(allItems.toArray());
				}
				
				Stroke existing = drawImage.getStroke();
				Stroke dashed = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				drawImage.setStroke(dashed);
				drawImage.setColor(Color.RED);
				drawImage.drawRect(searchRectangle.getX1(), searchRectangle.getY1(), searchRectangle.width(),
						searchRectangle.height());
				drawImage.setStroke(existing);
				
				
			}
//			searchRectangle = null;
			color = Color.RED;
		}

		public void updateGraphics(int x, int y) throws IOException {
			color = Color.RED;

			LocationItem item = new LocationItem(x, y, "point");
			// addPoint(item);
			tree.insert(item);
			tree.updateRoot();
			repaint();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		// logger.log(output.getText());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode(); // study KeyEvent class API
		if (keyCode == KeyEvent.VK_ENTER) {
			logger.log(output.getText());
			if (output.getText().trim().matches("search\\s+[0-9]+\\s+[0-9]+\\s+[0-9]+\\s*")) {
				int x = Integer.parseInt(output.getText().split("\\s+")[1]);
				int y = Integer.parseInt(output.getText().split("\\s+")[2]);
				int range = Integer.parseInt(output.getText().split("\\s+")[3]);
				
				searchRange = range;

				logger.log("you want to search for x: " + x + " y: " + y + " in range: " + range);
				String[] data = {"Searching..."};
				list.setListData(data);
				searchRectangle = new Rectangle(x, x + range, y, y + range);
				searchResults = tree.search(searchRectangle);
				for (Rectangle r : searchResults.keySet()) {
					logger.log("search results: " + r);
					for (LocationItem i : searchResults.get(r)) {
						logger.log("..." + i);
					}
					
				}

				logger.log("searchRectangle: " + searchRectangle);
				repaint();

			} else if (output.getText().trim().matches("delete\\s+[0-9]+\\s+[0-9]+\\s+\"[a-zA-Z][a-zA-Z0-9\\s+\\-,]+\"\\s*")) {

				int x = Integer.parseInt(output.getText().split("\\s+")[1]);
				int y = Integer.parseInt(output.getText().split("\\s+")[2]);
				String type = output.getText().split("\"")[1].replaceAll("\"", "");

				logger.log("you want to delete x: " + x + " y: " + y + " type: " + type);

				LocationItem toDelete = new LocationItem(x, y, type);
				tree.delete(toDelete);

				repaint();
			} else if (output.getText().trim().equals("print")) {
				tree.printTree();
			} else if (output.getText().trim().matches("set\\s+range\\s+[0-9]+\\s*")) {

				
				searchRange = Integer.parseInt(output.getText().split("\\s+")[2]);
				
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (showTreeOn) {
			showTreeOn = false;
			showTree.setText("Show Tree");
		} else {
			showTreeOn = true;
			showTree.setText("Hide Tree");
		}
		repaint();
	}
}
