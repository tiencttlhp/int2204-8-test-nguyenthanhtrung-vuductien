/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import com.sun.glass.events.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import com.sun.speech.freetts.VoiceManager;
import java.util.ArrayList;

/**
 *
 * @author TienLuong
 */
public class Application extends javax.swing.JFrame {
    
    private ArrayList<Word> ListWords;
    private int LengthList;

    DefaultListModel mode11;
    private static int indexArrList;    // vị trí trong mảng
    private static int indexModel;      // vị trí trong model hiện tại
    
    Diction list = new Diction();
    
    public Application() throws FileNotFoundException {
        mode11 = new DefaultListModel();
        indexArrList = -1; // vị trí từ đang được chọn hiện tại
        indexModel = -1; 
        
        ListWords = list.getListwords();
        LengthList = list.getLength();
        
        initComponents();
        this.setLocationRelativeTo(null);
        nghiaHienThi.setContentType("text/html");
        insertFromFile("E_V1.txt");
    }
    
    // load từ mới từ file
    public void insertFromFile(String st) throws FileNotFoundException{
        File inFile = new File(st);
        try (FileReader fileReader = new FileReader(inFile)) {
            BufferedReader reader = new BufferedReader(fileReader);
        
            String line;
            while( (line = reader.readLine())  != null )
            {
                String target = line.substring(0,line.indexOf("<html>"));
                String explain = line.substring(line.indexOf("<html>"));
                Word newWord = new Word(target,explain);
                ListWords.add(newWord);
                LengthList ++;      // tăng số lượng từ
            }
            showAllWord();  // hiển thị từ mới
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // ghi ra file
    public void dictionaryExportToFile(){
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter("E_V1.txt");
            bw = new BufferedWriter(fw);
            for (int i=0; i< LengthList ; i++) 
                bw.write(ListWords.get(i).getSPELLING() + "\n"
                    + ListWords.get(i).getEXPLAIN() + "\n");
        } catch (IOException e) {
        } finally {
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException e) {
            }
        }
    }
    
    
    // hiển thị toàn bộ từ
    public void showAllWord(){
        mode11.removeAllElements();
        for (int i=0; i < LengthList; i++){
            mode11.addElement(ListWords.get(i).getSPELLING());
        }
        this.ListWordsJList.setModel(mode11);
    }
    
    // hiển thị các từ gần giống vs từ cần tìm
    public void dictionaryShow(String st){
        mode11.removeAllElements();
        for(int i=0; i < LengthList ; i++){
            if (ListWords.get(i).getSPELLING().toUpperCase().indexOf(st.toUpperCase()) == 0 ){
                mode11.addElement(ListWords.get(i).getSPELLING());
            }
        }
        ListWordsJList.setModel(mode11);
    }
    
    // tìm từ và in ra màn hình nghĩa của từ
    public void dictionaryLookup(String st){
        indexArrList = ListWords.indexOf(new Word(st,""));

        if (indexArrList != -1){
            nghiaHienThi.setText(ListWords.get(indexArrList).getEXPLAIN());
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
            indexModel = 0;
        }
        else{
            indexArrList = -1; // khong co tu
            JOptionPane.showMessageDialog(this, "Không tìm thấy từ cần tìm!");
        }
    }
    
    // thêm từ
    public void addWord(String st1, String st2){
        st2 = "<html>" + st2 + "<html>";
        ListWords.add(new Word(st1, st2)); // thêm từ
        indexArrList = LengthList;                 // vào cuối danh sách
        
        // thêm vào model để hiện nghĩa
        mode11.addElement(ListWords.get(indexArrList).getSPELLING());
        this.ListWordsJList.setModel(mode11);
        
        LengthList ++;      // tăng độ dài
        
        inputText.setText(st1);
        nghiaHienThi.setText(st2);
    
        ListWordsJList.setSelectedIndex(indexArrList);
        ListWordsJList.ensureIndexIsVisible(indexArrList);
    }
    
    // xóa 1 từ
    public void deleteWord(){
        ListWords.remove(indexArrList);       // xoa vi tri trong listWords
        mode11.remove(indexModel);       // xoa vi tri trong model
        this.ListWordsJList.setModel(mode11);
        LengthList --;
        ListWordsJList.setSelectedIndex(indexModel); // chọn vị trí trong list
        ListWordsJList.ensureIndexIsVisible(indexModel);  // lăn thanh cuộn đến vị trí đang chọn
        //deleteButton.setEnabled(false);
        //editButton.setEnabled(false);
    }
    
    // sửa chữa từ
    public void editWords(String st1, String st2){
        st2 = "<html>" + st2 + "<html>";
        ListWords.set(indexArrList,new Word(st1, st2));
        nghiaHienThi.setText(ListWords.get(indexArrList).getEXPLAIN());  // in nghĩa mới được sửa
        mode11.setElementAt(ListWords.get(indexArrList).getSPELLING(), indexModel);
        this.ListWordsJList.setModel(mode11);
        ListWordsJList.setSelectedIndex(indexModel);      // chọn từ mới được sửa
        ListWordsJList.ensureIndexIsVisible(indexModel);      // cuộn tự động đến từ đó
        //deleteButton.setEnabled(false);  // khóa 2 nút delete và edit
        //editButton.setEnabled(false);
    }
    
    // phát âm từ vựng
    public static void speech(String text) {
        VoiceManager voiceManager = VoiceManager.getInstance();
        com.sun.speech.freetts.Voice syntheticVoice = voiceManager.getVoice("kevin16");
        syntheticVoice.allocate();
        syntheticVoice.speak(text);
        syntheticVoice.deallocate();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        timTuButton = new javax.swing.JButton();
        inputText = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        ListWordsJList = new javax.swing.JList<>();
        speed = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        nghiaHienThi = new javax.swing.JEditorPane();
        backgound = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        SaveMenu = new javax.swing.JMenuItem();
        exitMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        jScrollPane2.setViewportView(jTree1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dictionary");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(153, 255, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 51, 51)));
        jPanel1.setOpaque(false);

        addButton.setBackground(new java.awt.Color(255, 153, 0));
        addButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        addButton.setForeground(new java.awt.Color(0, 0, 255));
        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Button-Add-icon (1).png"))); // NOI18N
        addButton.setText("Thêm");
        addButton.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 0, 153)));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        editButton.setBackground(new java.awt.Color(255, 153, 0));
        editButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        editButton.setForeground(new java.awt.Color(0, 0, 255));
        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/IconEdit.png"))); // NOI18N
        editButton.setText("Sửa");
        editButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
        editButton.setEnabled(false);
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        deleteButton.setBackground(new java.awt.Color(255, 153, 0));
        deleteButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(0, 0, 255));
        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actions-edit-delete-icon (1).png"))); // NOI18N
        deleteButton.setText("Xóa");
        deleteButton.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 0, 102)));
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(51, 51, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập từ cần tra cứu", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(255, 255, 0));

        timTuButton.setBackground(new java.awt.Color(102, 255, 204));
        timTuButton.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        timTuButton.setForeground(new java.awt.Color(255, 0, 0));
        timTuButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search (1).png"))); // NOI18N
        timTuButton.setText("Tra cứu");
        timTuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timTuButtonActionPerformed(evt);
            }
        });

        inputText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputTextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputText, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timTuButton))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(inputText, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timTuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        ListWordsJList.setBackground(new java.awt.Color(204, 204, 255));
        ListWordsJList.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 255, 0)), "Chọn từ trong danh sách", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(204, 0, 0))); // NOI18N
        ListWordsJList.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        ListWordsJList.setAutoscrolls(false);
        ListWordsJList.setSelectionBackground(new java.awt.Color(255, 51, 51));
        ListWordsJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ListWordsJListMouseReleased(evt);
            }
        });
        ListWordsJList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ListWordsJListKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(ListWordsJList);

        speed.setBackground(new java.awt.Color(255, 255, 255));
        speed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/speaker1.gif"))); // NOI18N
        speed.setAutoscrolls(true);
        speed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                speedActionPerformed(evt);
            }
        });

        nghiaHienThi.setBackground(new java.awt.Color(204, 204, 204));
        nghiaHienThi.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)), "Nghĩa của từ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 3, 18), new java.awt.Color(0, 0, 102))); // NOI18N
        nghiaHienThi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(nghiaHienThi);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(speed, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(110, 110, 110)
                                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(79, 79, 79)
                                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)))))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addComponent(speed, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(addButton)
                                    .addComponent(editButton)
                                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel1, gridBagConstraints);

        backgound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bk.jpg"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(backgound, gridBagConstraints);

        jMenuBar1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));

        jMenu1.setText("File");

        SaveMenu.setText("Save");
        SaveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveMenuActionPerformed(evt);
            }
        });
        jMenu1.add(SaveMenu);

        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenu);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // sự kiện click chuột nút Tra Cứu
    private void timTuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timTuButtonActionPerformed
        
        String gm = inputText.getText().trim();
        if (!gm.equals("")) {   //nếu không rỗng thì tìm kiếm
            dictionaryLookup(gm);
            ListWordsJList.setSelectedIndex(indexModel);
            ListWordsJList.ensureIndexIsVisible(indexModel); 
        }
    }//GEN-LAST:event_timTuButtonActionPerformed

    private void inputTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextKeyReleased
        // TODO add your handling code here: KHI 1 KEY ĐƯỢC NHẢ RA
        String st = inputText.getText().trim();
        
            nghiaHienThi.setText("\0");
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        if (!st.equals("")){
            dictionaryShow(st);  //tìm các từ gần giống
            if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                dictionaryLookup(st);   // tìm từ
                //ListWordsJList.setSelectedIndex(indexModel);
                //ListWordsJList.ensureIndexIsVisible(indexModel); 
            }
        }
        else showAllWord();
    }//GEN-LAST:event_inputTextKeyReleased

    // khi ấn enter trong List từ
    private void ListWordsJListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ListWordsJListKeyReleased
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){      // nếu ấn nút enter
                String gm = ListWordsJList.getSelectedValue().trim();
                inputText.setText(gm);
                dictionaryLookup(gm);
                indexModel = ListWordsJList.getSelectedIndex();
            }
    }//GEN-LAST:event_ListWordsJListKeyReleased

    // NÚT thêm từ được click
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        AddWordApp ap = new AddWordApp();  // tạo 1 frame thêm từ
        ap.setVisible(true);
        ap.getButton().addActionListener((java.awt.event.ActionEvent evt1) -> {
            if (!ap.getTu().equals("")  && !ap.getNghia().equals("")){
                int k = ListWords.indexOf(new Word(ap.getTu(), ap.getNghia()));
                
                if (k != -1){
                        JOptionPane.showMessageDialog(this, "Đã có từ này trong danh sách",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ap.dispose();       // đóng Frame
                        addWord(ap.getTu(), ap.getNghia()); // thêm từ
                    }
            } else {
                JOptionPane.showMessageDialog(this, "Không để trống TỪ và NGHĨA",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }//GEN-LAST:event_addButtonActionPerformed

    // NÚT DELETE được click
    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int click = JOptionPane.showConfirmDialog(this,"Bạn có chắc chắn muốn xóa", 
                "CẢNH BÁO", JOptionPane.YES_NO_OPTION);
        if (click == JOptionPane.YES_OPTION){
            deleteWord();
        }
            
    }//GEN-LAST:event_deleteButtonActionPerformed

    // nút sửa chữa từ được gọi
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        EditWordApp ap = new EditWordApp();
        ap.setVisible(true);
        
        ap.setTu(ListWords.get(indexArrList).getSPELLING()); // viết lại từ đó
        ap.setNghia(ListWords.get(indexArrList).getEXPLAIN()); // viết lại nghĩa từ đó trong Frame mới
        
        ap.getButton().addActionListener((java.awt.event.ActionEvent evt1) -> {
            if(!ap.getTu().equals("") && !ap.getNghia().equals("")){
                int k = ListWords.indexOf(new Word(ap.getTu(), ap.getNghia()));    //loockup từ
                if (k == -1){
                    ap.dispose();       // đóng cửa sổ
                    editWords(ap.getTu(), ap.getNghia());  // gọi hàm sửa chữa
                } else {
                    JOptionPane.showMessageDialog(this, "Đã có từ này trong danh sách",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            } else{
                JOptionPane.showMessageDialog(this, "Không để trống TỪ và NGHĨA",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });
    }//GEN-LAST:event_editButtonActionPerformed

    // phát âm
    private void speedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_speedActionPerformed
        String st = inputText.getText();
        speech(st);
    }//GEN-LAST:event_speedActionPerformed

    // thoát chương trình bằng File/Exit
    private void exitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuActionPerformed

    private void SaveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveMenuActionPerformed
        // TODO add your handling code here:
        dictionaryExportToFile();
    }//GEN-LAST:event_SaveMenuActionPerformed

    private void ListWordsJListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ListWordsJListMouseReleased
        // TODO add your handling code here:
        String gm = ListWordsJList.getSelectedValue();
        inputText.setText(gm);
        dictionaryLookup(gm);  // tìm kiếm từ
        indexModel = ListWordsJList.getSelectedIndex();
    }//GEN-LAST:event_ListWordsJListMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> ListWordsJList;
    private javax.swing.JMenuItem SaveMenu;
    private javax.swing.JButton addButton;
    private javax.swing.JLabel backgound;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JTextField inputText;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTree jTree1;
    private javax.swing.JEditorPane nghiaHienThi;
    private javax.swing.JButton speed;
    private javax.swing.JButton timTuButton;
    // End of variables declaration//GEN-END:variables

}