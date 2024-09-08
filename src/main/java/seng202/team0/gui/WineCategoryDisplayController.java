package seng202.team0.gui;

import com.sun.tools.javac.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import seng202.team0.models.Wine;
import seng202.team0.services.SearchWineService;
import seng202.team0.services.WineCategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class WineCategoryDisplayController {
    @FXML
    Text titleText;

    @FXML
    GridPane wineGrid;

    @FXML
    FontAwesomeIconView leftArrowButton;
    @FXML
    FontAwesomeIconView rightArrowButton;

    int MAXWINES = 10;

    ArrayList<Parent> wineDisplays;

    @FXML
    public void initialize()
    {
        ArrayList<Wine> displayWines = SearchWineService.getInstance().getWineList();
        wineDisplays = new ArrayList<>();
        titleText.setText(WineCategoryService.getInstance().getCategoryTitles().get(WineCategoryService.getInstance().getCurrentCategory()));
        for (int i = 0; i < Math.min(displayWines.size(), MAXWINES); i++) {
            SearchWineService.getInstance().setCurrentWine(displayWines.get(i));

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                wineDisplays.add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        displayWines();
    }

    @FXML
    public void setTitleText(String title)
    {
        titleText.setText(title);
    }

    @FXML
    public void displayWines()
    {
        wineGrid.getChildren().clear();

        for (int i = 0; i < Math.min(wineDisplays.size(), wineGrid.getColumnCount()); i++) {
            Parent parent = wineDisplays.get(i);

            wineGrid.add(parent, i, 0);
        }

    }

    @FXML
    public void leftClicked()
    {
        if (!wineDisplays.isEmpty()) {
            Parent wineDisplay = wineDisplays.getLast();
            wineDisplays.remove(wineDisplay);
            wineDisplays.addFirst(wineDisplay);

            displayWines();
        }
    }

    @FXML
    public void rightClicked()
    {
        if (!wineDisplays.isEmpty()) {
            Parent wineDisplay = wineDisplays.getFirst();
            wineDisplays.remove(wineDisplay);
            wineDisplays.addLast(wineDisplay);

            displayWines();
        }
    }
}
