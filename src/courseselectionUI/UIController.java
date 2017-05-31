/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionUI;
import courseselectionsystem.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author SilentLamb
 */
public class UIController  implements CourseSelectionSystem.EntrySelectionHandler,CourseSelectionSystem.MessageHandler{
    UserTypeSelectPage usertypeselectpage;
    CourseSelectionSystem.Entry entry = CourseSelectionSystem.Entry.unknown;
    public static Object obj = new Object();
   
    public UIController(){

        usertypeselectpage = new UserTypeSelectPage();
        usertypeselectpage.setVisible(false);
    }
    
    public static void wait_until_notified() {
        synchronized(obj) {
            try {
                obj.wait();
            } catch (InterruptedException ex) {
                System.exit(-1);
            }
        }
    }
    
    public static void wake_up() {
        synchronized(obj) {
            obj.notify();
        }
    }
    
    @Override
    public CourseSelectionSystem.Entry handle(){
        usertypeselectpage.setVisible(true);
        
        
        
        
        wait_until_notified();
        entry = usertypeselectpage.get_entry();

        //System.out.println("返回了");
        usertypeselectpage.setVisible(false);
        return entry;
        
    }
    
    @Override
    public void handle(String message){
        Object[] options = { "OK", "CANCEL" }; 
        JOptionPane.showOptionDialog(null, message, "Warning", 
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
            null, options, options[0]); 
    }
    
    
}
