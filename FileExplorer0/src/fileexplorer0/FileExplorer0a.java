/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer0;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;
/**
 *
 * @author Eric O. Jonathan
 */
public class FileExplorer0 extends Application {
    
    BorderPane border = new BorderPane();
    
    String directory = "C:\\Users\\ACER\\Documents\\DesktopExplorerFiles\\";
    
    @Override
    public void start(Stage primaryStage) {
        
        System.setProperty("jna.library.path", "C:\\Program Files\\gs\\gs9.16\\bin");
        
        String os = System.getProperty("os.name");       
        
        Label top = new Label("Application Title Goes Here");
        top.fontProperty().set(Font.font("Calibri", 24.0F));
        
        Label bottom = new Label("Bottom");        
        
        List<Label> labels = Arrays.asList(bottom);
        
        border.setStyle("-fx-border-color: black;");
        
        border.setTop(top);
        BorderPane.setAlignment(top, Pos.CENTER);                
        
        String strRootItem = "Root Item";
        
        //Jika linux, rubah root folder di sini.
        if(os.toLowerCase().contains("win")) strRootItem = "DesktopExplorerFiles";
        
        TreeItem<String> rootItem = new TreeItem<String>(strRootItem);
        rootItem.setExpanded(true);
        
        File[] files = new File(directory).listFiles();
        
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
                
                if(ext[1].equals("txt"))
                {
                    String content = null;
                    FileReader reader = null;
                    try {
                        File file = new File(directory + newValue);
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
                    Image image = new Image("file:" + directory + newValue, 300, 300, true, true);
                    ImageView view = new ImageView(image);
                    view.maxWidth(200);
                    
                    BorderPane.setMargin(view, new Insets(10));
                    border.setRight(view);
                    
                }
                else if(ext[1].equals("pdf")){
                    try {                                                    
                            File file = new File(directory + newValue);
                            
                            System.out.println(directory + newValue);
                            System.out.println(file.exists());
                                                                                           
                            try {
                            FileInputStream stream = new FileInputStream(file);
                        } catch (Exception e) {
                        }
                            
                            PDFDocument doc = new PDFDocument();                           
                            doc.load(file);                                                                             
                            
                            SimpleRenderer renderer = new SimpleRenderer();
                            
                            renderer.setResolution(100);                             
                            
                            List<java.awt.Image> images = renderer.render(doc, 0, 1);
                            java.awt.Image image = images.get(0);                            
                            Image fxImage = SwingFXUtils.toFXImage((BufferedImage) image, null);
                            
                            ImageView view = new ImageView(fxImage);
                            view.maxWidth(300);                            
                            border.setRight(view);                            
                        
                    } catch (Exception e) {
                        e.printStackTrace();
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
