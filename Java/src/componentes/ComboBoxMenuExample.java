/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package componentes;

/**
 *
 * @author DAW
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * @version 1.0 05/10/99
 */
public class ComboBoxMenuExample extends JFrame {

  public ComboBoxMenuExample() {
    super("ComboBoxMenu Example");

    String[] itemStr = { "name", "Red", "Blue", "number", "255,0,0",
        "0,0,255",
        // separator
        "system", "control", "controlHighlight", "controlShadow",
        "text" };

    JMenuItem[] menuItems = new JMenuItem[7];
    menuItems[0] = new JMenuItem(itemStr[1]);
    menuItems[1] = new JMenuItem(itemStr[2]);
    menuItems[2] = new JMenuItem(itemStr[4]);
    menuItems[3] = new JMenuItem(itemStr[5]);
    menuItems[4] = new JMenuItem(itemStr[8]);
    menuItems[5] = new JMenuItem(itemStr[9]);
    menuItems[6] = new JMenuItem(itemStr[10]);

    JMenu[] menus = new JMenu[4];
    menus[0] = new JMenu(itemStr[0]);
    menus[1] = new JMenu(itemStr[3]);
    menus[2] = new JMenu(itemStr[6]);
    menus[3] = new JMenu(itemStr[7]);

    menus[0].add(menuItems[0]);
    menus[0].add(menuItems[1]);
    menus[1].add(menuItems[2]);
    menus[1].add(menuItems[3]);
    menus[3].add(menuItems[4]);
    menus[3].add(menuItems[5]);
    menus[2].add(menus[3]);
    menus[2].add(menuItems[6]);

    JMenu menu = ComboMenuBar.createMenu(menuItems[0].getText());
    menu.add(menus[0]);
    menu.add(menus[1]);
    menu.addSeparator();
    menu.add(menus[2]);

    ComboMenuBar comboMenu = new ComboMenuBar(menu);

    JComboBox combo = new JComboBox();
    combo.addItem(itemStr[1]);
    combo.addItem(itemStr[2]);
    combo.addItem(itemStr[4]);
    combo.addItem(itemStr[5]);
    combo.addItem(itemStr[8]);
    combo.addItem(itemStr[9]);
    combo.addItem(itemStr[10]);

    getContentPane().setLayout(new FlowLayout());
    getContentPane().add(new ComboPanel("Fake ComboBox", comboMenu));
    getContentPane().add(new ComboPanel("ComboBox", combo));
  }

  class ComboPanel extends JPanel {
    ComboPanel(String title, JComponent c) {
      setLayout(new FlowLayout());
      setBorder(new TitledBorder(title));
      add(c);
    }
  }

  public static void main(String args[]) {
    try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception evt) {}
  
    ComboBoxMenuExample frame = new ComboBoxMenuExample();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setSize(370, 100);
    frame.setVisible(true);
  }
}

class ComboMenuBar extends JMenuBar {

  JMenu menu;

  Dimension preferredSize;

  public ComboMenuBar(JMenu menu) {
    this.menu = menu;

    Color color = UIManager.getColor("Menu.selectionBackground");
    UIManager.put("Menu.selectionBackground", UIManager
        .getColor("Menu.background"));
    menu.updateUI();
    UIManager.put("Menu.selectionBackground", color);

    MenuItemListener listener = new MenuItemListener();
    setListener(menu, listener);

    add(menu);
  }

  class MenuItemListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JMenuItem item = (JMenuItem) e.getSource();
      menu.setText(item.getText());
      menu.requestFocus();
    }
  }

  private void setListener(JMenuItem item, ActionListener listener) {
    if (item instanceof JMenu) {
      JMenu menu = (JMenu) item;
      int n = menu.getItemCount();
      for (int i = 0; i < n; i++) {
        setListener(menu.getItem(i), listener);
      }
    } else if (item != null) { // null means separator
      item.addActionListener(listener);
    }
  }

  public String getSelectedItem() {
    return menu.getText();
  }

  public void setPreferredSize(Dimension size) {
    preferredSize = size;
  }

  public Dimension getPreferredSize() {
    if (preferredSize == null) {
      Dimension sd = super.getPreferredSize();
      Dimension menuD = getItemSize(menu);
      Insets margin = menu.getMargin();
      Dimension retD = new Dimension(menuD.width, margin.top
          + margin.bottom + menuD.height);
      menu.setPreferredSize(retD);
      preferredSize = retD;
    }
    return preferredSize;
  }

  private Dimension getItemSize(JMenu menu) {
    Dimension d = new Dimension(0, 0);
    int n = menu.getItemCount();
    for (int i = 0; i < n; i++) {
      Dimension itemD;
      JMenuItem item = menu.getItem(i);
      if (item instanceof JMenu) {
        itemD = getItemSize((JMenu) item);
      } else if (item != null) {
        itemD = item.getPreferredSize();
      } else {
        itemD = new Dimension(0, 0); // separator
      }
      d.width = Math.max(d.width, itemD.width);
      d.height = Math.max(d.height, itemD.height);
    }
    return d;
  }

  public static class ComboMenu extends JMenu {
    ArrowIcon iconRenderer;

    public ComboMenu(String label) {
      super(label);
      iconRenderer = new ArrowIcon(SwingConstants.SOUTH, true);
      setBorder(new EtchedBorder());
      setIcon(new BlankIcon(null, 11));
      setHorizontalTextPosition(JButton.LEFT);
      setFocusPainted(true);
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Dimension d = this.getPreferredSize();
      int x = Math.max(0, d.width - iconRenderer.getIconWidth() - 3);
      int y = Math.max(0,
          (d.height - iconRenderer.getIconHeight()) / 2 - 2);
      iconRenderer.paintIcon(this, g, x, y);
    }
  }

  public static JMenu createMenu(String label) {
    return new ComboMenu(label);
  }

}

class ArrowIcon implements Icon, SwingConstants {
  private static final int DEFAULT_SIZE = 11;

  //private static final int DEFAULT_SIZE = 5;

  private int size;

  private int iconSize;

  private int direction;

  private boolean isEnabled;

  private BasicArrowButton iconRenderer;

  public ArrowIcon(int direction, boolean isPressedView) {
    this(DEFAULT_SIZE, direction, isPressedView);
  }

  public ArrowIcon(int iconSize, int direction, boolean isEnabled) {
    this.size = iconSize / 2;
    this.iconSize = iconSize;
    this.direction = direction;
    this.isEnabled = isEnabled;
    iconRenderer = new BasicArrowButton(direction);
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    iconRenderer.paintTriangle(g, x, y, size, direction, isEnabled);
  }

  public int getIconWidth() {
    //int retCode;
    switch (direction) {
    case NORTH:
    case SOUTH:
      return iconSize;
    case EAST:
    case WEST:
      return size;
    }
    return iconSize;
  }

  public int getIconHeight() {
    switch (direction) {
    case NORTH:
    case SOUTH:
      return size;
    case EAST:
    case WEST:
      return iconSize;
    }
    return size;
  }
}

class BlankIcon implements Icon {
  private Color fillColor;

  private int size;

  public BlankIcon() {
    this(null, 11);
  }

  public BlankIcon(Color color, int size) {
    //UIManager.getColor("control")
    //UIManager.getColor("controlShadow")
    fillColor = color;

    this.size = size;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
    if (fillColor != null) {
      g.setColor(fillColor);
      g.drawRect(x, y, size - 1, size - 1);
    }
  }

  public int getIconWidth() {
    return size;
  }

  public int getIconHeight() {
    return size;
  }
}
           
       
