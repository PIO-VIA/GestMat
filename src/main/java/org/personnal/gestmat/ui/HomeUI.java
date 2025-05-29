package org.personnal.gestmat.ui;

import org.personnal.gestmat.ui.utils.ModernUtils;
import org.personnal.gestmat.ui.utils.ModernNotificationUtils;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeUI extends Application {
    
    // État de connexion
    private boolean isLoggedIn = false;
    private String currentUser = null;
    
    // Composants de l'interface
    private VBox sidebar;
    private VBox content;
    private Button btnConsultation;
    private Button btnReservationSalle;
    private Button btnReservationMateriel;
    private Button btnConnexion;
    private Button btnDeconnexion;
    private Label userLabel;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        
        // Créer le header
        HBox header = ModernUtils.createHeader("Accueil", "Consultez ou réservez vos salles et matériels");
        
        // Initialiser les composants
        initializeComponents();
        
        // Créer la sidebar
        createSidebar();
        
        // Créer le contenu initial
        createInitialContent();
        
        // Layout principal
        BorderPane layout = new BorderPane();
        layout.setTop(header);
        layout.setLeft(sidebar);
        layout.setCenter(content);
        
        Scene scene = new Scene(layout, 1100, 700);
        ModernUtils.initUI(scene);
        
        // Correction du chemin CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/org/personnal/gestmat/styles/modernStyle.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Erreur de chargement du CSS: " + e.getMessage());
            // Chemin alternatif si le premier ne fonctionne pas
            try {
                scene.getStylesheets().add(getClass().getResource("/resources/org/personnal/gestmat/styles/modernStyle.css").toExternalForm());
            } catch (Exception e2) {
                System.err.println("CSS non trouvé dans les ressources alternatives");
            }
        }
        
        stage.setTitle("Application de réservation de salle et de matériel");
        stage.setScene(scene);
        stage.show();
    }
    
    private void initializeComponents() {
        // Boutons de navigation (initialement invisibles)
        btnConsultation = ModernUtils.createNavButton("📅 Consultation planning");
        btnReservationSalle = ModernUtils.createNavButton("🏢 Réserver une salle");
        btnReservationMateriel = ModernUtils.createNavButton("💻 Réserver un matériel");
        
        // Événements des boutons de navigation
        btnConsultation.setOnAction(e -> showConsultationView());
        btnReservationSalle.setOnAction(e -> showReservationSalleView());
        btnReservationMateriel.setOnAction(e -> showReservationMaterielView());
        
        // Boutons de connexion/déconnexion
        btnConnexion = ModernUtils.createPrimaryButton("🔐 Se connecter");
        btnDeconnexion = ModernUtils.createNavButton("🚪 Se déconnecter");
        
        btnConnexion.setOnAction(e -> showLoginForm());
        btnDeconnexion.setOnAction(e -> logout());
        
        // Label utilisateur
        userLabel = new Label();
        userLabel.getStyleClass().add("user-label");
        userLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");
        
        // Masquer seulement les éléments de réservation au départ
        // Consultation reste toujours visible
        hideNavigationButtons();
    }
    
    private void createSidebar() {
        sidebar = new VBox();
        sidebar.getStyleClass().addAll("sidebar", "sidebar-scroll");
        sidebar.setPadding(new Insets(10));
        sidebar.setSpacing(8);
        
        updateSidebar();
    }
    
    private void updateSidebar() {
        sidebar.getChildren().clear();
        
        if (isLoggedIn) {
            // Utilisateur connecté - afficher le nom et toutes les options
            sidebar.getChildren().addAll(
                userLabel,
                ModernUtils.createSeparator(),
                btnConsultation,
                btnReservationSalle,
                btnReservationMateriel,
                ModernUtils.createSeparator(),
                btnDeconnexion
            );
        } else {
            // Utilisateur non connecté - afficher consultation + connexion
            Label welcomeLabel = new Label("Bienvenue !");
            welcomeLabel.getStyleClass().add("section-title");
            welcomeLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 16px;");
            
            Label instructionLabel = new Label("Consultez le planning ou connectez-vous pour réserver");
            instructionLabel.getStyleClass().add("section-description");
            instructionLabel.setWrapText(true);
            instructionLabel.setMaxWidth(180);
            
            sidebar.getChildren().addAll(
                welcomeLabel,
                instructionLabel,
                ModernUtils.createSeparator(),
                btnConsultation,  // Consultation toujours visible
                ModernUtils.createSeparator(),
                btnConnexion
            );
        }
    }
    
    private void createInitialContent() {
        content = ModernUtils.createContentArea(
            ModernUtils.createCard("Système de Réservation", 
                "Bienvenue dans l'application de réservation de salles et matériels.\n\n" +
                "✅ Consultation du planning : Accessible sans connexion\n" +
                "🔐 Réservations : Connexion requise pour réserver salles et matériels")
        );
    }
    
    private void showLoginForm() {
        VBox loginForm = new VBox();
        loginForm.setSpacing(15);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setPadding(new Insets(30));
        loginForm.getStyleClass().add("stat-card");
        loginForm.setMaxWidth(400);
        
        Label titleLabel = new Label("Connexion");
        titleLabel.getStyleClass().add("section-title");
        titleLabel.setStyle("-fx-font-size: 24px;");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        usernameField.getStyleClass().add("text-field");
        usernameField.setMaxWidth(300);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.getStyleClass().add("text-field");
        passwordField.setMaxWidth(300);
        
        Button loginButton = ModernUtils.createPrimaryButton("Se connecter");
        Button cancelButton = new Button("Annuler");
        cancelButton.getStyleClass().add("cancel-button");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, cancelButton);
        
        loginForm.getChildren().addAll(
            titleLabel,
            new Label("Entrez vos identifiants pour accéder à l'application"),
            usernameField,
            passwordField,
            buttonBox
        );
        
        // Gestion des événements
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            
            if (validateLogin(username, password)) {
                login(username);
                ModernNotificationUtils.showSuccessToast("Connexion réussie !");
            } else {
                ModernNotificationUtils.showErrorToast("Identifiants incorrects !");
                usernameField.clear();
                passwordField.clear();
                usernameField.requestFocus();
            }
        });
        
        cancelButton.setOnAction(e -> createInitialContent());
        
        // Permettre la connexion avec Entrée
        passwordField.setOnAction(e -> loginButton.fire());
        
        // Mettre à jour le contenu
        content.getChildren().clear();
        content.getChildren().add(loginForm);
    }
    
    private boolean validateLogin(String username, String password) {
        // Validation simple (à remplacer par une vraie authentification)
        return !username.isEmpty() && !password.isEmpty() && 
               (username.equals("admin") && password.equals("admin") ||
                username.equals("user") && password.equals("password"));
    }
    
    private void login(String username) {
        isLoggedIn = true;
        currentUser = username;
        userLabel.setText("👤 " + username);
        
        showNavigationButtons();
        updateSidebar();
        showDashboard();
    }
    
    private void logout() {
        boolean confirm = ModernNotificationUtils.showConfirmation(
            "Déconnexion", 
            "Êtes-vous sûr de vouloir vous déconnecter ?"
        );
        
        if (confirm) {
            isLoggedIn = false;
            currentUser = null;
            
            hideNavigationButtons();
            updateSidebar();
            createInitialContent();
            
            ModernNotificationUtils.showInfoToast("Vous avez été déconnecté");
        }
    }
    
    private void showDashboard() {
        VBox dashboard = new VBox();
        dashboard.setSpacing(20);
        
        Label welcomeTitle = new Label("Bienvenue, " + currentUser + " !");
        welcomeTitle.getStyleClass().add("section-title");
        welcomeTitle.setStyle("-fx-font-size: 20px;");
        
        // Statistiques ou informations rapides
        HBox statsBox = new HBox(20);
        statsBox.getChildren().addAll(
            ModernUtils.createCard("Réservations du jour", "3 salles réservées"),
            ModernUtils.createCard("Matériel disponible", "12 équipements libres"),
            ModernUtils.createCard("Planning", "2 créneaux disponibles")
        );
        
        VBox quickActions = new VBox(10);
        Label actionsTitle = new Label("Actions rapides");
        actionsTitle.getStyleClass().add("section-title");
        
        Button quickReservation = ModernUtils.createPrimaryButton("Réservation rapide");
        quickReservation.setOnAction(e -> showReservationSalleView());
        
        quickActions.getChildren().addAll(actionsTitle, quickReservation);
        
        dashboard.getChildren().addAll(welcomeTitle, statsBox, quickActions);
        
        content.getChildren().clear();
        content.getChildren().add(dashboard);
    }
    
    private void showConsultationView() {
        String consultationContent;
        if (isLoggedIn) {
            consultationContent = "Consultation Planning - " + currentUser + "\n\n" +
                "Ici vous pourrez consulter le planning des réservations.\n\n" +
                "• Voir toutes les réservations\n" +
                "• Filtrer par date/salle/matériel\n" +
                "• Exporter le planning\n\n" +
                "Fonctionnalité à implémenter...";
        } else {
            consultationContent = "Consultation Planning (Mode Public)\n\n" +
                "Vous pouvez consulter le planning des réservations sans connexion.\n\n" +
                "• Voir les créneaux occupés/libres\n" +
                "• Consulter la disponibilité des salles\n" +
                "• Vérifier la disponibilité du matériel\n\n" +
                "💡 Connectez-vous pour effectuer des réservations\n\n" +
                "Fonctionnalité à implémenter...";
        }
        
        content.getChildren().clear();
        content.getChildren().add(
            ModernUtils.createCard("Consultation Planning", consultationContent)
        );
        ModernNotificationUtils.showInfoToast("Vue consultation sélectionnée");
    }
    
    private void showReservationSalleView() {
        content.getChildren().clear();
        content.getChildren().add(
            ModernUtils.createCard("Réservation de Salle", 
                "Ici vous pourrez réserver une salle.\n\n" +
                "Fonctionnalité à implémenter...")
        );
        ModernNotificationUtils.showInfoToast("Réservation de salle sélectionnée");
    }
    
    private void showReservationMaterielView() {
        content.getChildren().clear();
        content.getChildren().add(
            ModernUtils.createCard("Réservation de Matériel", 
                "Ici vous pourrez réserver du matériel.\n\n" +
                "Fonctionnalité à implémenter...")
        );
        ModernNotificationUtils.showInfoToast("Réservation de matériel sélectionnée");
    }
    
    private void hideNavigationButtons() {
        // Consultation reste toujours visible - ne masquer que les réservations
        btnReservationSalle.setVisible(false);
        btnReservationMateriel.setVisible(false);
        btnDeconnexion.setVisible(false);
    }
    
    private void showNavigationButtons() {
        // Consultation était déjà visible - afficher seulement les réservations
        btnReservationSalle.setVisible(true);
        btnReservationMateriel.setVisible(true);
        btnDeconnexion.setVisible(true);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}