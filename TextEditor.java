import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

class TextEditor extends JFrame{
    private JTextArea area = new JTextArea(20, 120);
    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
    private String currentFile= "Untitled";
    private boolean changed = false; //boolean flag to check if text was edited

    //constructor
    public TextEditor(){
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll =
            new JScrollPane(
                    area,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            );
        add(scroll, BorderLayout.CENTER);

        //create a menu bar and add "File" and "Edit" drop down menus
        JMenuBar jmb = new JMenuBar();
        setJMenuBar(jmb);
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        jmb.add(file);
        jmb.add(edit);

        //add Actions to file and edit dropdown menu
        //New/Open/etc are defined later in the code
        //file.add(New);
        file.add(Open);
        file.add(Save);
        file.add(Quit);
        file.add(SaveAs);
        file.addSeparator();

        for(int i=0; i < 4; ++i){
            file.getItem(i).setIcon(null);
        }

        edit.add(Cut);
        edit.add(Copy);
        edit.add(Paste);

        edit.getItem(0).setText("Cut out");
        edit.getItem(1).setText("Copy");
        edit.getItem(2).setText("Paste");

        //positioning all the elements
        JToolBar tool = new JToolBar();
        add(tool, BorderLayout.NORTH);
        //tool.add(New);
        tool.add(Open);
        tool.add(Save);
        tool.addSeparator();

        JButton cut = tool.add(Cut), cpy = tool.add(Copy), pas = tool.add(Paste);
        cut.setText(null);
        cut.setIcon(new ImageIcon("cut.gif"));
        cpy.setText(null);
        cpy.setIcon(new ImageIcon("copy.gif"));
        pas.setText(null);
        pas.setIcon(new ImageIcon("paste.gif"));

        Save.setEnabled(false);
        SaveAs.setEnabled(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        area.addKeyListener(k1);
        setTitle(currentFile);
        setVisible(true);
    } //end constructor

    //listens for keypresses
    private KeyListener k1 = new KeyAdapter(){
        //when file is edited/changed, enable save and saveas
        public void keyPressed(KeyEvent e){
            changed = true;
            Save.setEnabled(true);
            SaveAs.setEnabled(true);
        }
    }; //note the ; at the end of listeners and actions

    Action Open = new AbstractAction("Open", new ImageIcon("open.gif")){
        public void actionPerformed(ActionEvent e){
            saveOld();
            if( dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                readInFile(dialog.getSelectedFile().getAbsolutePath());
            }
            SaveAs.setEnabled(true);
        }
    };

    Action Save = new AbstractAction("Save", new ImageIcon("save.gif")){
        public void actionPerformed(ActionEvent e){
            if(!currentFile.equals("Untitled")){
                saveFile(currentFile);
            } else {
                saveFileAs();
            }
        }
    };

    Action SaveAs = new AbstractAction("Save as..."){
        public void actionPerformed(ActionEvent e){
            saveFileAs();
        }
    };

    Action Quit = new AbstractAction("Quit"){
        public void actionPerformed(ActionEvent e){
            saveOld();
            System.exit(0);
        }
    };

    //map cut/copy/paste actions with the default system cut/copy/paste
    ActionMap m = area.getActionMap();
    Action Cut = m.get(DefaultEditorKit.cutAction);
    Action Copy = m.get(DefaultEditorKit.copyAction);
    Action Paste = m.get(DefaultEditorKit.pasteAction);


    //actual logic for save/open/etc functions
    private void saveFileAs(){
        if(dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            saveFile(dialog.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveOld(){
        if(changed){
            if(JOptionPane.showConfirmDialog
                    ( this,
                    "Would you like to save" + currentFile + " ?",
                    "Save",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                saveFile(currentFile);
            }
        }
    }

    private void readInFile(String fileName){
        try{
            FileReader fr = new FileReader(fileName);
            area.read(fr, null);
            fr.close();
            currentFile = fileName;
            setTitle(currentFile);
            changed = false;
        } catch(IOException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Editor can't fine file called " + fileName);
        }
    }

    private void saveFile(String fileName){
        try{
            FileWriter fw = new FileWriter(fileName);
            area.write(fw);
            fw.close();
            currentFile = fileName;
            setTitle(currentFile);
            changed = false;
            Save.setEnabled(false);
        } catch( IOException e ){
        }
    }






    //start program when run: 'java TextEditor'
    public  static void main(String[] arg) {
        new TextEditor();
    }
}
