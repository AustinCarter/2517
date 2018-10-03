import javax.swing.*;
import java.io.*;
/**
 * Write a description of class levelChooser here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class levelChooser extends JFrame
{
    String selectedName;
    boolean selected = false;
    public levelChooser()
    {
        JFileChooser chooser = new JFileChooser();
        File dir = new File("levels");
        chooser.setCurrentDirectory(dir);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        //int returnVal = chooser.showOpenDialog(this);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            selectedName = chooser.getSelectedFile().getName();
            selected = true;
        }
        else {}
            
    }
    
    public boolean fileSelected()
    {
        return selected;
    }
    
    public String selectedFile()
    {
        return selectedName;
    }
}