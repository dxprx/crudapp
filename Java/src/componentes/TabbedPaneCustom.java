package componentes;

import java.awt.Color;
import javax.swing.JTabbedPane;

/**
 *
 * @author RAVEN
 */
public class TabbedPaneCustom extends JTabbedPane {

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        repaint();
    }
    
    public Color getUnselectedColor() {
        return unselectedColor;
    }

    public void setUnselectedColor(Color unselectedColor) {
        this.unselectedColor = unselectedColor;
        repaint();
    }

    private Color selectedColor = new Color(135, 32, 49, 150);
    private Color unselectedColor = new Color(253,188,60,150);

    public TabbedPaneCustom() {
        this.setBackground(new Color(250, 250, 250));
        this.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setUI(new TabbedPaneCustomUI(this));
    }
}
