/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testbasicfile;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Joey
 */


public class TestBasicFile {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        boolean done = false;
        String menu = "Menu\n1. File Specifications \n2. Backup File\n3. "
                + "Word Count\n4.Write file contents\n5.Append File contents to output file\n6. Display Contents\n7 :Search in file\n8. Exit";
        
        while (!done) {
            BasicFile basicFile = new BasicFile();
            String s = JOptionPane.showInputDialog(menu);
            
            try {
                int i = Integer.parseInt(s);
                switch (i) {
                    
                    case 1:
                        JOptionPane.showMessageDialog(null, "The name, path, and size of the file will be shown after it has been selected.",
                                "File Selection", JOptionPane.INFORMATION_MESSAGE);
                        basicFile.fileSelecting();
                        if (basicFile.exists()) {
                            if(basicFile.getExtension(basicFile.selectedFile.getName()).equals("txt")){
                            basicFile.isText=true;
                        }
                            displayInfo(basicFile.toString(), "File");
                        } else {
                            basicFile.fileSelecting();
                        }
                        break;
                    
                    case 2:
                        basicFile.fileSelecting();
                        if (basicFile.exists()) {
                            displayInfo("Operation successfull", "File");
                        } else {
                            basicFile.fileSelecting();
                        }
                        basicFile.fileBackup();
                        break;
                    
                    case 3:
                        basicFile.fileSelecting();
                        if (basicFile.exists() && basicFile.getExtension(basicFile.selectedFile.getName()).equals("txt")) {
                            displayInfo(basicFile.wordCount(), "Word Count");
                        } else {
                            displayInfo("THE FILE IS NOT A TEXT FILE", "File is not  a text file");
                            basicFile.fileSelecting();
                        }
                        break;
                    
                    case 4:
                        //write to output file
                        basicFile.fileSelecting();
                        System.out.println(basicFile.getExtension(basicFile.selectedFile.getName()).equals("txt"));
                        if (basicFile.exists() && basicFile.getExtension(basicFile.selectedFile.getName()).equals("txt")) {
                            basicFile.writeToOutput(basicFile.selectedFile);
                            displayInfo("Operation Successfull", "File contents were copied successfully");

                        } else {

                            displayInfo("THE FILE IS NOT A TEXT FILE", "File selected is not  a text file");
                            basicFile.fileSelecting();
                        }
                        break;
                    
                    case 5:
                        //append to output file
                        basicFile.fileSelecting();
                        System.out.println(basicFile.getExtension(basicFile.selectedFile.getName()).equals("txt"));
                        if (basicFile.exists() && basicFile.getExtension(basicFile.selectedFile.getName()).equals("txt")) {
                            try {
                                Files.write(Paths.get(basicFile.outputFile), basicFile.appendToOutPut(basicFile.selectedFile).getBytes(), StandardOpenOption.APPEND);
                            }catch (IOException e) {
                            e.printStackTrace();                        
                            }
                            displayInfo("Operation Successfull", "File contents were appended successfully");
                        }
                        else{
                            displayInfo("THE FILE IS NOT A TEXT FILE", "File selected is not  a text file");
                            basicFile.fileSelecting();
                        }
                        break;
                    
                    case 6:
                        basicFile.fileSelecting();
                        //display contents
                        if(basicFile.exists()&&basicFile.getExtension(basicFile.selectedFile.getName()).equals("txt")){
                            try{
                                JTextArea textArea = new JTextArea(basicFile.appendToOutPut(basicFile.selectedFile));
                                JScrollPane scrollPane = new JScrollPane(textArea);
                                textArea.setLineWrap(true);
                                textArea.setWrapStyleWord(true);
                                scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
                                JOptionPane.showMessageDialog(null, scrollPane, "dialog test with textarea",
                                        JOptionPane.YES_NO_OPTION);
//                                displayInfo("OPERATION SUCCESSFUL", "OPERATION SUCCESSFUL");

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else{
                            displayInfo("THE FILE IS NOT A TEXT FILE", "File selected is not  a text file");
                            basicFile.fileSelecting();
                        }
                        break;
                    case 7:
                        //Search
                       //Search searchc=new Search();
                        basicFile.fileSelecting();
                        if(basicFile.exists()&&basicFile.getExtension(basicFile.selectedFile.getName()).equals("txt")) {
                            String search = JOptionPane.showInputDialog("Enter Search Parameter");
                        //System.out.println(basicFile.searchWord(search,basicFile.selectedFile));
                            JTextArea textArea = null;
                            try {
                                textArea = new JTextArea(String.valueOf(basicFile.runAll(search)));
                                JScrollPane scrollPane = new JScrollPane(textArea);
                                textArea.setLineWrap(true);
                                textArea.setWrapStyleWord(true);
                                scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
                                JOptionPane.showMessageDialog(null, scrollPane, "dialog test with textarea",
                                        JOptionPane.YES_NO_OPTION);
                                displayInfo("OPERATION SUCCESSFUL", "OPERATION SUCCESSFUL");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                        }
                        else{
                            displayInfo("THE FILE IS NOT A TEXT FILE", "File selected is not  a text file");
                            basicFile.fileSelecting();
                        }
                            break;
                    case 8:
                        done = true;
                        break;
                    default:
                }
            } catch (NumberFormatException e) {
                System.exit(0);
            } catch (NullPointerException e) {
                System.exit(0);
            }

        }
    }


    static void displayInfo(String s, String info) {
        JOptionPane.showMessageDialog(null, s, info, JOptionPane.INFORMATION_MESSAGE);
    }
}




class BasicFile {
    File selectedFile;
    JFileChooser fileSelect;
    String extension = "",outputFile="Output.txt";
    File f2;
    File outPut=new File(outputFile);
boolean isText;
    BasicFile() {
        fileSelect = new JFileChooser(".");
    }

    
    
    
    private static String stripExtension(String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }

    
    
    
    public void fileSelecting() {
        int statusResult = fileSelect.showOpenDialog(null);
        try {
            if (statusResult != JFileChooser.APPROVE_OPTION) {
                throw new IOException();
            }
            selectedFile = fileSelect.getSelectedFile();
            if (!selectedFile.exists()) {
                throw new FileNotFoundException();
            } else {
                f2 = new File(".", stripExtension(selectedFile.getName()) + " File Backup." + getExtension(selectedFile.getName()));

            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No File Found ", "Error", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            System.exit(0);
        }
    }

    
    
    
    void fileBackup() throws FileNotFoundException {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(new FileInputStream(selectedFile));
            out = new DataOutputStream(new FileOutputStream(f2));
            try {
                while (true) {
                    byte data = in.readByte();
                    out.writeByte(data);
                }
            } catch (EOFException e) {
                JOptionPane.showMessageDialog(null, "File backup completed.",
                        "Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "File Not Found ",
                        "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                display(e.toString(), "Error");
            }
        }
    }

    boolean exists() {
        return selectedFile.exists();
    }

    public String toString() {
        int lines = 0;
        String contents;
if(isText){
    try {
       lines= readLines(selectedFile);
    } catch (IOException e) {
        e.printStackTrace();
    }
    contents="NAME: " + selectedFile.getName() + "\n" + "PATH: " + selectedFile.getAbsolutePath() + "\n" + "SIZE: " + selectedFile.length() / 1024.0 + " KiloBytes\n LINES: "+lines;

}else {
    contents = "NAME: " + selectedFile.getName() + "\n" + "PATH: " + selectedFile.getAbsolutePath() + "\n" + "SIZE: " + selectedFile.length() / 1024.0 + " KiloBytes";
}
    return contents;
    }

    
    
    
    private int readLines(File file) throws IOException {
        int linecount=0;            //Intializing linecount as zero
        FileReader fr=new FileReader(file);  //Creation of File Reader object
        BufferedReader br = new BufferedReader(fr);    //Creation of File Reader object
        String s;
        while((s=br.readLine())!=null)    //Reading Content from the file line by line
        {
            linecount++;               //For each line increment linecount by one

        }
        fr.close();
    return linecount;
    }

    
    
    
    public String searchWord(String parameter,File file) throws IOException {
        int linecount=1;
        StringBuilder line = null;
        //Intializing linecount as zero
        FileReader fr=new FileReader(file);  //Creation of File Reader object
        BufferedReader br = new BufferedReader(fr);    //Creation of File Reader object
        String s;
        String[] words;
        while((s=br.readLine())!=null)    //Reading Content from the file line by line
        {

            words=s.split(" ");  //Split the word using space
            for (String word : words)
            {
                if (word.equals(parameter))   //Search for the given word
                {
                    System.out.println(word);

                    line.append("\n ").append(linecount).append(" : ").append(s);
                }
            }

            linecount++;               //For each line increment linecount by one

        }
        fr.close();
        System.out.println(line.toString());
        return line.toString();
    }




    String wordCount() {
        try {
            int wordCount = 0,
                    numberCount = 0,
                    lineCount = 1,
                    characterCount = 0,
                    totalWords = 0;
            FileReader r = new FileReader(selectedFile);
            StreamTokenizer t = new StreamTokenizer(r);
            t.resetSyntax();
            t.whitespaceChars(0, ' ');
            t.wordChars('a', 'z');
            t.wordChars('A', 'Z');
            t.wordChars('0', '9');
            t.eolIsSignificant(true);
            while (t.nextToken() != StreamTokenizer.TT_EOF) {
                switch (t.ttype) {
                    case StreamTokenizer.TT_NUMBER:
                        numberCount++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        characterCount += t.sval.length();
                        wordCount++;
                        break;
                    case StreamTokenizer.TT_EOL:
                        lineCount++;
                        break;
                    case StreamTokenizer.TT_EOF:
                        break;
                    default:
                }
            }
            r.close();
            totalWords = numberCount + wordCount;
            return selectedFile.getName() + " has " + lineCount + " line(s), "
                    + totalWords + " word(s), and "
                    + characterCount + " characters. ";
        } catch (IOException e) {
            display(e.toString(), "Error");
        }
        return " ";
    }

    
    
    private void display(String msg, String s) {
        JOptionPane.showMessageDialog(null, msg, s, JOptionPane.ERROR_MESSAGE);
    }

    
    
    String getExtension(String fileName) {
        char ch;
        int len;
        if (fileName == null ||
                (len = fileName.length()) == 0 ||
                (ch = fileName.charAt(len - 1)) == '/' || ch == '\\' || //in the case of a directory
                ch == '.') //in the case of . or ..
            return "";
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if (dotInd <= sepInd) {
            return "";
        } else {
            extension = fileName.substring(dotInd + 1).toLowerCase();
            return extension;
        }
    }

    
    
    
    void writeToOutput(File srcf) {
        FileChannel src = null;
        try {
            src = new FileInputStream(srcf).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel dest = null;
        try {
            dest = new FileOutputStream(outPut).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dest.transferFrom(src, 0, src.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    String appendToOutPut(File srcf){
        String content = null;
        try {
             content= readFile(srcf.getAbsolutePath(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
   private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
   
   
   
    StringBuilder runAll(String input) throws Exception {
        StringBuilder stringBuilder=new StringBuilder();
        int linecount=1;

        BufferedReader freader = new BufferedReader(new FileReader(selectedFile));
        String s;
        BasicFile basicFile=new BasicFile();
        while((s = freader.readLine()) != null) {
            String[] st = s.split(" ");
            for (String string:st
            ) {
                if(string.equalsIgnoreCase(input)){
//                    System.out.println(s);
                    stringBuilder.append(linecount).append(":").append(s).append("\n");
                }
            }
            linecount++;
        }
        return stringBuilder;
    }

}
