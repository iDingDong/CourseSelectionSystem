/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionUI;
import courseselectionsystem.*;


/**
 *
 * @author SilentLamb
 */
public class UIController  implements CourseSelectionSystem.EntrySelectionHandler {
    UserTypeSelectPage usertypeselectpage;

   
    public UIController(){

        usertypeselectpage = new UserTypeSelectPage();
        usertypeselectpage.setVisible(false);
    }
    
    @Override
    public CourseSelectionSystem.Entry handle(){
        usertypeselectpage.setVisible(true);
        return usertypeselectpage.get_entry();
    }
    
}
