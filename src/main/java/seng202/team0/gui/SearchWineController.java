package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.models.Wine;
import seng202.team0.services.SearchWineService;

import java.io.IOException;
import java.util.ArrayList;

public class SearchWineController {
    private static final Logger log = LogManager.getLogger(SearchWineController.class);

    ArrayList<Wine> allWines;

    private final int MAXSIZE = 50;

    private int currentPage = 0;

//    scroll panel
    @FXML
    GridPane wineGrid;
    @FXML
    AnchorPane scrollAnchorPane;
    @FXML
    ScrollPane scrollPane;

//    page navigation
    @FXML
    FontAwesomeIconView startArrowButton;
    @FXML
    FontAwesomeIconView prevArrowButton;
    @FXML
    Button prevTextButton;
    @FXML
    Text pageCounterText;
    @FXML
    Button nextTextButton;
    @FXML
    FontAwesomeIconView nextArrowButton;
    @FXML
    FontAwesomeIconView endArrowButton;

    /**
     * Initialises the controller using wines from SearchWineService instance.
     */
    @FXML
    public void initialize() {

        allWines = SearchWineService.getInstance().getWineList();

//        System.out.println(allWines.size());
//        System.out.println(Math.ceilDiv(allWines.size() - 1, MAXSIZE));

        displayCurrentPage();
    }

    /**
     * Displays the current page of wines in a scrollable grid format
     */
    @FXML
    public void displayCurrentPage()
    {
        int start = currentPage * MAXSIZE;

        if (start < 0 || start >= allWines.size()) {
            log.error("Cannot display wines out of bounds.");
            return;
        }

        wineGrid.getChildren().clear();

        scrollPane.setVvalue(0);

        int columns = wineGrid.getColumnCount();

//        setup grid
        int end = Math.min(start + MAXSIZE, allWines.size());

        int gridRows = Math.ceilDiv(end - start, columns);
        wineGrid.setMinHeight(gridRows * 130 - 10);
        scrollAnchorPane.setMinHeight(gridRows * 130 - 10);

//        page navigation management at bottom
        pageCounterText.setText(currentPage + 1 + "/" + (Math.ceilDiv(allWines.size() - 1, MAXSIZE)));
        prevArrowButton.getParent().setVisible(start > 0);
        nextArrowButton.getParent().setVisible(end < allWines.size());


//        add wines
        for (int i = start; i < end; i++) {
             SearchWineService.getInstance().setCurrentWine(allWines.get(i));

             int currentRow = i / columns;
             int currentCol = i % columns;

            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));

                Parent parent = fxmlLoader.load();

                wineGrid.add(parent, currentCol, currentRow);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set current page to 0 and display the page
     */
    @FXML
    public void pageStart() {
        currentPage = 0;
        displayCurrentPage();
    }

    /**
     * Decrement the current page number and display the page
     */
    @FXML
    public void pagePrev() {
        currentPage--;
        displayCurrentPage();
    }

    /**
     * Increment the current page number and display the page
     */
    @FXML
    public void pageNext() {
        currentPage++;
        displayCurrentPage();
    }

    /**
     * Set current page to last page and display the page
     */
    @FXML
    public void pageEnd() {
        currentPage = Math.ceilDiv(allWines.size() - 1, MAXSIZE);
        displayCurrentPage();
    }


    /**
     * Functions below based off main page. Currently does not work
     */
    @FXML
    public void onWineClicked(MouseEvent event)
    { // From advanced java fx tutorial
        AnchorPane pane = (AnchorPane) event.getSource();
        String[] name = pane.getId().split("");
        Integer paneNum = Integer.valueOf(name[8]);
//        Wine wine = winesTest.get(paneNum - 1);
        Wine wine = allWines.getFirst();

        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.initPopUp(wine);
    }

    @FXML
    public void darkenPane(MouseEvent event)
    {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-background-color: #999999; -fx-background-radius: 15");
    }

    @FXML
    public void lightenPane(MouseEvent event)
    {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-border-color: #d9d9d9; -fx-border-radius: 15");
    }
}
