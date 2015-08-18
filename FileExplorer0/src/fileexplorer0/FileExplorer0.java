// I'm still having problem creating a  PDF preview for this; however the others
// should be usable.

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileexplorer0;


import java.io.File;
import java.io.FileReader;
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
/**
 *
 * @author ACER
 */
public class FileExplorer0 extends Application {
    
    BorderPane border = new BorderPane();
    
    Path path = Paths.get("");
    String pathStr = path.toAbsolutePath().toString();
    
    String currentFolder = pathStr + "\\testFolder";
    
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
        
        //If os is windows, then the default folder will be in C:\Users\ACER\Documents\DesktopExplorerFiles
        
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
                
                if(ext[1].equals("txt"))
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
//                        text.maxWidth(200);
                        TextFlow flow = new TextFlow(text);
                        flow.setMaxWidth(300);
//                        FlowPane flow = new FlowPane(text);
//                        flow.maxWidth(250);
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
                            
//                            ImageView view = new ImageView(fxImage);
//                            //view.maxWidth(300);
//                            border.setRight(view);
                            
                        //File file = new File(directory + newValue);
                        //FileOutputStream fos = new FileOutputStream(file);
                        
//                        OutputStream stream = null;
//                        stream = new FileOutputStream(file);
//                        byte[] buffer = null;
//                        stream.write(buffer);
//                        ByteBuffer buf = ByteBuffer.wrap(buffer);                        
//                        PDFFile pdf = new PDFFile(buf);
                        
                        
                        
                    } catch (Exception e) {
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
