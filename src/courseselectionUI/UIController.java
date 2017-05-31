/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionUI;
import courseselectionsystem.*;
import javax.swing.JOptionPane;


/**
 *
 * @author SilentLamb
 */
public class UIController  implements CourseSelectionSystem.EntrySelectionHandler,CourseSelectionSystem.MessageHandler {
    UserTypeSelectPage usertypeselectpage;
    CourseSelectionSystem.Entry entry = CourseSelectionSystem.Entry.unknown;
   
    public UIController(){

        usertypeselectpage = new UserTypeSelectPage();
        usertypeselectpage.setVisible(false);
    }
    
    @Override
    public CourseSelectionSystem.Entry handle(){
        usertypeselectpage.setVisible(true);
        
        
        
        for (; ; ){
            new Thread(new Runnable(){
            @Override
            public void run(){
                //for (;;){
                    //System.out.println("查看有没有按下");
                    entry = usertypeselectpage.get_entry();
                    try{
                       Thread.sleep(10);
                    }catch(Exception e){
                        
                    }
               // }
            }
            
        }).start();
            //System.out.println("按下了没");
            if(entry !=  CourseSelectionSystem.Entry.unknown){
                
                //System.out.println("返回了");
                usertypeselectpage.setVisible(false);
                //usertypeselectpage.setVisible(false);
                return entry;
            }
        }
        
    }
    
    @Override
    public void handle(String message){
        Object[] options = { "OK", "CANCEL" }; 
        JOptionPane.showOptionDialog(null, message, "Warning", 
            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
            null, options, options[0]); 
    }
    
}
