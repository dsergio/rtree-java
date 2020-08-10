import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.text.DefaultCaret;

import rtree.item.ILocationItem;
import rtree.item.LocationItem2D;
import rtree.item.generic.ILocationItemGeneric;
import rtree.item.generic.LocationItemNDGeneric;
import rtree.item.generic.RDouble;
import rtree.log.ILogger;
import rtree.log.ILoggerPaint;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.Rectangle2D;
import rtree.rectangle.generic.IHyperRectangleGeneric;
import rtree.rectangle.generic.RectangleNDGeneric;
import rtree.tree.IRTree;
import rtree.tree.generic.IRTreeGeneric;

public class TestPaintDouble extends JFrame implements KeyListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2136767310271470973L;
	final PaintPanel paintPan;
	IRTreeGeneric<RDouble> tree;
	List<ILocationItemGeneric<RDouble>> points;
	private JTextField output;
	private IHyperRectangleGeneric<RDouble> searchRectangle = null;
	JButton searchButton = new JButton("Search");
	JButton showTree = new JButton("Show Tree");
	JButton showTreeStructure = new JButton("Show Tree Structure");
	boolean showTreeOn = false;
	JTextArea info = new JTextArea();
	Map<IHyperRectangleGeneric<RDouble>, List<ILocationItemGeneric<RDouble>>> searchResults;
	JList<String> list;
	double searchRange = 60;
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
	
	private double scaleFactor = 10000;
	
	public TestPaintDouble(IRTreeGeneric<RDouble> tree, boolean showTreeOn, ILogger logger, ILoggerPaint paintLogger) {
		this.showTreeOn = showTreeOn;
		this.tree = tree;
		this.logger = logger;
		this.paintLogger = paintLogger;
//		points = tree.getPoints();
		
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.setFixedCellWidth(listWidth);
		
		
		setTitle("RTREE: " + tree.getTreeName() +  " - MAX CHILREN: " + tree.getMaxChildren() + " MAX ITEMS: " + tree.getMaxItems() + " - David Sergio");
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
	
	public TestPaintDouble(IRTreeGeneric<RDouble> tree, ILogger logger, ILoggerPaint paintLogger) {
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

		/**
		 * 
		 */
		private static final long serialVersionUID = 8891010953648855035L;

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
				searchRectangle = new RectangleNDGeneric<RDouble>(2);
				
				double x1 = e.getX() - list.getWidth() - searchRange;
				double x2 = e.getX() - list.getWidth() + searchRange;
				double y1 = e.getY() - showTree.getHeight() - (int) (searchRange * 1.5);
				double y2 = e.getY() - showTree.getHeight() + (int) (searchRange / 1.5);
				
				searchRectangle.setDim1(0, new RDouble(x1 / scaleFactor));
				searchRectangle.setDim2(0, new RDouble(x2 / scaleFactor));
				searchRectangle.setDim1(1, new RDouble(y1 / scaleFactor));
				searchRectangle.setDim2(1, new RDouble(y2 / scaleFactor));
				
				searchResults = tree.search(searchRectangle);
				for (IHyperRectangleGeneric<RDouble> r : searchResults.keySet()) {
					logger.log("search results: " + r);
					for (ILocationItemGeneric<RDouble> i : searchResults.get(r)) {
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
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 951524481198357817L;
		private Color color = Color.RED;

		public PaintPanel() {
			setBackground(Color.LIGHT_GRAY);
		}

		public void setPoints(List<ILocationItemGeneric<RDouble>> pointsToAdd) {
			points = pointsToAdd;
		}

		public void addPoint(LocationItemNDGeneric<RDouble> item) {
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
				for (ILocationItemGeneric<RDouble> item : tree.getPoints()) {
					
					RDouble Rx = item.getDim(0);
					RDouble Ry = item.getDim(1);
					int x = (int) Double.parseDouble("" + (Rx.getData() * scaleFactor));
					int y = (int) Double.parseDouble("" + (Ry.getData() * scaleFactor));
					
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
						drawImage.fillOval(x, y, 4, 4);
						paintLogger.log("(" + x + ", " + y + ")", drawImage, x, y);
						paintLogger.log(item.getType(), drawImage, x, y + 20);
//						drawImage.drawString("(" + x + ", " + y + ")", x, y);
//						drawImage.drawString(item.getType(), x, y + 20);
						
					}
				}
				List<IHyperRectangleGeneric<RDouble>> rectangles = new ArrayList<IHyperRectangleGeneric<RDouble>>();
				rectangles = tree.getRectangles();
				for (IHyperRectangleGeneric<RDouble> r : rectangles) {
					
					RDouble Rx = r.getDim1(0);
					RDouble Ry = r.getDim1(1);
					RDouble Rx2 = r.getDim2(0);
					RDouble Ry2 = r.getDim2(1);
					int x = (int) Double.parseDouble("" + (Rx.getData() * scaleFactor));
					int y = (int) Double.parseDouble("" + (Ry.getData() * scaleFactor));
					
					int x2 = (int) Double.parseDouble("" + (Rx2.getData() * scaleFactor));
					int y2 = (int) Double.parseDouble("" + (Ry2.getData() * scaleFactor));
					
					int width = Math.abs(x2 - x);
					int height = Math.abs(y2 - y);
					
					drawImage = (Graphics2D) g;
					if (color != null) {

						
								
						if (r.getLevel() == 1) {
							color = Color.RED;
						} else if (r.getLevel() == 2) {
							color = Color.ORANGE;
						} else if (r.getLevel() == 3) {
							color = Color.YELLOW;
						} else if (r.getLevel() == 4) {
							color = Color.GREEN;
						} else if (r.getLevel() == 5) {
							color = Color.BLUE;
						} else if (r.getLevel() == 6) {
							color = Color.decode("#4B0082");
						} else if (r.getLevel() == 7) {
							color = Color.decode("#9400D3");
						} else {
							color = Color.BLACK;
						}

						int extra = r.getLevel() + 1;
						x += extra;
						y += extra;
						width -= extra;
						height -= extra;

						drawImage.setStroke(new BasicStroke(1.3f));

						drawImage.setColor(color);
						drawImage.drawRect(x, y, width, height);
						paintLogger.log("area: " + r.getSpace(), drawImage, x, y + height - 20);
//						drawImage.drawString("area: " + r.getArea(), x, y + height - 20);

					}
				}
			}
			

			if (searchRectangle != null) {
				
				drawImage = (Graphics2D) g;
				color = Color.BLACK;
				
				List<ILocationItemGeneric<RDouble>> allItems = new ArrayList<ILocationItemGeneric<RDouble>>();
				for (IHyperRectangleGeneric<RDouble> r : searchResults.keySet()) {
					
					if (r.getDim1(0) != searchRectangle.getDim1(0) && 
							r.getDim2(0) != searchRectangle.getDim2(0) && 
							r.getDim1(1) != searchRectangle.getDim1(1) && 
							r.getDim2(1) != searchRectangle.getDim2(1)
							) {
						Stroke existing = drawImage.getStroke();
						Stroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
						drawImage.setStroke(dashed);
						
						logger.log("Rectangle traverse: " + r + ", level: " + r.getLevel());
						
						int offset = 0;
						if (r.getLevel() == 1) {
							color = Color.RED;
						} else if (r.getLevel() == 2) {
							color = Color.ORANGE;
							offset = 3;
						} else if (r.getLevel() == 3) {
							color = Color.YELLOW;
							offset = 5;
						} else if (r.getLevel() == 4) {
							color = Color.GREEN;
							offset = 7;
						} else if (r.getLevel() == 5) {
							color = Color.BLUE;
							offset = 9;
						} else if (r.getLevel() == 6) {
							color = Color.decode("#4B0082");
							offset = 11;
						} else if (r.getLevel() == 7) {
							color = Color.decode("#9400D3");
							offset = 13;
						} else {
							color = Color.BLACK;
							offset = 15;
						}
						drawImage.setColor(color);
						
						RDouble Rx = r.getDim1(0);
						RDouble Ry = r.getDim1(1);
						RDouble Rx2 = r.getDim2(0);
						RDouble Ry2 = r.getDim2(1);
						int x = (int) Double.parseDouble("" + (Rx.getData() * scaleFactor));
						int y = (int) Double.parseDouble("" + (Ry.getData() * scaleFactor));
						
						int x2 = (int) Double.parseDouble("" + (Rx2.getData() * scaleFactor));
						int y2 = (int) Double.parseDouble("" + (Ry2.getData() * scaleFactor));
						
						int width = Math.abs(x2 - x);
						int height = Math.abs(y2 - y);
						
						drawImage.drawRect(x + offset, y, width - offset, height);
						drawImage.setStroke(existing);
					}
					
					for (ILocationItemGeneric<RDouble> i : searchResults.get(r)) {
						
						RDouble RItemX = i.getDim(0);
						RDouble RItemY = i.getDim(1);
						int itemX = (int) Double.parseDouble("" + (RItemX.getData() * scaleFactor));
						int itemY = (int) Double.parseDouble("" + (RItemY.getData() * scaleFactor));
						
						if (color != null) {
							drawImage.setColor(Color.decode("#ff6680"));
							drawImage.fillOval(itemX, itemY, 5, 5);
							drawImage.drawString("(" + itemX + ", " + itemY + ")", itemX, itemY);
							drawImage.setColor(Color.BLACK);
							
							drawImage.setFont(new Font("default", Font.BOLD, 16));
							drawImage.drawString(i.getType(), itemX, itemY + 20);
							drawImage.setFont(new Font("default", Font.PLAIN, 12));
							
						}
						allItems.add(i);
					}
				}
				if (allItems.isEmpty()) {
					String[] data = {"No Results"};
					list.setListData(data);
				} else {
					List<String> dataStrings = new ArrayList<String>();
					for (ILocationItemGeneric<RDouble> i : allItems) {
						dataStrings.add(i.toString());
					}
					list.setListData(dataStrings.toArray(new String[0]));
				}
				
				Stroke existing = drawImage.getStroke();
				Stroke dashed = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				drawImage.setStroke(dashed);
				drawImage.setColor(Color.decode("#ff6680"));
				
				RDouble Rx = searchRectangle.getDim1(0);
				RDouble Ry = searchRectangle.getDim1(1);
				RDouble Rx2 = searchRectangle.getDim2(0);
				RDouble Ry2 = searchRectangle.getDim2(1);
				int x = (int) Double.parseDouble("" + (Rx.getData() * scaleFactor));
				int y = (int) Double.parseDouble("" + (Ry.getData() * scaleFactor));
				
				int x2 = (int) Double.parseDouble("" + (Rx2.getData() * scaleFactor));
				int y2 = (int) Double.parseDouble("" + (Ry2.getData() * scaleFactor));
				
				int width = Math.abs(x2 - x);
				int height = Math.abs(y2 - y);
				
				drawImage.drawRect(x, y, width, height);
				drawImage.setStroke(existing);
				
				
			}
//			searchRectangle = null;
			color = Color.BLACK;
		}

		public void updateGraphics(int x, int y) throws IOException {
			color = Color.BLACK;

			LocationItemNDGeneric<RDouble> item = new LocationItemNDGeneric<RDouble>(2);
			
			RDouble Rx = new RDouble((double) x / scaleFactor);
			RDouble Ry = new RDouble((double) y / scaleFactor);
			
			item.setDim(0, Rx);
			item.setDim(1, Ry);
			
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
				double x = Double.parseDouble(output.getText().split("\\s+")[1]);
				double y = Double.parseDouble(output.getText().split("\\s+")[2]);
				double range = Double.parseDouble(output.getText().split("\\s+")[3]);
				
				searchRange = range;

				logger.log("you want to search for x: " + x + " y: " + y + " in range: " + range);
				String[] data = {"Searching..."};
				list.setListData(data);
				searchRectangle = new RectangleNDGeneric<RDouble>(2);
				
				
				searchRectangle.setDim1(0, new RDouble(x));
				searchRectangle.setDim2(0, new RDouble(x + range));
				searchRectangle.setDim1(1, new RDouble(y));
				searchRectangle.setDim2(1, new RDouble(y + range));
				
				searchResults = tree.search(searchRectangle);
				for (IHyperRectangleGeneric<RDouble> r : searchResults.keySet()) {
					logger.log("search results: " + r);
					for (ILocationItemGeneric<RDouble> i : searchResults.get(r)) {
						logger.log("..." + i);
					}
					
				}

				logger.log("searchRectangle: " + searchRectangle);
				repaint();

			} else if (output.getText().trim().matches("delete\\s+[0-9]+\\s+[0-9]+\\s+\"[a-zA-Z][a-zA-Z0-9\\s+\\-,]+\"\\s*")) {

				double x = Double.parseDouble(output.getText().split("\\s+")[1]);
				double y = Double.parseDouble(output.getText().split("\\s+")[2]);
				String type = output.getText().split("\"")[1].replaceAll("\"", "");

				logger.log("you want to delete x: " + x + " y: " + y + " type: " + type);

				ILocationItemGeneric<RDouble> toDelete = new LocationItemNDGeneric<RDouble>(2);
				toDelete.setDim(0, new RDouble(x));
				toDelete.setDim(1, new RDouble(y));
				toDelete.setType(type);
				tree.delete(toDelete);

				repaint();
			} else if (output.getText().trim().equals("print")) {
				tree.printTree();
			} else if (output.getText().trim().matches("set\\s+range\\s+[0-9]+\\s*")) {

				
				searchRange = Double.parseDouble(output.getText().split("\\s+")[2]);
				
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
