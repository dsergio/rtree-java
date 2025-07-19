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
import java.text.DecimalFormat;
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
import rtree.item.LocationItem;
import rtree.item.RDouble;
import rtree.log.ILogger;
import rtree.log.ILoggerPaint;
import rtree.rectangle.IHyperRectangle;
import rtree.rectangle.HyperRectangle;
import rtree.tree.IRTree;

public class TestPaintGeo extends JFrame implements KeyListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2136767310271470973L;
	private final PaintPanel paintPan;
	
	private IRTree<RDouble> tree;
	private List<ILocationItem<RDouble>> points;
	private Map<IHyperRectangle<RDouble>, List<ILocationItem<RDouble>>> searchResults;
	private IHyperRectangle<RDouble> searchRectangle = null;
	
	private JTextField output;
	private JTextArea info = new JTextArea();
	
	private JButton showTree = new JButton("Show Tree");
	private boolean showTreeOn = false;
	
	
	private JList<String> list;
	private double searchRange = 20;
	private ILogger logger;
	private ILoggerPaint paintLogger;
	
	private int paintInitialWidth = 1000;
	private int paintInitialHeight = 800;
	private int listWidth = 250;
	private int padding = 0;
	private int gridOffset = 10;
	private int mapOffset = 100;
	
	double mapRelativeSize = 0.7;
	
	private Double latitudeMin = null;
	private Double latitudeMax = null;
	private Double longitudeMin = null;
	private Double longitudeMax = null;
	
	private Double longitudeWidth = null;
	private Double latitudeHeight = null;
	
	public TestPaintGeo(IRTree<RDouble> tree, boolean showTreeOn, ILogger logger, ILoggerPaint paintLogger,
			Double longitudeMin, Double longitudeMax, Double latitudeMin, Double latitudeMax) {
		
		this.longitudeMin = longitudeMin;
		this.longitudeMax = longitudeMax;
		this.latitudeMin = latitudeMin;
		this.latitudeMax = latitudeMax;
		
		this.longitudeWidth = longitudeMax - longitudeMin;
		this.latitudeHeight = latitudeMax - latitudeMin;
		
		System.out.println("longitudeMin: " + longitudeMin + " longitudeMax: " + longitudeMax);
		System.out.println("latitudeMin: " + latitudeMin + " latitudeMax: " + latitudeMax);
		System.out.println("longitudeWidth: " + longitudeWidth + " latitudeHeight: " + latitudeHeight);
		
		this.showTreeOn = showTreeOn;
		this.tree = tree;
		this.logger = logger;
		this.paintLogger = paintLogger;
		
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.setFixedCellWidth(listWidth);
		
		
		setTitle("RTREE: " + tree.getTreeName() +  " - MAX CHILREN: " + tree.getMaxChildren() + " MAX ITEMS: " + tree.getMaxItems() + " - David Sergio");
		setSize(paintInitialWidth + list.getWidth() + info.getWidth() + padding, paintInitialHeight + padding);
		
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
	
	public TestPaintGeo(IRTree<RDouble> tree, ILogger logger, ILoggerPaint paintLogger,
			Double longitudeMin, Double longitudeMax, Double latitudeMin, Double latitudeMax) {
		this(tree, false, logger, paintLogger, longitudeMin, longitudeMax, latitudeMin, latitudeMax);
	}

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
				searchRectangle = new HyperRectangle<RDouble>(2);
				
				double x1 = e.getX() - list.getWidth() - searchRange;
				double x2 = e.getX() - list.getWidth() + searchRange;
				double y1 = e.getY() - showTree.getHeight() * 2 + searchRange;
				double y2 = e.getY() - showTree.getHeight() * 2 - searchRange;
				
//				x1 -= 50;
//				x2 -= 50;
				
//				y1 += 60;
//				y2 += 60;
				
				double longitudeSearch1 = (x1 / (double)(paintPan.getWidth())) * longitudeWidth + longitudeMin;
				double longitudeSearch2 = (x2 / (double)(paintPan.getWidth())) * longitudeWidth + longitudeMin;
				double latitudeSearch1 = ((paintPan.getHeight() - y1) / (double)(paintPan.getHeight())) * latitudeHeight + latitudeMin;
				double latitudeSearch2 = ((paintPan.getHeight() - y2) / (double)(paintPan.getHeight())) * latitudeHeight + latitudeMin;
				
				System.out.println("\n\n\n Searching ");
				System.out.println("e.getX(): " + e.getX());
				System.out.println("e.getY(): " + e.getY());
				System.out.println("(paintPan.getHeight() - y1): " + (paintPan.getHeight() - y1));
				System.out.println("(paintPan.getHeight() - y2): " + (paintPan.getHeight() - y2));
				System.out.println("paintPan.getHeight(): " + (paintPan.getHeight()));
				System.out.println("latitudeHeight:" + latitudeHeight);
				System.out.println("latitudeMin:" + latitudeMin);
				System.out.println("latitudeMax:" + latitudeMax);
				System.out.println("x1: " + x1);
				System.out.println("x2: " + x2);
				System.out.println("y1: " + y1);
				System.out.println("y2: " + y2);
				System.out.println("latitude1: " + latitudeSearch1);
				System.out.println("latitude2: " + latitudeSearch2);
				System.out.println("longitudeSearch1: " + longitudeSearch1);
				System.out.println("longitudeSearch2: " + longitudeSearch2);
				System.out.println("\n\n\n");
				
				searchRectangle.setDim1(0, new RDouble(longitudeSearch1));
				searchRectangle.setDim2(0, new RDouble(longitudeSearch2));
				searchRectangle.setDim1(1, new RDouble(latitudeSearch1));
				searchRectangle.setDim2(1, new RDouble(latitudeSearch2));
				
				searchResults = tree.search(searchRectangle);
				for (IHyperRectangle<RDouble> r : searchResults.keySet()) {
					logger.log("search results: " + r);
					for (ILocationItem<RDouble> i : searchResults.get(r)) {
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

		public void setPoints(List<ILocationItem<RDouble>> pointsToAdd) {
			points = pointsToAdd;
		}

		public void addPoint(LocationItem<RDouble> item) {
			points.add(item);
		}
		
		

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			color = Color.BLACK;
			
			Graphics2D drawImage = (Graphics2D) g;
			
			
			drawImage.drawLine(gridOffset, gridOffset, paintPan.getWidth() - gridOffset, gridOffset);
			drawImage.drawLine(gridOffset, gridOffset, gridOffset, paintPan.getHeight() - gridOffset);
			
			for (int i = 50; i < paintPan.getWidth() - 50; i += 50) {
				
				drawImage.drawLine(i, gridOffset - 5, i, gridOffset + 5);
				drawImage.drawString("" + i, i, gridOffset + 10);
				
				
				drawImage.setColor(Color.GRAY);
				Stroke existing = drawImage.getStroke();
				Stroke dashed = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				
				drawImage.setStroke(dashed);
				drawImage.drawLine(i, gridOffset * 2, i, paintPan.getHeight() - gridOffset);
				drawImage.setColor(Color.BLACK);
				drawImage.setStroke(existing);
			}
			for (int i = 50; i < paintPan.getHeight() - 50; i += 50) {
				
				drawImage.drawLine(gridOffset - 5, i, gridOffset + 5, i);
				drawImage.drawString("" + i, gridOffset + 10, i);
				
				drawImage.setColor(Color.GRAY);
				Stroke dashed = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				
				drawImage.setStroke(dashed);
				drawImage.drawLine(gridOffset * 2, i, paintPan.getWidth() - gridOffset, i);
				drawImage.setColor(Color.BLACK);
			}
			
			if (showTreeOn) {
				for (ILocationItem<RDouble> item : tree.getAllPoints()) {
					
					RDouble Rx = item.getDim(0);
					RDouble Ry = item.getDim(1);
					
					Double longitude = (double) Double.parseDouble("" + (Rx.getData()));
					int x = (int) (((longitude - longitudeMin) / longitudeWidth) * (double) (paintPan.getWidth()));
					Double latitude = (double) Double.parseDouble("" + (Ry.getData()));
					int y = (int) (((latitude - latitudeMin) / latitudeHeight) * (double) (paintPan.getHeight()));
					
//					x += 50;
//					y += 50;
					
					
					drawImage = (Graphics2D) g;
					if (color != null) {
						drawImage.setColor(color);
						drawImage.fillOval(x, paintPan.getHeight() - y, 4, 4);
//						paintLogger.log("(" + latitude + ", " + longitude + ")", drawImage, x, y);
//						paintLogger.log(item.getType(), drawImage, x, y + 20);
						
					}
				}
				List<IHyperRectangle<RDouble>> rectangles = new ArrayList<IHyperRectangle<RDouble>>();
				rectangles = tree.getAllRectangles();
				for (IHyperRectangle<RDouble> r : rectangles) {
					
					RDouble Rx = r.getDim1(0);
					RDouble Ry = r.getDim1(1);
					RDouble Rx2 = r.getDim2(0);
					RDouble Ry2 = r.getDim2(1);
					
					Double longitude1 = (double) Double.parseDouble("" + (Rx.getData()));
					int x = (int) (((longitude1 - longitudeMin) / longitudeWidth) * (double) (paintPan.getWidth()));
					Double latitude1 = (double) Double.parseDouble("" + (Ry.getData()));
					int y = (int) (((latitude1 - latitudeMin) / latitudeHeight) * (double) (paintPan.getHeight()));
					
					Double longitude2 = (double) Double.parseDouble("" + (Rx2.getData()));
					int x2 = (int) (((longitude2 - longitudeMin) / longitudeWidth) * (double) (paintPan.getWidth()));
					Double latitude2 = (double) Double.parseDouble("" + (Ry2.getData()));
					int y2 = (int) (((latitude2 - latitudeMin) / latitudeHeight) * (double) (paintPan.getHeight()));
					
//					x += 50;
//					y += 50;
//					x2 += 50;
//					y2 += 50;
					
					
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
//						drawImage.drawRect(x, paintPan.getHeight() - y - height, width, height);
//						paintLogger.log("area: " + r.getSpace(), drawImage, x, y + height - 20);

					}
				}
			}
			

			if (searchRectangle != null) {
				
				drawImage = (Graphics2D) g;
				color = Color.BLACK;
				
				List<ILocationItem<RDouble>> allItems = new ArrayList<ILocationItem<RDouble>>();
				for (IHyperRectangle<RDouble> r : searchResults.keySet()) {
					
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
						
						Double longitude1 = (double) Double.parseDouble("" + (Rx.getData()));
						int x = (int) (((longitude1 - longitudeMin) / longitudeWidth) * (double) (paintPan.getWidth()));
						Double latitude1 = (double) Double.parseDouble("" + (Ry.getData()));
						int y = (int) (((latitude1 - latitudeMin) / latitudeHeight) * (double) (paintPan.getHeight()));
						
						Double longitude2 = (double) Double.parseDouble("" + (Rx2.getData()));
						int x2 = (int) (((longitude2 - longitudeMin) / longitudeWidth) * (double) (paintPan.getWidth()));
						Double latitude2 = (double) Double.parseDouble("" + (Ry2.getData()));
						int y2 = (int) (((latitude2 - latitudeMin) / latitudeHeight) * (double) (paintPan.getHeight()));
						
//						x += 50;
//						y += 50;
//						x2 += 50;
//						y2 += 50;
						
						
						int width = Math.abs(x2 - x);
						int height = Math.abs(y2 - y);
						
//						drawImage.drawRect(x + offset, paintPan.getHeight() - y - height, width - offset, height);
						drawImage.setStroke(existing);
					}
					
					for (ILocationItem<RDouble> i : searchResults.get(r)) {
						
						RDouble Rx = i.getDim(0);
						RDouble Ry = i.getDim(1);
						
						Double longitude = (double) Double.parseDouble("" + (Rx.getData()));
						int itemX = (int) (((longitude - longitudeMin) / longitudeWidth) * (double) (paintPan.getWidth()));
						Double latitude = (double) Double.parseDouble("" + (Ry.getData()));
						int itemY = (int) (((latitude - latitudeMin) / latitudeHeight) * (double) (paintPan.getHeight()));
						
//						itemX += 50;
//						itemY += 50;
						
						
						if (color != null) {
							drawImage.setColor(Color.decode("#ff6680"));
							drawImage.fillOval(itemX, paintPan.getHeight() - itemY, 5, 5);
							drawImage.drawString("(" + itemX + ", " + itemY + ")", itemX, paintPan.getHeight() - itemY);
							drawImage.setColor(Color.BLACK);
							
							drawImage.setFont(new Font("default", Font.BOLD, 16));
							drawImage.drawString(i.getType(), itemX, paintPan.getHeight() - itemY + 20);
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
					for (ILocationItem<RDouble> i : allItems) {
						
						DecimalFormat df = new DecimalFormat("#.##");      
						Double longitude = Double.parseDouble(df.format(i.getDim(0).getData()));
						Double latitude = Double.parseDouble(df.format(i.getDim(1).getData()));
						dataStrings.add(longitude + "," + latitude + " " + i.getProperty("city") + "," + i.getProperty("country"));
						
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
				
				Double longitude1 = (double) Double.parseDouble("" + (Rx.getData()));
				int x = (int) (((longitude1 - longitudeMin) / longitudeWidth) * (double) (paintPan.getWidth()));
				Double latitude1 = (double) Double.parseDouble("" + (Ry.getData()));
				int y = (int) (((latitude1 - latitudeMin) / latitudeHeight) * (double) (paintPan.getHeight()));
				
				Double longitude2 = (double) Double.parseDouble("" + (Rx2.getData()));
				int x2 = (int) (((longitude2 - longitudeMin) / longitudeWidth) * (double) (paintPan.getWidth()));
				Double latitude2 = (double) Double.parseDouble("" + (Ry2.getData()));
				int y2 = (int) (((latitude2 - latitudeMin) / latitudeHeight) * (double) (paintPan.getHeight()));
				
//				x += 50;
//				y += 50;
//				x2 += 50;
//				y2 += 50;
				
				
				int width = Math.abs(x2 - x);
				int height = Math.abs(y2 - y);
				
				drawImage.drawRect(x, paintPan.getHeight() - y - showTree.getHeight(), width, height);
				drawImage.setStroke(existing);
				
				
			}
//			searchRectangle = null;
			color = Color.BLACK;
		}

		public void updateGraphics(int x, int y) throws IOException {
			color = Color.BLACK;

//			LocationItemNDGeneric<RDouble> item = new LocationItemNDGeneric<RDouble>(2);
//			
//			RDouble Rx = new RDouble((double) x / scaleFactor);
//			RDouble Ry = new RDouble((double) y / scaleFactor);
//			
//			item.setDim(0, Rx);
//			item.setDim(1, Ry);
//			
//			// addPoint(item);
//			tree.insert(item);
//			tree.updateRoot();
			
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
				searchRectangle = new HyperRectangle<RDouble>(2);
				
				
				searchRectangle.setDim1(0, new RDouble(x));
				searchRectangle.setDim2(0, new RDouble(x + range));
				searchRectangle.setDim1(1, new RDouble(y));
				searchRectangle.setDim2(1, new RDouble(y + range));
				
				searchResults = tree.search(searchRectangle);
				for (IHyperRectangle<RDouble> r : searchResults.keySet()) {
					logger.log("search results: " + r);
					for (ILocationItem<RDouble> i : searchResults.get(r)) {
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

				ILocationItem<RDouble> toDelete = new LocationItem<RDouble>(2);
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
