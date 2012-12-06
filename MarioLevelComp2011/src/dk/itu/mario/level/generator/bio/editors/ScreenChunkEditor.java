package dk.itu.mario.level.generator.bio.editors;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;

import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.level.generator.bio.Chunk;
import dk.itu.mario.level.generator.bio.ChunkLibrary;
import dk.itu.mario.level.generator.bio.ScreenChunk;
import dk.itu.mario.level.generator.bio.ScreenChunkLibrary;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;

public class ScreenChunkEditor {

	private JFrame frmSuperScreenChunk;
	private JSpinner widthSpinner;
	private JComboBox<Integer> chunkCombo;
	private JSpinner inSSpinner;
	private JSpinner inESpinner;
	private JList<String> inList;
	private JSpinner outSSpinner;
	private JSpinner outESpinner;
	private JList<String> outList;
	private JList<Integer> screenList;
	
	private ChunkLibrary cl;
	private ScreenChunkLibrary scl;
	private Chunk currentChunk;
	private ScreenChunk currentScreenChunk;
	private byte[][] map;
	private SpriteTemplate[][] sprites;
	private LevelDrawer levelDrawer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScreenChunkEditor window = new ScreenChunkEditor();
					window.frmSuperScreenChunk.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ScreenChunkEditor() {
		initialize();
		
		cl = ChunkLibrary.getInstance();
		scl = ScreenChunkLibrary.getInstance();
		
		try {
			cl.readLibFromFile(new File("chunkLibrary.res"));
			scl.readLibFromFile(new File("screenChunkLibrary.res"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to load library");
		}
		
		inList.setModel(new DefaultListModel<String>());
		outList.setModel(new DefaultListModel<String>());
		
		populateChunkCombo();
		
		createNewScreenChunk();
		
		populateScreenList();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSuperScreenChunk = new JFrame();
		frmSuperScreenChunk.setTitle("Super Screen Chunk Editor");
		frmSuperScreenChunk.setBounds(100, 100, 1010, 501);
		frmSuperScreenChunk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmSuperScreenChunk.getContentPane().setLayout(springLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, frmSuperScreenChunk.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, frmSuperScreenChunk.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 453, SpringLayout.NORTH, frmSuperScreenChunk.getContentPane());
		frmSuperScreenChunk.getContentPane().add(scrollPane);
		
		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.WEST, panel, 612, SpringLayout.WEST, frmSuperScreenChunk.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, -214, SpringLayout.EAST, frmSuperScreenChunk.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -6, SpringLayout.WEST, panel);
		
		levelDrawer = new LevelDrawer();
		scrollPane.setViewportView(levelDrawer);
		springLayout.putConstraint(SpringLayout.NORTH, panel, 10, SpringLayout.NORTH, frmSuperScreenChunk.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, 453, SpringLayout.NORTH, frmSuperScreenChunk.getContentPane());
		frmSuperScreenChunk.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		springLayout.putConstraint(SpringLayout.WEST, panel_1, 786, SpringLayout.WEST, frmSuperScreenChunk.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, panel_1, 10, SpringLayout.NORTH, frmSuperScreenChunk.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel_1, 453, SpringLayout.NORTH, frmSuperScreenChunk.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel_1, 204, SpringLayout.EAST, panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JPanel panel_2 = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, panel_2, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, panel_2, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, panel_2, 98, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panel_2, 168, SpringLayout.WEST, panel);
		panel.add(panel_2);
		SpringLayout sl_panel_2 = new SpringLayout();
		panel_2.setLayout(sl_panel_2);
		
		JLabel lblWidth = new JLabel("Width");
		sl_panel_2.putConstraint(SpringLayout.NORTH, lblWidth, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, lblWidth, -10, SpringLayout.EAST, panel_2);
		panel_2.add(lblWidth);
		
		widthSpinner = new JSpinner();
		widthSpinner.setModel(new SpinnerNumberModel(24, 1, 64, 1));
		sl_panel_2.putConstraint(SpringLayout.NORTH, widthSpinner, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, widthSpinner, -88, SpringLayout.WEST, lblWidth);
		sl_panel_2.putConstraint(SpringLayout.EAST, widthSpinner, -6, SpringLayout.WEST, lblWidth);
		panel_2.add(widthSpinner);
		
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewScreenChunk();
			}
		});
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnNew, 6, SpringLayout.SOUTH, widthSpinner);
		sl_panel_2.putConstraint(SpringLayout.EAST, btnNew, 0, SpringLayout.EAST, lblWidth);
		panel_2.add(btnNew);
		
		JButton btnAddScreenChunk = new JButton("Add Screen Chunk");
		btnAddScreenChunk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addScreenChunk();
			}
		});
		sl_panel_2.putConstraint(SpringLayout.NORTH, btnAddScreenChunk, 6, SpringLayout.SOUTH, btnNew);
		sl_panel_2.putConstraint(SpringLayout.WEST, btnAddScreenChunk, 10, SpringLayout.WEST, panel_2);
		panel_2.add(btnAddScreenChunk);
		
		chunkCombo = new JComboBox();
		sl_panel.putConstraint(SpringLayout.NORTH, chunkCombo, 6, SpringLayout.SOUTH, panel_2);
		sl_panel.putConstraint(SpringLayout.WEST, chunkCombo, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, chunkCombo, 158, SpringLayout.WEST, panel);
		panel.add(chunkCombo);
		
		JLabel lblInWindow = new JLabel("In Window");
		sl_panel.putConstraint(SpringLayout.NORTH, lblInWindow, 23, SpringLayout.SOUTH, chunkCombo);
		sl_panel.putConstraint(SpringLayout.WEST, lblInWindow, 0, SpringLayout.WEST, panel_2);
		panel.add(lblInWindow);
		
		JLabel lblS = new JLabel("S");
		sl_panel.putConstraint(SpringLayout.NORTH, lblS, 6, SpringLayout.SOUTH, lblInWindow);
		sl_panel.putConstraint(SpringLayout.WEST, lblS, 0, SpringLayout.WEST, panel_2);
		panel.add(lblS);
		
		inSSpinner = new JSpinner();
		inSSpinner.setModel(new SpinnerNumberModel(0, 0, 13, 1));
		sl_panel.putConstraint(SpringLayout.WEST, inSSpinner, 0, SpringLayout.WEST, chunkCombo);
		sl_panel.putConstraint(SpringLayout.SOUTH, inSSpinner, 0, SpringLayout.SOUTH, lblS);
		panel.add(inSSpinner);
		
		JLabel lblE = new JLabel("E");
		sl_panel.putConstraint(SpringLayout.WEST, lblE, 6, SpringLayout.EAST, inSSpinner);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblE, 0, SpringLayout.SOUTH, lblS);
		panel.add(lblE);
		
		inESpinner = new JSpinner();
		inESpinner.setModel(new SpinnerNumberModel(14, 1, 14, 1));
		sl_panel.putConstraint(SpringLayout.WEST, inESpinner, 6, SpringLayout.EAST, lblE);
		sl_panel.putConstraint(SpringLayout.SOUTH, inESpinner, 0, SpringLayout.SOUTH, lblS);
		panel.add(inESpinner);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addInWindow();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnAdd, -4, SpringLayout.NORTH, lblS);
		sl_panel.putConstraint(SpringLayout.EAST, btnAdd, -10, SpringLayout.EAST, panel);
		panel.add(btnAdd);
		
		inList = new JList<String>();
		sl_panel.putConstraint(SpringLayout.NORTH, inList, 1, SpringLayout.SOUTH, btnAdd);
		sl_panel.putConstraint(SpringLayout.WEST, inList, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, inList, 56, SpringLayout.SOUTH, btnAdd);
		sl_panel.putConstraint(SpringLayout.EAST, inList, 158, SpringLayout.WEST, panel);
		panel.add(inList);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeInWindow();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnDelete, 4, SpringLayout.SOUTH, inList);
		sl_panel.putConstraint(SpringLayout.EAST, btnDelete, -10, SpringLayout.EAST, panel);
		panel.add(btnDelete);
		
		JLabel lblOutWindow = new JLabel("Out Window");
		sl_panel.putConstraint(SpringLayout.NORTH, lblOutWindow, 44, SpringLayout.SOUTH, inList);
		sl_panel.putConstraint(SpringLayout.WEST, lblOutWindow, 0, SpringLayout.WEST, panel_2);
		panel.add(lblOutWindow);
		
		JLabel lblS_1 = new JLabel("S");
		sl_panel.putConstraint(SpringLayout.NORTH, lblS_1, 6, SpringLayout.SOUTH, lblOutWindow);
		sl_panel.putConstraint(SpringLayout.WEST, lblS_1, 0, SpringLayout.WEST, panel_2);
		panel.add(lblS_1);
		
		outSSpinner = new JSpinner();
		outSSpinner.setModel(new SpinnerNumberModel(0, 0, 13, 1));
		sl_panel.putConstraint(SpringLayout.WEST, outSSpinner, 0, SpringLayout.WEST, chunkCombo);
		sl_panel.putConstraint(SpringLayout.SOUTH, outSSpinner, 0, SpringLayout.SOUTH, lblS_1);
		panel.add(outSSpinner);
		
		JLabel lblE_1 = new JLabel("E");
		sl_panel.putConstraint(SpringLayout.WEST, lblE_1, 0, SpringLayout.WEST, lblE);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblE_1, 0, SpringLayout.SOUTH, lblS_1);
		panel.add(lblE_1);
		
		outESpinner = new JSpinner();
		outESpinner.setModel(new SpinnerNumberModel(14, 1, 14, 1));
		sl_panel.putConstraint(SpringLayout.WEST, outESpinner, 0, SpringLayout.WEST, inESpinner);
		sl_panel.putConstraint(SpringLayout.SOUTH, outESpinner, 0, SpringLayout.SOUTH, lblS_1);
		panel.add(outESpinner);
		
		JButton btnAdd_1 = new JButton("Add");
		btnAdd_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addOutWindow();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnAdd_1, -4, SpringLayout.NORTH, lblS_1);
		sl_panel.putConstraint(SpringLayout.EAST, btnAdd_1, 0, SpringLayout.EAST, panel_2);
		panel.add(btnAdd_1);
		
		outList = new JList<String>();
		sl_panel.putConstraint(SpringLayout.NORTH, outList, 1, SpringLayout.SOUTH, btnAdd_1);
		sl_panel.putConstraint(SpringLayout.WEST, outList, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, outList, 56, SpringLayout.SOUTH, btnAdd_1);
		sl_panel.putConstraint(SpringLayout.EAST, outList, 158, SpringLayout.WEST, panel);
		panel.add(outList);
		
		JButton btnDelete_1 = new JButton("Delete");
		btnDelete_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeOutWindow();
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnDelete_1, 6, SpringLayout.SOUTH, outList);
		sl_panel.putConstraint(SpringLayout.EAST, btnDelete_1, 0, SpringLayout.EAST, chunkCombo);
		panel.add(btnDelete_1);
		frmSuperScreenChunk.getContentPane().add(panel_1);
		SpringLayout sl_panel_1 = new SpringLayout();
		panel_1.setLayout(sl_panel_1);
		
		screenList = new JList<Integer>();
		sl_panel_1.putConstraint(SpringLayout.NORTH, screenList, 10, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, screenList, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, screenList, 379, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, screenList, 188, SpringLayout.WEST, panel_1);
		panel_1.add(screenList);
		
		JButton btnRemove = new JButton("Remove");
		sl_panel_1.putConstraint(SpringLayout.NORTH, btnRemove, 6, SpringLayout.SOUTH, screenList);
		sl_panel_1.putConstraint(SpringLayout.EAST, btnRemove, -10, SpringLayout.EAST, panel_1);
		panel_1.add(btnRemove);
		
		JButton btnSaveLibrary = new JButton("Save Library");
		btnSaveLibrary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					scl.writeLibToFile(new File("screenChunkLibrary.res"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Failed to save Screen Chunk Library");
				}
			}
		});
		sl_panel_1.putConstraint(SpringLayout.WEST, btnSaveLibrary, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, btnSaveLibrary, -10, SpringLayout.SOUTH, panel_1);
		panel_1.add(btnSaveLibrary);
	}
	
	private void populateChunkCombo() {
		DefaultComboBoxModel<Integer> dcbm = new DefaultComboBoxModel<Integer>();

		// Get chunks and add entries to the list.
		int numOfChunks = cl.getNumOfChunks();
		int id = 0;

		while (numOfChunks > 0) {
			Chunk chunk = cl.getChunk(id);
			if (chunk != null) {
				dcbm.addElement(chunk.getId());
				--numOfChunks;
			}
			++id;
		}
		
		chunkCombo.setModel(dcbm);
		chunkCombo.setSelectedIndex(0);
		currentChunk = cl.getChunk((Integer) dcbm.getSelectedItem());
	}
	
	private void createNewScreenChunk() {
		int w = (Integer) widthSpinner.getValue();
		currentScreenChunk = new ScreenChunk(scl.getNextId(), w);
		map = new byte[w][15];
		sprites = new SpriteTemplate[w][15];
		
		updateWindows();
		
		levelDrawer.setSize(w * 16, 15 * 16);
		levelDrawer.setPreferredSize(new Dimension(w * 16, 15 * 16));
		levelDrawer.revalidate();
		levelDrawer.setMap(map);
		levelDrawer.setSprites(sprites);
		levelDrawer.repaint();
	}
	
	private void populateScreenList() {
		DefaultListModel<Integer> dlm = new DefaultListModel<Integer>();

		// Get chunks and add entries to the list.
		int numOfChunks = scl.getNumOfChunks();
		int id = 0;

		while (numOfChunks > 0) {
			ScreenChunk chunk = scl.getChunk(id);
			if (chunk != null) {
				dlm.addElement(chunk.getId());
				--numOfChunks;
			}
			++id;
		}
		
		screenList.setModel(dlm);
		//screenList.setSelectedIndex(0);
		//currentScreenChunk = scl.getChunk(dlm.get(0));
	}
	
	private void updateWindows() {
		DefaultListModel<String> in = (DefaultListModel<String>) inList.getModel();
		DefaultListModel<String> out = (DefaultListModel<String>) outList.getModel();
		
		List<byte[]> inWindows = currentScreenChunk.getInWindows();
		List<byte[]> outWindows = currentScreenChunk.getOutWindows();
		
		in.clear();
		out.clear();
		
		for (byte[] w : inWindows) {
			in.addElement(String.format("S: %d ; E: %d", w[0], w[1]));
		}
		
		for (byte[] w : outWindows) {
			out.addElement(String.format("S: %d ; E: %d", w[0], w[1]));
		}
	}
	
	private void addScreenChunk() {
		if ((currentScreenChunk.getInWindows().size() == 0) || (currentScreenChunk.getOutWindows().size() == 0)) {
			JOptionPane.showMessageDialog(null, "Current Screen Chunk does not have properly initialized windows");
			return;
		}
		
		if (!scl.addChunk(currentScreenChunk))
			JOptionPane.showMessageDialog(null, "Screen Chunk with current ID is already added");
		
		DefaultListModel<Integer> list = (DefaultListModel<Integer>) screenList.getModel();
		list.addElement(currentScreenChunk.getId());
	}
	
	private void addInWindow() {
		int s = (Integer) inSSpinner.getValue();
		int e = (Integer) inESpinner.getValue();
		
		currentScreenChunk.addInWindow((byte)s, (byte)e);
		
		updateWindows();
	}
	
	private void addOutWindow() {
		int s = (Integer) outSSpinner.getValue();
		int e = (Integer) outESpinner.getValue();
		
		currentScreenChunk.addOutWindow((byte)s, (byte)e);
		
		updateWindows();
	}
	
	private void removeInWindow() {
		DefaultListModel<String> in = (DefaultListModel<String>) inList.getModel();
		
		currentScreenChunk.removeInWindow(inList.getSelectedIndex());
		in.remove(inList.getSelectedIndex());
		
		updateWindows();
	}
	
	private void removeOutWindow() {
		DefaultListModel<String> out = (DefaultListModel<String>) outList.getModel();
		
		currentScreenChunk.removeOutWindow(outList.getSelectedIndex());
		out.remove(outList.getSelectedIndex());
		
		updateWindows();
	}
}
