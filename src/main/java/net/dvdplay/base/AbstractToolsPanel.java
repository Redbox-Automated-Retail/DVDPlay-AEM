package net.dvdplay.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import net.dvdplay.aem.Aem;
import net.dvdplay.aem.AemFactory;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.models.BarCodeHash;
import net.dvdplay.models.ButtonStatusHash;
import net.dvdplay.models.PopupYesNo;
import net.dvdplay.models.TestConnectionHash;
import net.dvdplay.view.KeyboardAssembler;
import net.dvdplay.view.Utility;

public abstract class AbstractToolsPanel extends JPanel {
   protected Font labelFont = new Font("Arial", 0, 16);
   protected Border labelBorder = new LineBorder(Color.GRAY, 1, true);
   protected Font buttonFont = new Font("Arial", 0, 17);
   protected Border buttonBorder = new LineBorder(Color.GRAY, 2, true);
   protected Font boldButton = new Font("Arial", 1, 17);
   protected ArrayList buttonList = new ArrayList();
   protected FocusAdapter fa;
   protected JTextField focus;
   protected static int selectionStart = 0;
   protected KeyboardAssembler ka;
   protected Hashtable textFieldHash = new Hashtable();
   protected Hashtable buttonToBeDisable = new Hashtable();
   protected JLabel pictureLabel;
   protected ArrayList focusableTextField;
   protected int currentFocusIndex = 0;
   protected String command;
   protected String data;
   protected boolean textFieldFocus = true;
   protected AbstractTableModel atm;
   protected int focusRow;
   protected int focusCol;

   protected JLabel createLabel(String lText) {
      JLabel temp = new JLabel(lText);
      temp.setFont(this.labelFont);
      temp.setHorizontalAlignment(0);
      return temp;
   }

   protected JLabel createLabel(String lText, int location) {
      JLabel temp = new JLabel(lText);
      temp.setFont(this.labelFont);
      temp.setHorizontalAlignment(location);
      return temp;
   }

   protected JPanel createRow(String lName, int col, String specialName) {
      JPanel temp = new JPanel();
      JPanel labelPanel = new JPanel(new BorderLayout());
      JPanel fieldPanel = new JPanel(new BorderLayout());
      JLabel tmpLabel = new JLabel(lName, 4);
      tmpLabel.setFont(this.labelFont);
      tmpLabel.setBorder(this.labelBorder);
      tmpLabel.setPreferredSize(new Dimension(170, 20));
      labelPanel.add(tmpLabel, "East");
      JTextField tempField = new JTextField();
      tempField.setColumns(col);
      tempField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      this.textFieldHash.put(specialName, tempField);
      fieldPanel.add(tempField, "West");
      temp.setLayout(new BorderLayout(5, 10));
      temp.add(labelPanel, "West");
      temp.add(fieldPanel, "Center");
      tmpLabel.addFocusListener(this.fa);
      return temp;
   }

   protected JPanel createRow(String lName, int col, String specialName, String value, boolean editable, Font font) {
      JPanel temp = new JPanel();
      JPanel labelPanel = new JPanel(new BorderLayout());
      JPanel fieldPanel = new JPanel(new BorderLayout());
      JLabel tmpLabel = new JLabel(lName, 4);
      tmpLabel.setFont(font);
      tmpLabel.setBorder(this.labelBorder);
      tmpLabel.setPreferredSize(new Dimension(170, 20));
      labelPanel.add(tmpLabel, "East");
      JTextField tempField = new JTextField(value);
      tempField.setColumns(col);
      tempField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      tempField.setEditable(editable);
      this.textFieldHash.put(specialName, tempField);
      fieldPanel.add(tempField, "West");
      temp.setLayout(new BorderLayout(5, 10));
      temp.add(labelPanel, "West");
      temp.add(fieldPanel, "Center");
      tmpLabel.addFocusListener(this.fa);
      return temp;
   }

   protected JPanel createRow(String lName, int col, String specialName, String value, boolean editable) {
      JPanel temp = new JPanel();
      JPanel labelPanel = new JPanel(new BorderLayout());
      JPanel fieldPanel = new JPanel(new BorderLayout());
      JLabel tmpLabel = new JLabel(lName, 4);
      tmpLabel.setFont(this.labelFont);
      tmpLabel.setBorder(this.labelBorder);
      tmpLabel.setPreferredSize(new Dimension(170, 20));
      labelPanel.add(tmpLabel, "East");
      JTextField tempField = new JTextField(value);
      tempField.setColumns(col);
      tempField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      tempField.setEditable(editable);
      this.textFieldHash.put(specialName, tempField);
      fieldPanel.add(tempField, "West");
      temp.setLayout(new BorderLayout(5, 10));
      temp.add(labelPanel, "West");
      temp.add(fieldPanel, "Center");
      tmpLabel.addFocusListener(this.fa);
      return temp;
   }

   protected JPanel createRow(String lName, int col) {
      JPanel temp = new JPanel();
      JPanel labelPanel = new JPanel(new BorderLayout());
      JPanel fieldPanel = new JPanel(new BorderLayout());
      JLabel tmpLabel = new JLabel(lName, 4);
      tmpLabel.setFont(this.labelFont);
      tmpLabel.setBorder(this.labelBorder);
      tmpLabel.setPreferredSize(new Dimension(170, 20));
      labelPanel.add(tmpLabel, "East");
      JTextField tempField = new JTextField();
      tempField.setColumns(col);
      tempField.addFocusListener(new AbstractToolsPanel.FocusEventDemo());
      fieldPanel.add(tempField, "West");
      temp.setLayout(new BorderLayout(5, 10));
      temp.add(labelPanel, "West");
      temp.add(fieldPanel, "Center");
      tmpLabel.addFocusListener(this.fa);
      return temp;
   }

   protected JButton createButton(String lName, String function) {
      JButton test = new JButton(lName);
      test.setActionCommand(function);
      test.addActionListener(new AbstractToolsPanel.ActionTools());
      this.buttonList.add(test);
      return test;
   }

   protected JButton createButton(String lName, String function, ActionListener al) {
      JButton test = new JButton(lName);
      test.setActionCommand(function);
      test.addActionListener(al);
      this.buttonList.add(test);
      return test;
   }

   public void addActionListener(ActionListener l) {
      this.listenerList
         .add(
            ActionListener.class,
            l
         );

      for (int i = 0; i < this.buttonList.size(); i++) {
         JButton tmp = (JButton)this.buttonList.get(i);
         tmp.addActionListener(l);
      }
   }

   public void removeActionListener(ActionListener l) {
      this.listenerList
         .remove(
            ActionListener.class,
            l
         );
   }

   public void showDialog(String text) {
      JDialog d = new JDialog();
      d.setSize(500, 150);
      JLabel l = new JLabel(text);
      d.getContentPane().setLayout(new BorderLayout());
      d.getContentPane().add(l, "Center");
      JButton yes = new JButton("OK");
      yes.addActionListener(ae -> {
         d.setVisible(false);
         d.dispose();
      });
      JPanel p = new JPanel();
      p.add(yes);
      d.getContentPane().add(p, "South");
      d.setBounds(50, 250, 700, 150);
      d.setVisible(true);
   }

   public void showYesNoDialog(String text) {
      JDialog d = new JDialog();
      d.setSize(500, 150);
      JLabel l = new JLabel(text);
      d.getContentPane().setLayout(new BorderLayout());
      d.getContentPane().add(l, "Center");
      JButton yes = new JButton("Yes");
      yes.setActionCommand("Yes");
      JButton no = new JButton("No");
      no.setActionCommand("No");
      no.addActionListener(new Al(d));
      yes.addActionListener(new Al(d));
      JPanel p = new JPanel();
      p.add(yes);
      p.add(no);
      d.getContentPane().add(p, "South");
      d.setBounds(50, 250, 700, 150);
      d.setVisible(true);
   }

   public Object verifyEntry() {
      String data = "";
      Enumeration keys = this.textFieldHash.keys();

      while (keys.hasMoreElements()) {
         String key = (String)keys.nextElement();
         JTextField temp = (JTextField)this.textFieldHash.get(key);
         String tempStr = temp.getText();
         if (Utility.isEmpty(tempStr)) {
            this.showDialog("Please enter number in " + temp.getName());
            return false;
         }

         if (!Utility.isInt(tempStr)) {
            this.showDialog("Only number is allowed in " + temp.getName());
            return false;
         }

         int ttInt = Integer.parseInt(tempStr);
         data = data + key + ":" + ttInt + ",";
      }

      return data;
   }

   public Object verifyEntry(String entryNameCSV) {
      String data = "";
      StringTokenizer stk = new StringTokenizer(entryNameCSV, ",");

      while (stk.hasMoreTokens()) {
         String key = stk.nextToken();
         JTextField temp = (JTextField)this.textFieldHash.get(key);
         String tempStr = temp.getText();
         if (Utility.isEmpty(tempStr)) {
            this.showDialog("Please enter number in " + temp.getName());
            return false;
         }

         if (!Utility.isInt(tempStr)) {
            this.showDialog("Only number is allowed in " + temp.getName());
            return false;
         }

         int ttInt = Integer.parseInt(tempStr);
         data = data + key + ":" + ttInt + ",";
      }

      return data;
   }

   public Object verifyStringEntry() {
      String data = "";
      Enumeration keys = this.textFieldHash.keys();

      while (keys.hasMoreElements()) {
         String key = (String)keys.nextElement();
         JTextField temp = (JTextField)this.textFieldHash.get(key);
         String tempStr = temp.getText();
         if (Utility.isEmpty(tempStr)) {
            this.showDialog("Please enter something in " + temp.getName());
            return false;
         }

         data = data + key + ":" + tempStr + ",";
      }

      return data;
   }

   public Object verifyStringEntry(String entryNameCSV) {
      String data = "";
      StringTokenizer stk = new StringTokenizer(entryNameCSV, ",'");

      while (stk.hasMoreTokens()) {
         String key = stk.nextToken();
         JTextField temp = (JTextField)this.textFieldHash.get(key);
         String tempStr = temp.getText();
         if (Utility.isEmpty(tempStr)) {
            this.showDialog("Please enter something in " + temp.getName());
            return false;
         }

         data = data + key + ":" + tempStr + ",";
      }

      return data;
   }

   class Al implements ActionListener {
      ActionTools action;
      JComponent component;
      private final JDialog val$d;

      Al(JDialog val$d) {
         this.val$d = val$d;
         this.action = new ActionTools();
      }

      public void actionPerformed(ActionEvent ae) {
         Aem lAem = AemFactory.getInstance();
         if (ae.getActionCommand().equals("Yes")) {
            lAem.updateAllData(Aem.getLastSynchDate());
            this.val$d.setVisible(false);
            this.val$d.dispose();
         }
         if (ae.getActionCommand().equals("No")) {
            this.val$d.setVisible(false);
            this.val$d.dispose();
         }
      }
   }


   public class ActionTools implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         if (ae.getActionCommand().length() > 0) {
            StringTokenizer stk = new StringTokenizer(ae.getActionCommand(), " ");
            String data = new String();
            String model = stk.nextToken();
            String action = stk.nextToken();
            if (stk.hasMoreTokens()) {
               data = stk.nextToken();
            }

            Aem.logDetailMessage(DvdplayLevel.INFO, "Model : " + model + " Action : " + action + " Data : " + data);
            if (!model.equals("DoNothing")) {
               try {
                  model = "net.dvdplay.hardware." + model;
                  Class c = Class.forName(model);
                  Class[] args = new Class[]{Class.forName("java.lang.String")};
                  Method executeMethod = c.getMethod(action, args);
                  Class[] parameters = executeMethod.getParameterTypes();
                  StringTokenizer stk2 = new StringTokenizer(data, " ");
                  Object[] params = new Object[]{new String()};

                  for (int i = 0; stk2.hasMoreTokens(); i++) {
                     params[i] = stk2.nextToken();
                  }

                  Object result = executeMethod.invoke(c, params);

                  try {
                     if (!(result instanceof String)) {
                        if (result instanceof ButtonStatusHash) {
                           ButtonStatusHash bsh = (ButtonStatusHash)result;
                           Enumeration keys = bsh.keys();

                           while (keys.hasMoreElements()) {
                              String key = (String)keys.nextElement();
                              String value = (String)bsh.get(key);
                              JButton button = (JButton)AbstractToolsPanel.this.buttonToBeDisable.get(key);
                              button.setEnabled(Boolean.valueOf(value));
                           }
                        } else if (result instanceof BarCodeHash) {
                           BarCodeHash bch = (BarCodeHash)result;
                           Enumeration keys = bch.keys();

                           while (keys.hasMoreElements()) {
                              String key = (String)keys.nextElement();
                              String value = (String)bch.get(key);
                              JTextField textField = (JTextField)AbstractToolsPanel.this.textFieldHash.get(key);
                              textField.setText(value);
                           }
                        } else if (result instanceof TestConnectionHash) {
                           TestConnectionHash tch = (TestConnectionHash)result;
                           Enumeration keys = tch.keys();

                           while (keys.hasMoreElements()) {
                              String key = (String)keys.nextElement();
                              String value = (String)tch.get(key);
                              AbstractToolsPanel.this.showDialog("Connection to " + key + " is " + value + ".\n");
                           }
                        } else if (result instanceof PopupYesNo) {
                           PopupYesNo pyn = (PopupYesNo)result;
                           AbstractToolsPanel.this.showYesNoDialog(pyn.getMessage());
                        } else if (result instanceof Hashtable) {
                           Hashtable ht = (Hashtable)result;
                           Enumeration keys = ht.keys();

                           while (keys.hasMoreElements()) {
                              String key = (String)keys.nextElement();
                              String value = (String)ht.get(key);
                              JTextField field = (JTextField)AbstractToolsPanel.this.textFieldHash.get(key);
                              field.setText(value);
                           }
                        }
                     }
                  } catch (ClassCastException var19) {
                     Aem.logDetailMessage(DvdplayLevel.ERROR, "[Tools] " + var19.toString(), var19);
                  }
               } catch (ClassNotFoundException var20) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[Tools] " + var20.toString(), var20);
               } catch (NoSuchMethodException var21) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[Tools] " + var21.toString(), var21);
               } catch (IllegalAccessException var22) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[Tools] " + var22.toString(), var22);
               } catch (InvocationTargetException var23) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[Tools] " + var23.toString(), var23);
               } catch (IllegalArgumentException var24) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[Tools] " + var24.toString(), var24);
               }
            }
         }
      }
   }

   public class FocusEventDemo implements FocusListener {
      public void focusLost(FocusEvent fe) {
      }

      public void focusGained(FocusEvent fe) {
         AbstractToolsPanel.this.focus = (JTextField)fe.getSource();
         AbstractToolsPanel.selectionStart = AbstractToolsPanel.this.focus.getSelectionStart();
         AbstractToolsPanel.this.textFieldFocus = true;
      }
   }

   public class KeyTools implements ActionListener {
      public void actionPerformed(ActionEvent ae) {
         String action = ae.getActionCommand();

         try {
            if (action == "SHIFT") {
               AbstractToolsPanel.this.ka.shift();
               AbstractToolsPanel.this.focus.grabFocus();
            } else if (action.equals("TAB")) {
               if (AbstractToolsPanel.this.textFieldFocus) {
                  AbstractToolsPanel.this.currentFocusIndex++;
                  if (AbstractToolsPanel.this.currentFocusIndex == AbstractToolsPanel.this.focusableTextField.size()) {
                     AbstractToolsPanel.this.currentFocusIndex = 0;
                  }

                  String name = (String)AbstractToolsPanel.this.focusableTextField.get(AbstractToolsPanel.this.currentFocusIndex);
                  AbstractToolsPanel.this.focus = (JTextField)AbstractToolsPanel.this.textFieldHash.get(name);
                  AbstractToolsPanel.this.focus.grabFocus();
               } else {
                  AbstractToolsPanel.this.focusRow++;
               }
            } else if (action == "BACK") {
               try {
                  if (AbstractToolsPanel.this.textFieldFocus) {
                     String start = AbstractToolsPanel.this.focus.getText(0, AbstractToolsPanel.selectionStart > 0 ? AbstractToolsPanel.selectionStart - 1 : 0);
                     String end = AbstractToolsPanel.this.focus
                        .getText(
                           AbstractToolsPanel.selectionStart,
                           AbstractToolsPanel.this.focus.getText().length() > 0
                              ? AbstractToolsPanel.this.focus.getText().length() - AbstractToolsPanel.selectionStart
                              : 0
                        );
                     AbstractToolsPanel.this.focus.setText(start + end);
                     AbstractToolsPanel.selectionStart--;
                     AbstractToolsPanel.this.focus.grabFocus();
                  } else {
                     String orgValue = (String)AbstractToolsPanel.this.atm.getValueAt(AbstractToolsPanel.this.focusRow, AbstractToolsPanel.this.focusCol);
                     orgValue = orgValue.substring(0, orgValue.length() - 1);
                     AbstractToolsPanel.this.atm.setValueAt(new String(orgValue), AbstractToolsPanel.this.focusRow, AbstractToolsPanel.this.focusCol);
                     AbstractToolsPanel.this.atm.fireTableDataChanged();
                  }
               } catch (BadLocationException var10) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[AbstractToolsPanel]" + var10.toString());
               }
            } else if (action == "SPACE") {
               try {
                  if (AbstractToolsPanel.this.textFieldFocus) {
                     String start = AbstractToolsPanel.this.focus.getText(0, AbstractToolsPanel.selectionStart > 0 ? AbstractToolsPanel.selectionStart : 0);
                     String end = AbstractToolsPanel.this.focus
                        .getText(
                           AbstractToolsPanel.selectionStart,
                           AbstractToolsPanel.this.focus.getText().length() > 0
                              ? AbstractToolsPanel.this.focus.getText().length() - AbstractToolsPanel.selectionStart
                              : 0
                        );
                     AbstractToolsPanel.this.focus.setText(start + " " + end);
                     AbstractToolsPanel.selectionStart++;
                     AbstractToolsPanel.this.focus.grabFocus();
                  } else {
                     String orgValue = (String)AbstractToolsPanel.this.atm.getValueAt(AbstractToolsPanel.this.focusRow, AbstractToolsPanel.this.focusCol);
                     AbstractToolsPanel.this.atm.setValueAt(new String(orgValue + " "), AbstractToolsPanel.this.focusRow, AbstractToolsPanel.this.focusCol);
                     AbstractToolsPanel.this.atm.fireTableDataChanged();
                  }
               } catch (BadLocationException var9) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[AbstractToolsPanel]" + var9.toString());
               }
            } else if (action == "<-") {
               AbstractToolsPanel.selectionStart--;
            } else if (action == "->") {
               AbstractToolsPanel.selectionStart++;
            } else if (action == "START") {
               try {
                  Aem.startButton();
               } catch (Exception var8) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[AbstractToolsPanel] " + var8.toString());
               }
            } else if (action == "ENTER") {
               try {
                  Robot r = new Robot();
                  r.keyPress(10);
                  r.keyRelease(10);
               } catch (Exception var7) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[AbstractToolsPanel] " + var7.toString());
               }
            } else if (action == "Ctl-Alt-Del") {
               try {
                  Runtime.getRuntime().exec("C:\\windows\\system32\\taskmgr.exe");
               } catch (Exception var6) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[AbstractToolsPanel] " + var6.toString());
               }
            } else {
               try {
                  if (AbstractToolsPanel.this.textFieldFocus) {
                     String start = AbstractToolsPanel.this.focus.getText(0, AbstractToolsPanel.selectionStart > 0 ? AbstractToolsPanel.selectionStart : 0);
                     String end = AbstractToolsPanel.this.focus
                        .getText(
                           AbstractToolsPanel.selectionStart,
                           AbstractToolsPanel.this.focus.getText().length() > 0
                              ? AbstractToolsPanel.this.focus.getText().length() - AbstractToolsPanel.selectionStart
                              : 0
                        );
                     AbstractToolsPanel.this.focus.setText(start + action + end);
                     AbstractToolsPanel.selectionStart++;
                  } else {
                     String orgValue = (String)AbstractToolsPanel.this.atm.getValueAt(AbstractToolsPanel.this.focusRow, AbstractToolsPanel.this.focusCol);
                     AbstractToolsPanel.this.atm.setValueAt(new String(orgValue + action), AbstractToolsPanel.this.focusRow, AbstractToolsPanel.this.focusCol);
                     AbstractToolsPanel.this.atm.fireTableDataChanged();
                  }
               } catch (BadLocationException var5) {
                  Aem.logDetailMessage(DvdplayLevel.ERROR, "[AbstractToolsPanel]" + var5.toString());
               }
            }
         } catch (Exception var11) {
         }
      }
   }

   public class VolumeTools implements ChangeListener {
      public void stateChanged(ChangeEvent ce) {
         JSlider source = (JSlider)ce.getSource();
         Aem.setVolume(source.getValue());
      }
   }
}
