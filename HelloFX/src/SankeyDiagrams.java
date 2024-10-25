import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import java.util.List;

public class SankeyDiagrams extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage firstStage) throws Exception {
        Stage stage = new Stage();
        VBox vBox = new VBox(10);

        Text text = new Text("Enter file path:");
        TextField textField = new TextField();
        textField.setPromptText("Enter file path here");
        textField.setText("D:\\cw3");

        Button openButton = new Button("Select the file");
        openButton.setOnAction(e -> {
            String userInput = textField.getText();
            if (!userInput.equals("D:\\cw3")) {
                // Print error message to console
                System.out.println("Invalid file path. Please enter 'D:\\cw3'");
            } else {
                stage.close();
                try {
                    showMainStage(firstStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().add(openButton);

        vBox.getChildren().addAll(text, textField, buttonContainer);

        Scene initialScene = new Scene(vBox, 300, 100);
        stage.setScene(initialScene);
        stage.setTitle("File Path");
        stage.show();
    }
        private void showMainStage(Stage firstStage) throws Exception {
        Pane layout = new Pane();
        FileRead fileRead = new FileRead();
        fileRead.readData();
        String[] names = fileRead.getListType().toArray(new String[0]);// get the listType
        List<String> dataList = fileRead.getListData();//put in to the list
        int[] values = new int[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            values[i] = Integer.parseInt(dataList.get(i));
        }
        Color[] colors = new Color[values.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] =Color.hsb(values[i]%360,1,1 );
        }
        Color[] CurveColor = new Color[values.length];
        for (int i = 0; i < CurveColor.length; i++) {
            CurveColor[i] = Color.hsb(values[i]%360,0.3,0.95);
        }
        Rectangle rectangle1 = new Rectangle();// set the left barblock
        rectangle1.setX(60);
        rectangle1.setY(175);
        rectangle1.setWidth(10);
        rectangle1.setHeight(250);
        rectangle1.setFill(Color.PURPLE);
        layout.getChildren().add(rectangle1);

        double rectXRatio = rectangle1.getX() / 600;
        double rectYRatio = rectangle1.getY() / 600;
        double rectWidthRatio = rectangle1.getWidth() / 600;
        double rectHeightRatio = rectangle1.getHeight() / 600;
        rectangle1.xProperty().bind(layout.widthProperty().multiply(rectXRatio));
        rectangle1.yProperty().bind(layout.heightProperty().multiply(rectYRatio));
        rectangle1.widthProperty().bind(layout.widthProperty().multiply(rectWidthRatio));
        rectangle1.heightProperty().bind(layout.heightProperty().multiply(rectHeightRatio));

        CubicCurve cubicCurve = new CubicCurve() ;
        cubicCurve.setStartX(0);
        cubicCurve.setStartY(0);
        cubicCurve.setControlX1(0);
        cubicCurve.setControlY1(0);
        cubicCurve.setControlX2(0);
        cubicCurve.setControlY2(0);
        cubicCurve.setEndX(0);
        cubicCurve.setEndY(0) ;
        cubicCurve.setStrokeWidth(2);
        cubicCurve.setStroke(Color.BLACK);
        cubicCurve.setFill(Color.TRANSPARENT);

        String listTitle = fileRead.getListTitle();
        DrawPane drawPane = new DrawPane(layout,fileRead, names, values, colors,CurveColor);
        layout.getChildren().add(drawPane);

        Scene scene = new Scene(layout, 600, 600);// set the pane
            firstStage.setTitle(listTitle); // set the title
            firstStage.setScene(scene);
            firstStage.show();
    }
    public class DrawPane extends Pane {
        private String[] names;
        private int[] values;
        private Color[] colors;
        private Color[] CurveColor;
        private Pane layout;
        private int firstDataInt;
        private FileRead fileRead;
        public DrawPane( Pane layout,FileRead fileRead,String[] names,int[] values,Color[] colors,Color[] CurveColor) {
            super();
            this.layout = layout;
            this.names = names;
            this.values = values;
            this.colors = colors;
            this.fileRead = fileRead;
            this.CurveColor = CurveColor;
            this.firstDataInt = Integer.parseInt(fileRead.getListData().get(0));

            layout.widthProperty().addListener((obs, oldVal, newVal) -> updateElements());
            layout.heightProperty().addListener((obs, oldVal, newVal) -> updateElements());
            updateElements();
        }
        private void updateElements() {
            this.getChildren().clear();
            String firstType = fileRead.getListType().get(0);
            String firstData = fileRead.getListData().get(0);
            int firstDataInt = Integer.parseInt(firstData);// get int data
            List<String> ListType = fileRead.getListType();//succeed two lists in the FileRead
            double text1XRatio = 5.0 / 600; // x position divide width of Pane
            double text1YRatio = 300.0 / 600; // y position divide height of Pane
            Text text1 = new Text(layout.getWidth() * text1XRatio, layout.getHeight() * text1YRatio,
                    firstType + ": \n" + firstData);// create text add into DrawPane
            this.getChildren().add(text1);
            double widthRatio = layout.getWidth() / 600;  // set the ratio of width with the width of pane
            double heightRatio = layout.getHeight() / 600;  //  set the ratio of height with the height of pane
            double totalHeight = 60.0 * heightRatio;
            double totalLeftHeight = 250 * heightRatio;
            double startY = 175.0 * heightRatio;
            double endY = 60 * heightRatio;
            for(int i = 1;i < ListType.size();i++){
                double barHeight = (double) values[i] / firstDataInt * totalLeftHeight;
                BarBlock barBlock = new BarBlock(450 * widthRatio, totalHeight, 10 * widthRatio, barHeight, colors[i]);//画block
                this.getChildren().add(barBlock);

                // calculate the middle of right rectangles
                double barCenterY = totalHeight + barHeight / 2;
                double startY1 = startY + barHeight;
                double endY1 = endY + barHeight;

                CurveDraw curveDraw = new CurveDraw(70 * widthRatio, startY,
                        175 * widthRatio, startY,
                        175 * widthRatio, endY,
                        450 * widthRatio, endY,
                        CurveColor[i], 2, Color.TRANSPARENT);
                this.getChildren().add(curveDraw);

                CurveDraw curveDraw1 = new CurveDraw(70 * widthRatio, startY1,
                        175 * widthRatio, startY1,
                        175 * widthRatio, endY1,
                        450 * widthRatio, endY1,
                        CurveColor[i], 2, Color.TRANSPARENT);
                this.getChildren().add(curveDraw1);

                startY += barHeight;
                endY += barHeight + 40 * heightRatio;

                // add text and in the middle
                Text text = new Text(470 * widthRatio, barCenterY, names[i] + ": \n" + values[i]);
                this.getChildren().add(text);
                // renew totalHeight for next barblock
                totalHeight += barHeight + 40 * heightRatio;

                // create a path for filling the color
                Path path = new Path();
                path.getElements().add(new MoveTo(curveDraw.getStartX(), curveDraw.getStartY()));
                path.getElements().add(new CubicCurveTo(curveDraw.getControlX1(), curveDraw.getControlY1(), curveDraw.getControlX2(),
                        curveDraw.getControlY2(), curveDraw.getEndX(), curveDraw.getEndY()));
                path.getElements().add(new LineTo(curveDraw1.getEndX(), curveDraw1.getEndY()));
                path.getElements().add(new CubicCurveTo(curveDraw1.getControlX2(), curveDraw1.getControlY2(), curveDraw1.getControlX1(),
                        curveDraw1.getControlY1(), curveDraw1.getStartX(), curveDraw1.getStartY()));
                path.getElements().add(new LineTo(curveDraw.getStartX(), curveDraw.getStartY()));
                path.setFill(CurveColor[i]);
                path.setStroke(Color.TRANSPARENT);
                this.getChildren().add(path);
            }
        }
    }
    static class BarBlock extends Rectangle{
        public BarBlock(double x,double y,double width,double height,Color colors){
            this.setX(x);
            this.setY(y);
            this.setWidth(width);
            this.setHeight(height);
            this.setFill(colors);
        }
    }
    static class CurveDraw extends CubicCurve{
        public CurveDraw(double x1,double y1,double cx1, double cy1,double cx2, double cy2,double ex, double ey,
                         Color CurveColor,int a,Color CurveColor1){
            this.setStartX(x1);
            this.setStartY(y1);
            this.setControlX1(cx1);
            this.setControlY1(cy1);
            this.setControlX2(cx2);
            this.setControlY2(cy2);
            this.setEndX(ex);
            this.setEndY(ey);
            this.setStroke(CurveColor);
            this.setStrokeWidth(a);
            this.setFill(CurveColor1);
        }
    }
}