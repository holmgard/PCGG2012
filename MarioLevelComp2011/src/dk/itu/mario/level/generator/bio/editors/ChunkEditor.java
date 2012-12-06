package dk.itu.mario.level.generator.bio.editors;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.level.generator.bio.Chunk;
import dk.itu.mario.level.generator.bio.ChunkLibrary;

import javax.swing.SpringLayout;
import javax.swing.BoxLayout;
import java.awt.SystemColor;
import java.awt.Color;
import java.io.File;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ButtonGroup;

public class ChunkEditor {

	// Components
	private JFrame frmSuperChunkEditor;
	
	private JList<Integer> list;
	private JSpinner widthSpinner;
	private JSpinner heightSpinner;
	private LevelDrawer levelDrawer;
	private JRadioButton tileRadio;
	private JRadioButton enemyRadio;
	private JComboBox<String> tileCombo;
	private JComboBox<String> enemyCombo;
	private JCheckBox wingedCheck;
	
	// Chunk editing stuff ;)
	private ChunkLibrary cl;
	private Chunk currentChunk;
	private byte currentTile;
	private SpriteTemplate currentSprite;
	
	private boolean mousePressed;
	private boolean draw;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChunkEditor window = new ChunkEditor();
					window.frmSuperChunkEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChunkEditor() {
		initialize();
		
		cl = ChunkLibrary.getInstance();
		File clFile = new File("chunkLibrary.res");
		try {
			cl.readLibFromFile(clFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		// Get chunks and add entries to the list.
		int numOfChunks = cl.getNumOfChunks();
		int id = 0;
		DefaultListModel<Integer> dlm = new DefaultListModel<Integer>();
		while (numOfChunks > 0) {
			Chunk chunk = cl.getChunk(id);
			if (chunk != null) {
				dlm.addElement(chunk.getId());
				--numOfChunks;
			}
			++id;
		}
		list.setModel(dlm);
		
		levelDrawer.init();
		
		createNewChunk();
		
		populateTileCombo();
		populateEnemyCombo();
		
		mousePressed = false;
		draw = true;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSuperChunkEditor = new JFrame();
		frmSuperChunkEditor.setTitle("Super Chunk Editor");
		frmSuperChunkEditor.setBounds(100, 100, 1000, 414);
		frmSuperChunkEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmSuperChunkEditor.getContentPane().setLayout(springLayout);
		
		JPanel chunkPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, chunkPanel, 10, SpringLayout.NORTH, frmSuperChunkEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, chunkPanel, 10, SpringLayout.WEST, frmSuperChunkEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, chunkPanel, 366, SpringLayout.NORTH, frmSuperChunkEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, chunkPanel, -381, SpringLayout.EAST, frmSuperChunkEditor.getContentPane());
		chunkPanel.setBackground(Color.WHITE);
		frmSuperChunkEditor.getContentPane().add(chunkPanel);
		
		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 10, SpringLayout.NORTH, frmSuperChunkEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel, 6, SpringLayout.EAST, chunkPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, panel, -10, SpringLayout.SOUTH, frmSuperChunkEditor.getContentPane());
		chunkPanel.setLayout(null);
		
		levelDrawer = new LevelDrawer();
		levelDrawer.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (!mousePressed)
					return;
				
				if (tileRadio.isSelected()) {
					if (draw)
						currentChunk.getMap()[e.getX() / 16][e.getY() / 16] = currentTile;
					else
						currentChunk.getMap()[e.getX() / 16][e.getY() / 16] = 0;
				} else {
					SpriteTemplate[][] sprites = currentChunk.getSprites();
					if (draw) {
						SpriteTemplate sprite = sprites[e.getX() / 16][e.getY() / 16];
						if (sprite != null) {
							if ((sprite.type != currentSprite.type) || (sprite.winged != currentSprite.winged))
								sprites[e.getX() / 16][e.getY() / 16] = currentSprite.clone();
						} else
							sprites[e.getX() / 16][e.getY() / 16] = currentSprite.clone();
					} else {
						sprites[e.getX() / 16][e.getY() / 16] = null;
					}
				}
				
				levelDrawer.repaint();
			}
		});
		levelDrawer.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				mousePressed = true;
				if (arg0.getButton() == MouseEvent.BUTTON1) // if left button clicked
					draw = true;
				else if (arg0.getButton() == MouseEvent.BUTTON3)
					draw = false;
				
				if (tileRadio.isSelected()) {
					if (draw)
						currentChunk.getMap()[arg0.getX() / 16][arg0.getY() / 16] = currentTile;
					else
						currentChunk.getMap()[arg0.getX() / 16][arg0.getY() / 16] = 0;
				} else {
					SpriteTemplate[][] sprites = currentChunk.getSprites();
					if (draw) {
						SpriteTemplate sprite = sprites[arg0.getX() / 16][arg0.getY() / 16];
						if (sprite != null) {
							if ((sprite.type != currentSprite.type) || (sprite.winged != currentSprite.winged))
								sprites[arg0.getX() / 16][arg0.getY() / 16] = currentSprite.clone();
						} else
							sprites[arg0.getX() / 16][arg0.getY() / 16] = currentSprite.clone();
					} else {
						sprites[arg0.getX() / 16][arg0.getY() / 16] = null;
					}
				}
				
				levelDrawer.repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				mousePressed = false;
			}
		});
		levelDrawer.setBackground(SystemColor.control);
		levelDrawer.setBounds(10, 11, 512, 128);
		chunkPanel.add(levelDrawer);
		frmSuperChunkEditor.getContentPane().add(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JPanel settingsPanel = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, settingsPanel, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, settingsPanel, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, settingsPanel, 0, SpringLayout.EAST, panel);
		panel.add(settingsPanel);
		
		JPanel tilePanel = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, tilePanel, 148, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, tilePanel, 0, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, settingsPanel, -6, SpringLayout.NORTH, tilePanel);
		sl_panel.putConstraint(SpringLayout.WEST, tilePanel, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, tilePanel, 0, SpringLayout.EAST, panel);
		SpringLayout sl_settingsPanel = new SpringLayout();
		settingsPanel.setLayout(sl_settingsPanel);
		
		widthSpinner = new JSpinner();
		widthSpinner.setModel(new SpinnerNumberModel(8, 1, 32, 1));
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, widthSpinner, 10, SpringLayout.NORTH, settingsPanel);
		settingsPanel.add(widthSpinner);
		
		JLabel lblWidth = new JLabel("Width");
		sl_settingsPanel.putConstraint(SpringLayout.WEST, widthSpinner, -65, SpringLayout.WEST, lblWidth);
		sl_settingsPanel.putConstraint(SpringLayout.EAST, widthSpinner, -6, SpringLayout.WEST, lblWidth);
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, lblWidth, 10, SpringLayout.NORTH, settingsPanel);
		sl_settingsPanel.putConstraint(SpringLayout.EAST, lblWidth, -10, SpringLayout.EAST, settingsPanel);
		settingsPanel.add(lblWidth);
		
		heightSpinner = new JSpinner();
		heightSpinner.setModel(new SpinnerNumberModel(8, 1, 15, 1));
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, heightSpinner, 6, SpringLayout.SOUTH, widthSpinner);
		sl_settingsPanel.putConstraint(SpringLayout.WEST, heightSpinner, 0, SpringLayout.WEST, widthSpinner);
		sl_settingsPanel.putConstraint(SpringLayout.EAST, heightSpinner, 0, SpringLayout.EAST, widthSpinner);
		settingsPanel.add(heightSpinner);
		
		JLabel lblHeight = new JLabel("Height");
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, lblHeight, 6, SpringLayout.SOUTH, widthSpinner);
		sl_settingsPanel.putConstraint(SpringLayout.EAST, lblHeight, -10, SpringLayout.EAST, settingsPanel);
		settingsPanel.add(lblHeight);
		
		JButton btnNew = new JButton("New");
		sl_settingsPanel.putConstraint(SpringLayout.NORTH, btnNew, 6, SpringLayout.SOUTH, heightSpinner);
		sl_settingsPanel.putConstraint(SpringLayout.EAST, btnNew, 0, SpringLayout.EAST, widthSpinner);
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createNewChunk();
			}
		});
		settingsPanel.add(btnNew);
		
		JButton btnAddChunk = new JButton("Add Chunk");
		sl_settingsPanel.putConstraint(SpringLayout.WEST, btnAddChunk, 10, SpringLayout.WEST, settingsPanel);
		btnAddChunk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addChunk();
			}
		});
		sl_settingsPanel.putConstraint(SpringLayout.SOUTH, btnAddChunk, -10, SpringLayout.SOUTH, settingsPanel);
		settingsPanel.add(btnAddChunk);
		panel.add(tilePanel);
		SpringLayout sl_tilePanel = new SpringLayout();
		tilePanel.setLayout(sl_tilePanel);
		
		tileRadio = new JRadioButton("Tile");
		buttonGroup.add(tileRadio);
		tileRadio.setSelected(true);
		sl_tilePanel.putConstraint(SpringLayout.NORTH, tileRadio, 10, SpringLayout.NORTH, tilePanel);
		sl_tilePanel.putConstraint(SpringLayout.WEST, tileRadio, 10, SpringLayout.WEST, tilePanel);
		tilePanel.add(tileRadio);
		
		enemyRadio = new JRadioButton("Enemy");
		buttonGroup.add(enemyRadio);
		sl_tilePanel.putConstraint(SpringLayout.WEST, enemyRadio, 0, SpringLayout.WEST, tileRadio);
		tilePanel.add(enemyRadio);
		
		tileCombo = new JComboBox();
		tileCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectTile();
			}
		});

		sl_tilePanel.putConstraint(SpringLayout.NORTH, enemyRadio, 6, SpringLayout.SOUTH, tileCombo);
		sl_tilePanel.putConstraint(SpringLayout.EAST, tileCombo, 227, SpringLayout.WEST, tilePanel);
		sl_tilePanel.putConstraint(SpringLayout.NORTH, tileCombo, 6, SpringLayout.SOUTH, tileRadio);
		sl_tilePanel.putConstraint(SpringLayout.WEST, tileCombo, 10, SpringLayout.WEST, tilePanel);
		tilePanel.add(tileCombo);
		
		enemyCombo = new JComboBox();
		enemyCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectSprite();
			}
		});
		sl_tilePanel.putConstraint(SpringLayout.NORTH, enemyCombo, 6, SpringLayout.SOUTH, enemyRadio);
		sl_tilePanel.putConstraint(SpringLayout.WEST, enemyCombo, 10, SpringLayout.WEST, tilePanel);
		sl_tilePanel.putConstraint(SpringLayout.EAST, enemyCombo, 0, SpringLayout.EAST, tileCombo);
		tilePanel.add(enemyCombo);
		
		wingedCheck = new JCheckBox("winged");
		wingedCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectSprite();
			}
		});
		sl_tilePanel.putConstraint(SpringLayout.NORTH, wingedCheck, 6, SpringLayout.SOUTH, enemyCombo);
		sl_tilePanel.putConstraint(SpringLayout.WEST, wingedCheck, 0, SpringLayout.WEST, tileRadio);
		tilePanel.add(wingedCheck);
		
		JPanel libPanel = new JPanel();
		springLayout.putConstraint(SpringLayout.EAST, libPanel, -10, SpringLayout.EAST, frmSuperChunkEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, -6, SpringLayout.WEST, libPanel);
		springLayout.putConstraint(SpringLayout.NORTH, libPanel, 10, SpringLayout.NORTH, frmSuperChunkEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, libPanel, 852, SpringLayout.WEST, frmSuperChunkEditor.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, libPanel, -10, SpringLayout.SOUTH, frmSuperChunkEditor.getContentPane());
		frmSuperChunkEditor.getContentPane().add(libPanel);
		SpringLayout sl_libPanel = new SpringLayout();
		libPanel.setLayout(sl_libPanel);
		
		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				selectChunk();
			}
		});
		sl_libPanel.putConstraint(SpringLayout.NORTH, list, 0, SpringLayout.NORTH, libPanel);
		sl_libPanel.putConstraint(SpringLayout.WEST, list, 0, SpringLayout.WEST, libPanel);
		sl_libPanel.putConstraint(SpringLayout.SOUTH, list, 294, SpringLayout.NORTH, libPanel);
		sl_libPanel.putConstraint(SpringLayout.EAST, list, 122, SpringLayout.WEST, libPanel);
		libPanel.add(list);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeChunk(list.getSelectedValue());
			}
		});
		sl_libPanel.putConstraint(SpringLayout.NORTH, btnDelete, 6, SpringLayout.SOUTH, list);
		sl_libPanel.putConstraint(SpringLayout.EAST, btnDelete, -10, SpringLayout.EAST, list);
		libPanel.add(btnDelete);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					cl.writeLibToFile(new File("chunkLibrary.res"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Failed to save the chunks.");
				}
			}
		});
		sl_libPanel.putConstraint(SpringLayout.NORTH, btnSave, 0, SpringLayout.SOUTH, btnDelete);
		sl_libPanel.putConstraint(SpringLayout.WEST, btnSave, 10, SpringLayout.WEST, libPanel);
		libPanel.add(btnSave);
	}
	
	private void createNewChunk() {
		int w = (Integer)widthSpinner.getValue();
		int h = (Integer)heightSpinner.getValue();
		currentChunk = new Chunk(cl.getNextId(), w, h);
		levelDrawer.setSize(w * 16, h * 16);
		levelDrawer.setMap(currentChunk.getMap());
		levelDrawer.setSprites(currentChunk.getSprites());
		levelDrawer.repaint();
	}
	
	private void addChunk() {
		if (!cl.addChunk(currentChunk)) {
			JOptionPane.showMessageDialog(null, "Failed to add Chunk since the ID is already in use.");
		} else {
			DefaultListModel<Integer> dlm = (DefaultListModel<Integer>) list.getModel();
			dlm.addElement(currentChunk.getId());
		}
	}
	
	private void selectChunk() {
		Chunk sel = cl.getChunk(list.getSelectedValue());
		if (sel != null) {
			currentChunk = sel;
			levelDrawer.setSize(sel.getWidth() * 16, sel.getHeight() * 16);
			widthSpinner.setValue(sel.getWidth());
			heightSpinner.setValue(sel.getHeight());
			levelDrawer.setMap(currentChunk.getMap());
			levelDrawer.setSprites(currentChunk.getSprites());
			levelDrawer.repaint();
		}
	}
	
	private void removeChunk(int id) {
		cl.removeChunk(id);
		DefaultListModel<Integer> dlm = (DefaultListModel<Integer>) list.getModel();
		dlm.removeElement(id);
	}
	
	private void populateTileCombo() {
		DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<String>();
		
		dcbm.addElement(String.format("%04d - Block Coin", LevelDrawer.BLOCK_COIN));
		dcbm.addElement(String.format("%04d - Block Empty", LevelDrawer.BLOCK_EMPTY));
		dcbm.addElement(String.format("%04d - Block Powerup", LevelDrawer.BLOCK_POWERUP));
		dcbm.addElement(String.format("%04d - Coin", LevelDrawer.COIN));
		dcbm.addElement(String.format("%04d - Ground", LevelDrawer.GROUND));
		dcbm.addElement(String.format("%04d - Hill Fill", LevelDrawer.HILL_FILL));
		dcbm.addElement(String.format("%04d - Hill Left", LevelDrawer.HILL_LEFT));
		dcbm.addElement(String.format("%04d - Hill Right", LevelDrawer.HILL_RIGHT));
		dcbm.addElement(String.format("%04d - Hill Top", LevelDrawer.HILL_TOP));
		dcbm.addElement(String.format("%04d - Hill Top Left", LevelDrawer.HILL_TOP_LEFT));
		dcbm.addElement(String.format("%04d - Hill Top Left In", LevelDrawer.HILL_TOP_LEFT_IN));
		dcbm.addElement(String.format("%04d - Hill Top Right", LevelDrawer.HILL_TOP_RIGHT));
		dcbm.addElement(String.format("%04d - Hill Top Right In", LevelDrawer.HILL_TOP_RIGHT_IN));
		dcbm.addElement(String.format("%04d - Left Grass Edge", LevelDrawer.LEFT_GRASS_EDGE));
		dcbm.addElement(String.format("%04d - Left Pocket Grass", LevelDrawer.LEFT_POCKET_GRASS));
		dcbm.addElement(String.format("%04d - Left Up Grass Edge", LevelDrawer.LEFT_UP_GRASS_EDGE));
		dcbm.addElement(String.format("%04d - Right Grass Edge", LevelDrawer.RIGHT_GRASS_EDGE));
		dcbm.addElement(String.format("%04d - Right Pocket Grass", LevelDrawer.RIGHT_POCKET_GRASS));
		dcbm.addElement(String.format("%04d - Right Up Grass Edge", LevelDrawer.RIGHT_UP_GRASS_EDGE));
		dcbm.addElement(String.format("%04d - Rock", LevelDrawer.ROCK));
		dcbm.addElement(String.format("%04d - Tube Side Left", LevelDrawer.TUBE_SIDE_LEFT));
		dcbm.addElement(String.format("%04d - Tube Side Right", LevelDrawer.TUBE_SIDE_RIGHT));
		dcbm.addElement(String.format("%04d - Tube Top Left", LevelDrawer.TUBE_TOP_LEFT));
		dcbm.addElement(String.format("%04d - Tube Top Right", LevelDrawer.TUBE_TOP_RIGHT));
		
		tileCombo.setModel(dcbm);
		tileCombo.setSelectedIndex(0);
		currentTile = LevelDrawer.BLOCK_COIN;
	}
	
	private void populateEnemyCombo() {
		DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<String>();
		
		dcbm.addElement(String.format("%d - Red Turtle", SpriteTemplate.RED_TURTLE));
		dcbm.addElement(String.format("%d - Green Turtle", SpriteTemplate.GREEN_TURTLE));
		dcbm.addElement(String.format("%d - Goompa", SpriteTemplate.GOOMPA));
		dcbm.addElement(String.format("%d - Armored Turtle", SpriteTemplate.ARMORED_TURTLE));
		dcbm.addElement(String.format("%d - Jump Flower", SpriteTemplate.JUMP_FLOWER));
		dcbm.addElement(String.format("%d - Cannon Ball", SpriteTemplate.CANNON_BALL));
		dcbm.addElement(String.format("%d - Chomp Flower", SpriteTemplate.CHOMP_FLOWER));
		
		enemyCombo.setModel(dcbm);
		enemyCombo.setSelectedIndex(0);
		currentSprite = new SpriteTemplate(0, wingedCheck.isSelected());
	}
	
	private void selectTile() {
		String item = (String) tileCombo.getModel().getSelectedItem();
		String b = item.substring(0, 4);
		currentTile = Byte.parseByte(b);
	}
	
	private void selectSprite() {
		String item = (String) enemyCombo.getModel().getSelectedItem();
		String b = item.substring(0, 1);
		currentSprite = new SpriteTemplate(Integer.parseInt(b), wingedCheck.isSelected());
	}
}
