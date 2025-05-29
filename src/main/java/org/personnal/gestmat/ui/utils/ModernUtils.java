package org.personnal.gestmat.ui.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ModernUtils {

    /**
     * Applique la feuille de style modernStyle.css à un nœud.
     */
   

    // HEADER
    public static HBox createHeader(String title, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("app-title");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("app-subtitle");

        VBox textBox = new VBox(titleLabel, subtitleLabel);
        textBox.setSpacing(4);

        HBox header = new HBox(textBox);
        header.getStyleClass().add("content-header");
        header.setPadding(new Insets(20));
        return header;
    }

    // FOOTER
    public static HBox createFooter(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: -fx-gray-500;");

        HBox footer = new HBox(label);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(12));
        footer.setStyle("-fx-background-color: -fx-gray-100;");
        return footer;
    }

    // SIDEBAR
    public static VBox createSidebar(Node... items) {
        VBox sidebar = new VBox(items);
        sidebar.getStyleClass().addAll("sidebar", "sidebar-scroll");
        sidebar.setPadding(new Insets(10));
        sidebar.setSpacing(8);
        return sidebar;
    }

    // NAVIGATION BUTTON
    public static Button createNavButton(String labelText) {
        Button btn = new Button(labelText);
        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    // PRIMARY BUTTON
    public static Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("primary-button");
        return btn;
    }

    // CARD
    public static VBox createCard(String title, String content) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        Label contentLabel = new Label(content);
        contentLabel.getStyleClass().add("section-description");

        VBox card = new VBox(titleLabel, contentLabel);
        card.getStyleClass().add("stat-card");
        card.setSpacing(10);
        return card;
    }

    // CONTENT AREA
    public static VBox createContentArea(Node... content) {
        VBox area = new VBox(content);
        area.getStyleClass().add("content-area");
        area.setSpacing(20);
        area.setPadding(new Insets(20));
        return area;
    }

    // SEPARATOR
    public static Separator createSeparator() {
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");
        return separator;
    }


    public static void loadCustomFont(String fontName, double size) {
    try {
        Font.loadFont(
            ModernUtils.class.getResource("/org/peronnal/gestman/ui/fonts/" + fontName).toExternalForm(),
            size
        );
    } catch (Exception e) {
        System.err.println("Erreur de chargement de la police : " + fontName);
    }

}

public static void initUI(Scene scene) {
    loadCustomFont("Poppins-Regular.ttf", 14); // Charger la police personnalisée
}


}
