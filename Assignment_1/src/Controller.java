import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPopupMenu;
import prefuse.controls.ControlAdapter;
import prefuse.controls.Control;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
public class Controller extends ControlAdapter implements Control {
public void itemClicked(VisualItem item, MouseEvent e)
{
if(item instanceof NodeItem)
{
String occupation = ((String) item.get("label"));
String age = (String) item.get("value");
JPopupMenu jpub = new JPopupMenu();
jpub.add("label: " + occupation);
jpub.add("value: " + age);
jpub.show(e.getComponent(),(int) e.getX(), (int)
e.getY());
}
}
}
