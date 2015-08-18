// Thanks to Mr. Erwin 
//(http://edwin.baculsoft.com/2010/12/how-to-convert-pdf-files-to-images-using-java/),
// I'm able to make the pdf viewer working! :)

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer0;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFViewer;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
/**
 *
 * @author Eric O. Jonathan
 * Brief description:
 *  This is a tryout project using JavaFX to create an application explorer
 *  that preview files within the designated folder.
 */

public class FileExplorer0 extends Application {
    
    BorderPane border = new BorderPane();
    
    Path path = Paths.get("");
    String pathStr = path.toAbsolutePath().toString();
    
    String currentFolder = pathStr + "\\testFolder\\";
    
    void Execute() throws Exception
    {
        File file = new File(currentFolder + "\\hagakure.pdf");
        
        PDFViewer pdfv = new PDFViewer(true);
        pdfv.openFile(file);
        pdfv.setEnabling();
        pdfv.pack();
        pdfv.setVisible(true);
    }
    
    @Override
    public void start(Stage primaryStage) {             
        
        String os = System.getProperty("os.name");       
        
        Label top = new Label("Application Title Goes Here");
        top.fontProperty().set(Font.font("Calibri", 24.0F));
        
        Label bottom = new Label("Bottom");        
        
        List<Label> labels = Arrays.asList(bottom);
        
        border.setStyle("-fx-border-color: black;");
        
        border.setTop(top);
        BorderPane.setAlignment(top, Pos.CENTER);        
        
        String strRootItem = "Root Item";
        if(os.toLowerCase().contains("win")) strRootItem = "DesktopExplorerFiles";
        
        TreeItem<String> rootItem = new TreeItem<String>(strRootItem);
        rootItem.setExpanded(true);
        
        File[] files = new File(currentFolder).listFiles();
        
        for(File file : files) 
        {
            if(file.isDirectory())
            {
                TreeItem<String> item = new TreeItem<String>(file.getName());
                rootItem.getChildren().add(item);
            }
        }
        
        TreeView<String> tree = new TreeView<String>(rootItem);
        
        border.setLeft(tree);
	
        ArrayList<String> itemList = new ArrayList<>();
        
        for(File file : files)
        {
            if(file.isFile())
            {
                itemList.add(file.getName());
            }
        }
          
        ObservableList<String> ob = FXCollections.observableList(itemList);
	
        ListView<String> listView = new ListView<String>(ob);
        
	listView.setMinWidth(500);
        border.setCenter(listView);
        
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                String[] ext = newValue.split("\\.");
                
                if(ext[1].equals("txt") || 
                   ext[1].equals("py") ||
                   ext[1].equals("doc"))
                {
                    String content = null;
                    FileReader reader = null;
                    try {
                        File file = new File(currentFolder + newValue);
                        reader = new FileReader(file);
                        char[] chars = new char[(int) file.length()];
                        reader.read(chars);
                        content = new String(chars);
                        reader.close();
                        
                        Text text = new Text(content);   
                        TextFlow flow = new TextFlow(text);
                        flow.setMaxWidth(300);
                        ScrollPane pane = new ScrollPane(flow);
                        
                        BorderPane.setAlignment(pane, Pos.CENTER);
                        BorderPane.setMargin(pane, new Insets(5,5,5,5));
                        border.setRight(pane);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        
                    } 
                }
                else if(ext[1].equals("jpg") || 
                        ext[1].equals("jpeg") || 
                        ext[1].equals("png"))
                {
                    Image image = new Image("file:" + currentFolder + newValue, 300, 300, true, true);
                    ImageView view = new ImageView(image);
                    view.maxWidth(200);
                    
                    BorderPane.setMargin(view, new Insets(10));
                    border.setRight(view);
                    
                }
                else if(ext[1].equals("pdf")){
                    try {
                            File file = new File(currentFolder + newValue);
                                
                            RandomAccessFile raf = new RandomAccessFile(file, "r");
                            ReadableByteChannel ch = Channels.newChannel(new FileInputStream(file));
                            
                            FileChannel channel = raf.getChannel();
                            ByteBuffer buff = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                            PDFFile pdfFile = new PDFFile(buff);
                            
                            PDFPage page = pdfFile.getPage(1);
                            
                            Rectangle rect = new Rectangle(0, 0, 
                            (int) page.getBBox().getWidth(), 
                            (int) page.getBBox().getHeight());
                            
                            java.awt.Image image = page.getImage(rect.width, rect.height, rect, null, true, true);
                            
                            if (!(image instanceof RenderedImage)) {
                                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                                image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                                Graphics g = bufferedImage.createGraphics();
                                g.drawImage(image, 0, 0, null);
                                g.dispose();

                                image = bufferedImage;
                            }
                            
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            ImageIO.write((RenderedImage) image, "png", out);
                            out.flush();
                            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                    
                            Image imageFx = new Image(in);
                            ImageView view = new ImageView(imageFx);                            
                            view.maxWidth(200);
                    
                            BorderPane.setMargin(view, new Insets(10));
                            border.setRight(view);
                            
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }                  
                }
                else
                {
                    Label right = new Label("Right");   
                    right.setText(ext[1]);
                    border.setRight(right);
                    BorderPane.setAlignment(right, Pos.CENTER);
                }
            }
        });
        
        border.setBottom(bottom);
		
	for(Label l : labels){
            BorderPane.setAlignment(l, Pos.CENTER);
	}
        
        //Scene scene = new Scene(root, 300, 250);
        Scene scene = new Scene(border, 600, 500);
        primaryStage.setTitle(os);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
