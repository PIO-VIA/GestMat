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

/**
 * Classe utilitaire pour créer des composants d'interface moderne
 * Compatible avec le style Flutter/Gmail
 */
public class ModernUtils {

    // ===== MÉTHODES D'INITIALISATION =====

    /**
     * Initialise l'interface utilisateur avec gestion robuste des ressources
     */
    public static void initUI(Scene scene) {
        // Charger la police personnalisée (optionnel)
        loadCustomFont("Poppins-SemiBold.ttf", 14);

        // Appliquer des styles de base si le CSS n'est pas disponible
        applyFallbackStyles(scene);
    }

    /**
     * Charge une police personnalisée avec gestion d'erreur robuste
     */
    public static void loadCustomFont(String fontName, double size) {
        try {
            // Essayer plusieurs chemins possibles
            String[] possiblePaths = {
                    "/org/personnal/gestmat/ui/fonts/" + fontName,
                    "/fonts/" + fontName,
                    "/resources/fonts/" + fontName,
                    "/" + fontName
            };

            boolean fontLoaded = false;
            for (String path : possiblePaths) {
                try {
                    var fontUrl = ModernUtils.class.getResource(path);
                    if (fontUrl != null) {
                        Font.loadFont(fontUrl.toExternalForm(), size);
                        System.out.println("✅ Police chargée : " + path);
                        fontLoaded = true;
                        break;
                    }
                } catch (Exception e) {
                    // Continuer avec le chemin suivant
                }
            }

            if (!fontLoaded) {
                System.out.println("⚠️ Police " + fontName + " non trouvée - police système utilisée");
            }

        } catch (Exception e) {
            System.err.println("⚠️ Erreur chargement police " + fontName + " : " + e.getMessage());
        }
    }

    /**
     * Applique des styles CSS de base si le fichier principal n'est pas disponible
     */
    private static void applyFallbackStyles(Scene scene) {
        // Styles CSS de base intégrés comme fallback
        String fallbackCSS = """
            .root {
                -fx-font-family: 'Segoe UI', 'Roboto', Arial, sans-serif;
                -fx-font-size: 14px;
                -fx-background-color: #f8f9fa;
            }
            
            .primary-button {
                -fx-background-color: #1a73e8;
                -fx-text-fill: white;
                -fx-padding: 12 24;
                -fx-background-radius: 8;
                -fx-cursor: hand;
                -fx-font-weight: 500;
            }
            
            .primary-button:hover {
                -fx-background-color: #1557b0;
            }
            
            .nav-button {
                -fx-background-color: transparent;
                -fx-text-fill: #5f6368;
                -fx-padding: 12 20;
                -fx-alignment: center-left;
                -fx-background-radius: 12;
                -fx-cursor: hand;
                -fx-font-weight: 500;
            }
            
            .nav-button:hover {
                -fx-background-color: #f1f3f4;
            }
            
            .nav-button-selected {
                -fx-background-color: rgba(26, 115, 232, 0.1);
                -fx-text-fill: #1a73e8;
                -fx-font-weight: 600;
            }
            
            .sidebar {
                -fx-background-color: white;
                -fx-border-color: #e8eaed;
                -fx-border-width: 0 1 0 0;
                -fx-min-width: 260;
            }
            
            .content-area {
                -fx-background-color: #f8f9fa;
            }
            
            .stat-card {
                -fx-background-color: white;
                -fx-padding: 24;
                -fx-background-radius: 8;
                -fx-border-color: #e8eaed;
                -fx-border-width: 1;
                -fx-border-radius: 8;
            }
            
            .section-title {
                -fx-font-size: 20px;
                -fx-font-weight: 500;
                -fx-text-fill: #3c4043;
            }
            
            .section-description {
                -fx-text-fill: #5f6368;
                -fx-wrap-text: true;
                -fx-line-spacing: 2;
            }
            
            .text-field {
                -fx-background-color: white;
                -fx-border-color: #dadce0;
                -fx-border-width: 1;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                -fx-padding: 12 16;
            }
            
            .text-field:focused {
                -fx-border-color: #1a73e8;
                -fx-border-width: 2;
            }
            
            .user-label {
                -fx-background-color: rgba(26, 115, 232, 0.1);
                -fx-text-fill: #1a73e8;
                -fx-padding: 12 16;
                -fx-background-radius: 12;
                -fx-font-weight: 600;
            }
            """;

        // Appliquer les styles de base
        scene.getRoot().setStyle(fallbackCSS);
        System.out.println("✅ Styles CSS de base appliqués");
    }

    /**
     * Méthode utilitaire pour charger le CSS avec plusieurs tentatives
     */
    public static boolean loadCSS(Scene scene, String primaryPath) {
        String[] cssPaths = {
                primaryPath,
                "/org/personnal/gestmat/ui/styles/modernStyle.css",
                "/org/personnal/gestmat/styles/modernStyle.css",
                "/styles/modernStyle.css",
                "/modernStyle.css"
        };

        for (String path : cssPaths) {
            try {
                var cssUrl = ModernUtils.class.getResource(path);
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                    System.out.println("✅ CSS chargé : " + path);
                    return true;
                }
            } catch (Exception e) {
                // Continuer avec le chemin suivant
            }
        }

        System.out.println("⚠️ Aucun fichier CSS trouvé - styles de base utilisés");
        return false;
    }

    // ===== COMPOSANTS D'INTERFACE =====

    /**
     * Crée un header moderne
     */
    public static HBox createHeader(String title, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("app-title");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("app-subtitle");

        VBox textBox = new VBox(4);
        textBox.getChildren().addAll(titleLabel, subtitleLabel);

        HBox header = new HBox(textBox);
        header.getStyleClass().add("content-header");
        header.setPadding(new Insets(20, 32, 20, 32));
        return header;
    }

    /**
     * Crée un footer moderne
     */
    public static HBox createFooter(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("text-muted");

        HBox footer = new HBox(label);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(16));
        footer.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e8eaed; -fx-border-width: 1 0 0 0;");
        return footer;
    }

    /**
     * Crée une sidebar moderne
     */
    public static VBox createSidebar(Node... items) {
        VBox sidebar = new VBox(items);
        sidebar.getStyleClass().addAll("sidebar", "sidebar-scroll");
        sidebar.setPadding(new Insets(16, 8, 16, 8));
        sidebar.setSpacing(8);
        return sidebar;
    }

    /**
     * Crée un bouton de navigation moderne
     */
    public static Button createNavButton(String labelText) {
        Button btn = new Button(labelText);
        btn.getStyleClass().add("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    /**
     * Crée un bouton principal moderne
     */
    public static Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("primary-button");
        return btn;
    }

    /**
     * Crée un bouton secondaire moderne
     */
    public static Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("secondary-button");
        return btn;
    }

    /**
     * Crée une carte moderne avec titre et contenu
     */
    public static VBox createCard(String title, String content) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        Label contentLabel = new Label(content);
        contentLabel.getStyleClass().add("section-description");
        contentLabel.setWrapText(true);

        VBox card = new VBox(16);
        card.getStyleClass().add("stat-card");
        card.getChildren().addAll(titleLabel, contentLabel);

        return card;
    }

    /**
     * Crée une carte avec un nœud personnalisé comme contenu
     */
    public static VBox createCard(String title, Node content) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        VBox card = new VBox(16);
        card.getStyleClass().add("stat-card");
        card.getChildren().addAll(titleLabel, content);

        return card;
    }

    /**
     * Crée une zone de contenu principale
     */
    public static VBox createContentArea(Node... content) {
        VBox area = new VBox(content);
        area.getStyleClass().add("content-area");
        area.setSpacing(24);
        area.setPadding(new Insets(24));
        return area;
    }

    /**
     * Crée un séparateur moderne
     */
    public static Separator createSeparator() {
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");
        return separator;
    }

    // ===== UTILITAIRES SPÉCIALISÉS =====

    /**
     * Crée un conteneur de statistiques horizontales
     */
    public static HBox createStatsContainer(Node... stats) {
        HBox container = new HBox(16);
        container.getStyleClass().add("stats-container");
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(stats);
        return container;
    }

    /**
     * Crée une statistique rapide avec icône, valeur et label
     */
    public static VBox createQuickStat(String icon, String value, String label) {
        VBox stat = new VBox(8);
        stat.getStyleClass().add("quick-stats");
        stat.setAlignment(Pos.CENTER);
        stat.setPrefWidth(120);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-number");

        Label labelLabel = new Label(label);
        labelLabel.getStyleClass().add("stat-label");
        labelLabel.setWrapText(true);
        labelLabel.setMaxWidth(100);
        labelLabel.setAlignment(Pos.CENTER);

        stat.getChildren().addAll(iconLabel, valueLabel, labelLabel);
        return stat;
    }

    /**
     * Crée un conteneur avec titre et description
     */
    public static VBox createTitledContainer(String title, String description, Node... content) {
        VBox container = new VBox(16);

        if (title != null && !title.isEmpty()) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("section-title");
            container.getChildren().add(titleLabel);
        }

        if (description != null && !description.isEmpty()) {
            Label descLabel = new Label(description);
            descLabel.getStyleClass().add("section-description");
            descLabel.setWrapText(true);
            container.getChildren().add(descLabel);
        }

        container.getChildren().addAll(content);
        return container;
    }

    /**
     * Crée un bouton avec icône
     */
    public static Button createIconButton(String icon, String text, String styleClass) {
        Button button = new Button(icon + " " + text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    /**
     * Crée un label avec style spécifique
     */
    public static Label createStyledLabel(String text, String... styleClasses) {
        Label label = new Label(text);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    // ===== MÉTHODES UTILITAIRES =====

    /**
     * Applique une classe CSS à un nœud
     */
    public static void addStyleClass(Node node, String... styleClasses) {
        if (node != null && styleClasses != null) {
            node.getStyleClass().addAll(styleClasses);
        }
    }

    /**
     * Retire une classe CSS d'un nœud
     */
    public static void removeStyleClass(Node node, String... styleClasses) {
        if (node != null && styleClasses != null) {
            for (String styleClass : styleClasses) {
                node.getStyleClass().remove(styleClass);
            }
        }
    }

    /**
     * Active/désactive un nœud avec style approprié
     */
    public static void setDisabled(Node node, boolean disabled) {
        if (node != null) {
            node.setDisable(disabled);
            if (disabled) {
                addStyleClass(node, "disabled");
            } else {
                removeStyleClass(node, "disabled");
            }
        }
    }

    /**
     * Configure un espacement standard pour un conteneur
     */
    public static void setStandardSpacing(VBox container) {
        container.setSpacing(16);
        container.setPadding(new Insets(16));
    }

    /**
     * Configure un espacement large pour un conteneur
     */
    public static void setLargeSpacing(VBox container) {
        container.setSpacing(24);
        container.setPadding(new Insets(24));
    }
}