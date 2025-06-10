package org.personnal.gestmat.ui;

import org.personnal.gestmat.ui.controllers.HomeUIController;
import org.personnal.gestmat.ui.controllers.HomeUIController.ViewType;
import org.personnal.gestmat.ui.utils.ModernUtils;
import org.personnal.gestmat.ui.utils.ModernNotificationUtils;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Interface principale de l'application avec architecture MVC
 * Système de gestion des réservations avec rôles Enseignant/Responsable
 */
public class HomeUI extends Application implements
        HomeUIController.AuthenticationListener,
        HomeUIController.ViewChangeListener {

    // ===== CONTRÔLEUR MVC =====
    private HomeUIController controller;

    // ===== COMPOSANTS INTERFACE =====
    private BorderPane mainLayout;
    private VBox sidebar;
    private ScrollPane contentScrollPane;
    private VBox contentArea;

    // Boutons de navigation
    private Button btnPlanning;
    private Button btnReservationSalle;
    private Button btnReservationMateriel;
    private Button btnRecapHoraire;
    private Button btnGestionEnseignants;
    private Button btnConnexion;
    private Button btnDeconnexion;

    // Labels d'information utilisateur
    private Label userLabel;
    private Label roleLabel;

    // Contrôles de planning
    private Node planningControls;
    private VBox planningContent;

    // Bouton actuellement sélectionné
    private Button selectedButton;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        // Initialiser le contrôleur MVC
        controller = new HomeUIController();
        controller.addAuthenticationListener(this);
        controller.addViewChangeListener(this);

        // Construire l'interface
        buildInterface();

        // Configurer la scène
        Scene scene = new Scene(mainLayout, 1200, 800);
        setupStyles(scene);

        // Configurer la fenêtre
        stage.setTitle("Gestion des Réservations - Université");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();

        // Afficher la vue par défaut
        showDashboard();
    }

    // ===== CONSTRUCTION DE L'INTERFACE =====

    /**
     * Construit l'interface principale
     */
    private void buildInterface() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-container");

        // Créer la sidebar
        createSidebar();

        // Créer la zone de contenu avec scroll
        createContentArea();

        // Assembler le layout
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentScrollPane);
    }

    /**
     * Crée la barre latérale de navigation
     */
    private void createSidebar() {
        sidebar = new VBox();
        sidebar.getStyleClass().addAll("sidebar");
        sidebar.setSpacing(0); // Pas d'espacement global
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(250);
        sidebar.setMaxWidth(250);

        // Initialiser les composants
        initializeSidebarComponents();

        // Mettre à jour la sidebar selon l'état de connexion
        updateSidebar();
    }

    /**
     * Initialise les composants de la sidebar
     */
    private void initializeSidebarComponents() {
        // Labels utilisateur
        userLabel = new Label();
        userLabel.getStyleClass().add("user-label");
        userLabel.setMaxWidth(Double.MAX_VALUE);
        userLabel.setAlignment(Pos.CENTER);

        roleLabel = new Label();
        roleLabel.getStyleClass().add("role-label");
        roleLabel.setMaxWidth(Double.MAX_VALUE);
        roleLabel.setAlignment(Pos.CENTER);

        // Logo/icône en haut
        Label logoIcon = new Label("🏢");
        logoIcon.getStyleClass().add("logo-icon");
        logoIcon.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        logoIcon.setAlignment(Pos.CENTER);
        logoIcon.setMaxWidth(Double.MAX_VALUE);

        // Boutons de navigation - SANS ICÔNES comme dans l'image
        btnPlanning = createNavButton("PLANNING", () -> controller.changeView(ViewType.PLANNING));
        btnReservationSalle = createNavButton("RÉSERVER SALLE", () -> controller.changeView(ViewType.RESERVATION_SALLE));
        btnReservationMateriel = createNavButton("RÉSERVER MATÉRIEL", () -> controller.changeView(ViewType.RESERVATION_MATERIEL));
        btnRecapHoraire = createNavButton("RÉCAP HORAIRE", () -> controller.changeView(ViewType.RECAP_HORAIRE));
        btnGestionEnseignants = createNavButton("GESTION ENSEIGNANTS", () -> controller.changeView(ViewType.GESTION_ENSEIGNANTS));

        // Boutons d'authentification
        btnConnexion = createAuthButton("SE CONNECTER", this::showLoginForm);
        btnDeconnexion = createAuthButton("SE DÉCONNECTER", this::logout);

        // Définir le bouton Planning comme sélectionné par défaut
        setSelectedButton(btnPlanning);
    }


    /**
     * Crée un bouton de navigation
     */
    private Button createNavButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);

        button.setOnAction(e -> {
            setSelectedButton(button);
            action.run();
        });

        return button;
    }

    /**
     * Crée un bouton d'authentification
     */
    private Button createAuthButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("nav-button-auth");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * Définit le bouton sélectionné avec le style approprié
     */
    private void setSelectedButton(Button button) {
        // Retirer la classe de sélection de l'ancien bouton
        if (selectedButton != null) {
            selectedButton.getStyleClass().remove("nav-button-selected");
        }

        // Ajouter la classe au nouveau bouton
        selectedButton = button;
        if (selectedButton != null) {
            selectedButton.getStyleClass().add("nav-button-selected");
        }
    }

    /**
     * Met à jour la sidebar selon l'état de connexion
     */
    private void updateSidebar() {
        sidebar.getChildren().clear();

        // Logo section en haut
        VBox logoSection = new VBox();
        logoSection.getStyleClass().add("logo-section");
        logoSection.setAlignment(Pos.CENTER);

        Label logoIcon = new Label("🏢");
        logoIcon.getStyleClass().add("logo-icon");
        logoIcon.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        logoIcon.setAlignment(Pos.CENTER);
        logoIcon.setMaxWidth(60);
        logoIcon.setMaxHeight(60);
        logoIcon.setMinWidth(60);
        logoIcon.setMinHeight(60);

        logoSection.getChildren().add(logoIcon);

        // Spacer pour pousser le bouton de connexion vers le bas
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        if (controller.isLoggedIn()) {
            // Utilisateur connecté
            var user = controller.getCurrentUser();
            userLabel.setText(user.getFullName());
            roleLabel.setText(user.getRole().toString());

            sidebar.getChildren().addAll(
                    logoSection,
                    userLabel,
                    roleLabel,
                    createSeparator(),
                    btnPlanning,
                    btnReservationSalle,
                    btnReservationMateriel,
                    btnRecapHoraire
            );

            // Ajouter gestion enseignants pour les responsables
            if (user.isResponsable()) {
                sidebar.getChildren().add(btnGestionEnseignants);
            }

            sidebar.getChildren().addAll(spacer, btnDeconnexion);

        } else {
            // Utilisateur non connecté
            sidebar.getChildren().addAll(
                    logoSection,
                    createSeparator(),
                    btnPlanning,
                    spacer,
                    createSeparator(),
                    btnConnexion
            );
        }
    }

    /**
     * Crée la zone de contenu avec défilement
     */
    private void createContentArea() {
        contentArea = new VBox();
        contentArea.getStyleClass().add("content-area");
        contentArea.setSpacing(32);
        contentArea.setPadding(new Insets(32));

        // ScrollPane pour le contenu
        contentScrollPane = new ScrollPane(contentArea);
        contentScrollPane.getStyleClass().add("content-scroll");
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setFitToHeight(true);
        contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    /**
     * Crée un séparateur stylisé
     */
    private Node createSeparator() {
        Region separator = new Region();
        separator.getStyleClass().add("separator");
        separator.setPrefHeight(1);
        separator.setMaxHeight(1);
        separator.setStyle("-fx-background-color: var(--outline);");
        return separator;
    }

    /**
     * Affiche le tableau de bord
     */
    private void showDashboard() {
        VBox dashboard = ModernUtils.createCard("🏠 Tableau de bord", controller.getDashboardContent());

        if (controller.isLoggedIn()) {
            // Ajouter les statistiques pour les utilisateurs connectés
            HBox statsContainer = createStatsContainer();

            VBox content = new VBox(24);
            content.getChildren().addAll(dashboard, statsContainer);

            setContentArea(content);
        } else {
            setContentArea(dashboard);
        }
    }

    /**
     * Affiche la vue planning avec contrôles et cartes modernes
     */
    private void showPlanningView() {
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Créer la carte d'en-tête du planning
        VBox planningHeader = ModernUtils.createCard("📅 Planning des Salles", controller.getConsultationContent());

        // Créer les contrôles de planning (ComboBox)
        planningControls = controller.createPlanningControls(this::updatePlanningContent);

        // Initialiser le conteneur de planning
        planningContent = new VBox();
        updatePlanningContent(); // Mise à jour initiale

        // Assembler la vue
        mainContainer.getChildren().addAll(planningHeader, planningControls, planningContent);

        setContentArea(mainContainer);
    }

    /**
     * Crée les contrôles de planning avec ComboBoxes côte à côte
     */
    private Node createPlanningControls(Runnable updateCallback) {
        HBox controls = new HBox(30);
        controls.getStyleClass().add("planning-controls");
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(20));

        // Section Salle
        HBox salleSection = new HBox(10);
        salleSection.setAlignment(Pos.CENTER_LEFT);

        Label salleLabel = new Label("Salle:");
        salleLabel.getStyleClass().add("combo-label");

        ComboBox<HomeUIController.Salle> salleComboBox = new ComboBox<>();
        salleComboBox.getStyleClass().add("combo-box-modern");
        salleComboBox.setPromptText("Sélectionner une salle");
        salleComboBox.getItems().addAll(controller.getSalles());
        salleComboBox.setPrefWidth(250);

        salleSection.getChildren().addAll(salleLabel, salleComboBox);

        // Section Semaine
        HBox semaineSection = new HBox(10);
        semaineSection.setAlignment(Pos.CENTER_LEFT);

        Label semaineLabel = new Label("Semaine");
        semaineLabel.getStyleClass().add("combo-label");

        ComboBox<HomeUIController.Semaine> semaineComboBox = new ComboBox<>();
        semaineComboBox.getStyleClass().add("combo-box-modern");
        semaineComboBox.setPromptText("Sélectionner une semaine");
        semaineComboBox.getItems().addAll(controller.getSemaines());
        semaineComboBox.setPrefWidth(250);

        // Définir la semaine actuelle par défaut
        if (!controller.getSemaines().isEmpty()) {
            semaineComboBox.setValue(controller.getSemaines().get(0));
        }

        semaineSection.getChildren().addAll(semaineLabel, semaineComboBox);

        // Ajouter des listeners pour mettre à jour le contenu
        salleComboBox.setOnAction(e -> updateCallback.run());
        semaineComboBox.setOnAction(e -> updateCallback.run());

        // Stocker les références pour pouvoir les récupérer
        controls.getProperties().put("salleCombo", salleComboBox);
        controls.getProperties().put("semaineCombo", semaineComboBox);

        controls.getChildren().addAll(salleSection, semaineSection);

        return controls;
    }


    /**
     * Met à jour le contenu du planning avec les nouvelles cartes
     */
    private void updatePlanningContent() {
        if (planningControls != null && planningContent != null) {
            var salle = controller.getSelectedSalle(planningControls);
            var semaine = controller.getSelectedSemaine(planningControls);

            // Créer les nouvelles cartes de planning
            VBox newPlanningCards = controller.createPlanningCards(salle, semaine);

            // Remplacer le contenu
            planningContent.getChildren().clear();
            planningContent.getChildren().addAll(newPlanningCards.getChildren());
        }
    }

    /**
     * Affiche la vue réservation de salle
     */
    private void showReservationSalleView() {
        String content = "🏢 Réservation de Salle\n\n" +
                "Fonctionnalités disponibles :\n" +
                "• Recherche de salles par capacité et équipements\n" +
                "• Visualisation de la disponibilité en temps réel\n" +
                "• Réservation directe avec confirmation\n" +
                "• Gestion des récurrences\n\n" +
                "⚙️ Interface de réservation à implémenter...";

        VBox reservationView = ModernUtils.createCard("Réservation de Salle", content);
        setContentArea(reservationView);
    }

    /**
     * Affiche la vue réservation de matériel
     */
    private void showReservationMaterielView() {
        String content = "💻 Réservation de Matériel\n\n" +
                "Matériels disponibles :\n" +
                "• Vidéoprojecteurs portables\n" +
                "• Ordinateurs portables\n" +
                "• Équipements audio/vidéo\n" +
                "• Matériel de laboratoire\n\n" +
                "⚙️ Interface de réservation à implémenter...";

        VBox reservationView = ModernUtils.createCard("Réservation de Matériel", content);
        setContentArea(reservationView);
    }

    /**
     * Affiche la vue récap horaire
     */
    private void showRecapHoraireView() {
        var user = controller.getCurrentUser();
        String content = "📊 Récapitulatif Horaire\n\n" +
                "Planning personnel de " + user.getFullName() + "\n\n";

        if (user.isResponsable()) {
            content += "🎯 Mode Responsable :\n" +
                    "• Visualisation de votre planning\n" +
                    "• Modification des plannings des enseignants\n" +
                    "• Génération de rapports horaires\n" +
                    "• Export vers les systèmes RH\n\n";
        } else {
            content += "👨‍🏫 Mode Enseignant :\n" +
                    "• Consultation de votre planning hebdomadaire\n" +
                    "• Historique des cours dispensés\n" +
                    "• Calcul automatique des heures\n" +
                    "• Export personnel\n\n";
        }

        content += "⚙️ Interface de gestion horaire à implémenter...";

        VBox recapView = ModernUtils.createCard("Récap Horaire", content);
        setContentArea(recapView);
    }

    /**
     * Affiche la vue gestion des enseignants (responsables uniquement)
     */
    private void showGestionEnseignantsView() {
        String content = "👥 Gestion des Enseignants\n\n" +
                "🔧 Fonctionnalités Responsable :\n" +
                "• Ajout de nouveaux enseignants\n" +
                "• Modification des profils existants\n" +
                "• Attribution des droits d'accès\n" +
                "• Gestion des plannings individuels\n" +
                "• Supervision des réservations\n\n" +
                "📊 Enseignants actuels : " + controller.getEnseignants().size() + "\n\n" +
                "⚙️ Interface de gestion à implémenter...";

        VBox gestionView = ModernUtils.createCard("Gestion des Enseignants", content);
        setContentArea(gestionView);
    }

    /**
     * Crée le conteneur de statistiques
     */
    private HBox createStatsContainer() {
        HBox statsContainer = new HBox(24);
        statsContainer.getStyleClass().add("stats-container");
        statsContainer.setAlignment(Pos.CENTER);

        // Statistiques différentes selon le rôle
        var user = controller.getCurrentUser();

        VBox stat1 = createQuickStat("📚", "3", "Cours aujourd'hui");
        VBox stat2 = createQuickStat("🏢", "2", "Salles réservées");
        VBox stat3 = createQuickStat("💻", "1", "Matériel emprunté");

        if (user.isResponsable()) {
            VBox stat4 = createQuickStat("👥", String.valueOf(controller.getEnseignants().size()), "Enseignants");
            statsContainer.getChildren().addAll(stat1, stat2, stat3, stat4);
        } else {
            VBox stat4 = createQuickStat("⏰", "18h", "Heures semaine");
            statsContainer.getChildren().addAll(stat1, stat2, stat3, stat4);
        }

        return statsContainer;
    }

    /**
     * Crée une statistique rapide
     */
    private VBox createQuickStat(String icon, String value, String label) {
        VBox stat = new VBox(16);
        stat.getStyleClass().add("quick-stats");
        stat.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-number");

        Label descLabel = new Label(label);
        descLabel.getStyleClass().add("stat-label");
        descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        stat.getChildren().addAll(iconLabel, valueLabel, descLabel);

        return stat;
    }

    /**
     * Met à jour la zone de contenu
     */
    private void setContentArea(Node content) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(content);
    }

    /**
     * Affiche le formulaire de connexion
     */
    private void showLoginForm() {
        VBox loginForm = new VBox(24);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(48));
        loginForm.getStyleClass().add("stat-card");
        loginForm.setMaxWidth(480);

        Label titleLabel = new Label("🔐 Connexion");
        titleLabel.getStyleClass().addAll("section-title", "text-center");
        titleLabel.setStyle("-fx-font-size: 28px;");

        Label subtitleLabel = new Label("Accédez à votre espace personnel");
        subtitleLabel.getStyleClass().addAll("section-description", "text-center");

        // Champs de saisie
        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        usernameField.getStyleClass().add("text-field");
        usernameField.setMaxWidth(320);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.getStyleClass().add("text-field");
        passwordField.setMaxWidth(320);

        // Zone d'aide
        Label helpLabel = new Label("💡 Comptes de test :\n" +
                "• responsable / pass (Responsable)\n" +
                "• enseignant / pass (Enseignant)\n" +
                "• admin / admin (Responsable)");
        helpLabel.getStyleClass().addAll("text-muted", "text-center");
        helpLabel.setStyle("-fx-font-size: 12px; -fx-background-color: var(--surface-variant); " +
                "-fx-padding: 16px; -fx-background-radius: 8px;");

        // Boutons
        Button loginButton = new Button("Se connecter");
        loginButton.getStyleClass().add("primary-button");

        Button cancelButton = new Button("Annuler");
        cancelButton.getStyleClass().add("secondary-button");

        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, cancelButton);

        loginForm.getChildren().addAll(
                titleLabel, subtitleLabel,
                usernameField, passwordField, helpLabel,
                buttonBox
        );

        // Actions
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            controller.login(username, password);
        });

        cancelButton.setOnAction(e -> controller.changeView(ViewType.DASHBOARD));

        passwordField.setOnAction(e -> loginButton.fire());

        setContentArea(loginForm);
        usernameField.requestFocus();
    }

    /**
     * Déconnecte l'utilisateur
     */
    private void logout() {
        boolean confirm = ModernNotificationUtils.showConfirmation(
                "Déconnexion",
                "Voulez-vous vraiment vous déconnecter ?"
        );

        if (confirm) {
            controller.logout();
        }
    }

    /**
     * Configure les styles CSS et charge la police Poppins
     */
    private void setupStyles(Scene scene) {
        ModernUtils.initUI(scene);

        // Charger CSS
        boolean cssLoaded = ModernUtils.loadCSS(scene, "/org/personnal/gestmat/styles/modernStyle.css");
        if (!cssLoaded) {
            System.out.println("📝 Échec du chargement de modernStyle.css, styles de base appliqués");
        } else {
            System.out.println("✅ CSS chargé : /org/personnal/gestmat/styles/modernStyle.css");
        }

        // Charger explicitement la police
        Font loaded = Font.loadFont(getClass().getResource("/fonts/Poppins-SemiBold.ttf").toExternalForm(), 14);
        if (loaded == null) {
            System.out.println("⚠️ Échec du chargement de la police Poppins-Regular.ttf");
        } else {
            System.out.println("✅ Police chargée : " + loaded.getName());
        }

        // Test si utilisable
        Font poppins = Font.font("Poppins", 14);
        if (poppins.getFamily().equals("System")) {
            System.out.println("⚠️ Police Poppins non trouvée, utilisation de la police système");
        } else {
            System.out.println("✅ Police Poppins appliquée");
        }
    }

    // ===== IMPLÉMENTATION DES LISTENERS MVC =====

    @Override
    public void onLogin(HomeUIController.User user) {
        updateSidebar();
        showDashboard();
        setSelectedButton(btnPlanning); // Reset selection to dashboard
    }

    @Override
    public void onLogout() {
        updateSidebar();
        showDashboard();
        setSelectedButton(btnPlanning); // Reset selection
    }

    @Override
    public void onViewChanged(ViewType oldView, ViewType newView) {
        switch (newView) {
            case DASHBOARD -> {
                showDashboard();
                setSelectedButton(null); // Pas de bouton spécifique pour dashboard
            }
            case PLANNING -> {
                showPlanningView();
                setSelectedButton(btnPlanning);
            }
            case RESERVATION_SALLE -> {
                showReservationSalleView();
                setSelectedButton(btnReservationSalle);
            }
            case RESERVATION_MATERIEL -> {
                showReservationMaterielView();
                setSelectedButton(btnReservationMateriel);
            }
            case RECAP_HORAIRE -> {
                showRecapHoraireView();
                setSelectedButton(btnRecapHoraire);
            }
            case GESTION_ENSEIGNANTS -> {
                showGestionEnseignantsView();
                setSelectedButton(btnGestionEnseignants);
            }
        }
    }

    // ===== MAIN =====

    public static void main(String[] args) {
        launch(args);
    }
}