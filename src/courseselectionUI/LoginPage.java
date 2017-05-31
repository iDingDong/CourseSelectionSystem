/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseselectionUI;
//import java.awt.event.*;

import courseselectionsystem.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
/**
 *
 * @author SilentLamb
 */
public class LoginPage extends javax.swing.JFrame {
    public static class StudentLoginPageController  implements StudentEntry.LoginHandler{
        LoginPage loginpage;
        
        @Override
        public StudentEntry.UserInfo handle(){
            loginpage.setVisible(true);
            loginpage.TxtUsername.setText("");
            loginpage.TxtUserpassword.setText("");
            UIController.wait_until_notified();
            StudentEntry.UserInfo result = new StudentEntry.UserInfo();
            result.id = loginpage.user_info.id;
            result.password = loginpage.user_info.password;
            loginpage.setVisible(false);
            loginpage.TxtUsername.setText("");
            loginpage.TxtUserpassword.setText("");
            return result;
        }
        public StudentLoginPageController(){
        
            loginpage = new LoginPage();
            loginpage.setVisible(false);
        }

        

        

        
    
    
}

    /**
     * Creates new form LoginPage
     */
    public LoginPage() {
        initComponents();
        String info1 = "请输入 [账号]";
 //       TxtUsername.addFocusListener(new MyFocusListener(info1, TxtUsername));
        TxtUsername.addCaretListener(new TextFieldInputListener());
    }
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        TxtUsername = new javax.swing.JTextField();
        MsgUsername = new javax.swing.JLabel();
        MsgPassword = new javax.swing.JLabel();
        TxtUserpassword = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(400, 300));

        TxtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtUsernameActionPerformed(evt);
            }
        });

        MsgUsername.setText("账号");

        MsgPassword.setText("密码");

        jButton1.setText("登录");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MsgUsername)
                    .addComponent(MsgPassword))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TxtUserpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MsgUsername))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MsgPassword)
                    .addComponent(TxtUserpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addComponent(jButton1)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        TxtUsername.getAccessibleContext().setAccessibleName("");
        TxtUserpassword.getAccessibleContext().setAccessibleName("TxtPassword");
        jButton1.getAccessibleContext().setAccessibleName("LoginSubmit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(416, 339));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (TxtUsername.getText().length() < 12){
            JOptionPane.showMessageDialog(this, "请输入12位有效账号", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(Arrays.toString(TxtUserpassword.getPassword()).length() > 16){
            JOptionPane.showMessageDialog(this, "请输入16位以内的有效密码", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            user_info.id = Long.valueOf(TxtUsername.getText());
            user_info.password = String.valueOf(TxtUserpassword.getPassword());
            UIController.wake_up();
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TxtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtUsernameActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MsgPassword;
    private javax.swing.JLabel MsgUsername;
    private javax.swing.JTextField TxtUsername;
    private javax.swing.JPasswordField TxtUserpassword;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    private final UserInfo user_info = new UserInfo();
    private class UserInfo{
        public long id;
        public String password;
    }
}
/*
class MyFocusListener implements FocusListener {
    String info;
    JTextField jtf;
    public MyFocusListener(String info, JTextField jtf) {
        this.info = info;
        this.jtf = jtf;
    }
    @Override
    public void focusGained(FocusEvent e) {//获得焦点的时候,清空提示文字
        String temp = jtf.getText();
        if(temp.equals(info)){
            jtf.setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e) {//失去焦点的时候,判断如果为空,就显示提示文字
        String temp = jtf.getText();
        if(temp.equals("")){
            jtf.setText(info);
        }
    }
}
*/

class TextFieldInputListener implements CaretListener {
    //JLabel msglabel;
    
    /*
    public TextFieldInputListener(JLabel label1){
        msglabel = label1;
        
    }
*/
 
    @Override
    public void caretUpdate(CaretEvent e) {
        JTextField textField = (JTextField) e.getSource(); // 获得触发事件的 JTextField
        String text = textField.getText();
        if (text.length() == 0) {
            return;
        }
        char ch = text.charAt(text.length() - 1);
        if (!(ch >= '0' && ch <= '9'  )) {
            
            JOptionPane.showMessageDialog(textField, "只能输入数字", "提示", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(() -> {
                // 去掉 JTextField 中的末尾字符
                textField.setText(text.substring(0, text.length() - 1));
            });
            
        }
        else if(text.length() > 12){
            JOptionPane.showMessageDialog(textField, "只能输入12位", "提示", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(() -> {
                // 去掉 JTextField 中的末尾字符
                textField.setText(text.substring(0, text.length() - 1));
            });
        }
    }
    
 
}
