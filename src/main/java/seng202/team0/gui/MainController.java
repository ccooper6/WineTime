package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import seng202.team0.models.Wine;
import seng202.team0.services.SearchWineService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the mainpage.fxml page
 * @author Caleb Cooper
 */
public class MainController {
    @FXML
    AnchorPane mainWine1;
    @FXML
    AnchorPane mainWine2;
    @FXML
    AnchorPane mainWine3;
    @FXML
    AnchorPane mainWine4;
    @FXML
    Label wineInfo1;
    @FXML
    Label wineInfo2;
    @FXML
    Label wineInfo3;
    @FXML
    Label wineInfo4;
    @FXML
    ImageView mainWineIcon1;
    @FXML
    ImageView mainWineIcon2;
    @FXML
    ImageView mainWineIcon3;
    @FXML
    ImageView mainWineIcon4;
    @FXML
    FontAwesomeIconView scrollArrow;

    ArrayList<Wine> wineList;

    /**
     * Class to format wine information to display on main page.
     * @param wine wine to get data of
     * @return formatted wine data string
     */
    public String getWineInfo(Wine wine) {
        return ("Name: %s\nVariety: %s\nDescription: %s".formatted(wine.getName(), wine.getVariety(), wine.getDescription()));
    }
    // ******************ENDS HERE********************

    /**
     * Initializes the controller.
     */
    public void initialize() {
        List<AnchorPane> wineViews = List.of(mainWine1, mainWine2, mainWine3, mainWine4);
        List<Label> wineInfos = List.of(wineInfo1, wineInfo2, wineInfo3, wineInfo4);
        List<ImageView> wineIcons = List.of(mainWineIcon1, mainWineIcon2, mainWineIcon3, mainWineIcon4);
        displayWines(wineViews, wineInfos, wineIcons);
        onRefresh(wineViews, wineInfos, wineIcons);
    }

    /**
     * Sets each anchor pane to include the wine data of the corresponding wine object.
     * @param wineView list of anchor panes
     * @param wineInfo list text boxes for the wine data
     * @param wineIcon list of wine icons corresponding to each wine
     */
    @FXML
    public void displayWines(List<AnchorPane> wineView, List<Label> wineInfo, List<ImageView> wineIcon) {
        SearchWineService.getInstance().searchWinesByName("Rainstorm");
        wineList = SearchWineService.getInstance().getWineList();
        if(wineList.size() >= wineView.size()) {
            for (int i = 0; i < wineView.size(); i++) {
                System.out.println(wineList.get(i).getImagePath());
                wineInfo.get(i).setText(getWineInfo(wineList.get(i)));
                wineIcon.get(i).setImage(new Image(wineList.get(i).getImagePath()));
            }
        } else {
            for (int i = wineList.size(); i < wineView.size(); i++) {
                wineInfo.get(i).setText("No wine available.");
                wineIcon.get(i).setImage(null);
            }
        }
    }

    /**
     * Refreshes the wine list in preparation to be displayed on the main page.
     * @param wineView list of anchor panes
     * @param wineInfo list text boxes for the wine data
     * @param wineIcon list of wine icons corresponding to each wine
     */
    @FXML
    public void onRefresh(List<AnchorPane> wineView, List<Label> wineInfo, List<ImageView> wineIcon) {
        scrollArrow.setOnMouseClicked(event -> {
            Wine firstWine = wineList.get(0);
            wineList.removeFirst();
            wineList.add(firstWine);
            displayWines(wineView, wineInfo, wineIcon);
        });
    }

    /**
     * Calls the initialize popup function from navigation controller to display the data of the
     * corresponding wine.
     * @param event the anchor pane of the wine that was clicked
     */
    @FXML
    public void onWineClicked(MouseEvent event) { // From advanced java fx tutorial
        AnchorPane pane = (AnchorPane) event.getSource();
        String[] name = pane.getId().split("");
        Integer paneNum = Integer.valueOf(name[8]);
        Wine wine = wineList.get(paneNum - 1);

        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.initPopUp(wine);
    }

    /**
     * Darkens the anchor pane to indicate the cursor is hovering.
     * @param event the anchor pane of the wine that was hovered over
     */
    @FXML
    public void darkenPane(MouseEvent event) {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-background-color: #999999; -fx-background-radius: 15");
    }

    /**
     * Lightens the anchor pane to indicate the cursor is no longer hovering.
     * @param event the anchor pane of the wine that was hovered over
     */
    @FXML
    public void lightenPane(MouseEvent event) {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-border-color: #d9d9d9; -fx-border-radius: 15");
    }
}
