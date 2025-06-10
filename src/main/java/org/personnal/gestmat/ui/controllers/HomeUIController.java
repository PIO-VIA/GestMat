package org.personnal.gestmat.ui.controllers;

import org.personnal.gestmat.ui.utils.ModernNotificationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Contrôleur MVC pour l'interface d'accueil
 * Gère la logique métier et les interactions utilisateur
 */
public class HomeUIController {

    // ===== ÉNUMÉRATIONS =====

    /**
     * Types de rôles dans l'application
     */
    public enum UserRole {
        ENSEIGNANT("Enseignant", "👨‍🏫"),
        RESPONSABLE("Responsable", "👨‍💼");

        private final String displayName;
        private final String icon;

        UserRole(String displayName, String icon) {
            this.displayName = displayName;
            this.icon = icon;
        }

        public String getDisplayName() { return displayName; }
        public String getIcon() { return icon; }

        @Override
        public String toString() { return icon + " " + displayName; }
    }

    /**
     * Types de vues disponibles
     */
    public enum ViewType {
        DASHBOARD,
        PLANNING,
        RESERVATION_SALLE,
        RESERVATION_MATERIEL,
        RECAP_HORAIRE,
        GESTION_ENSEIGNANTS
    }

    // ===== MODÈLES DE DONNÉES =====

    /**
     * Modèle représentant un utilisateur
     */
    public static class User {
        private final String username;
        private final String fullName;
        private final UserRole role;
        private final String email;

        public User(String username, String fullName, UserRole role, String email) {
            this.username = username;
            this.fullName = fullName;
            this.role = role;
            this.email = email;
        }

        public String getUsername() { return username; }
        public String getFullName() { return fullName; }
        public UserRole getRole() { return role; }
        public String getEmail() { return email; }

        public boolean isResponsable() { return role == UserRole.RESPONSABLE; }
        public boolean isEnseignant() { return role == UserRole.ENSEIGNANT; }
    }

    /**
     * Modèle représentant une salle
     */
    public static class Salle {
        private final String id;
        private final String nom;
        private final int capacite;
        private final String type;

        public Salle(String id, String nom, int capacite, String type) {
            this.id = id;
            this.nom = nom;
            this.capacite = capacite;
            this.type = type;
        }

        public String getId() { return id; }
        public String getNom() { return nom; }
        public int getCapacite() { return capacite; }
        public String getType() { return type; }

        @Override
        public String toString() { return nom + " (" + capacite + " places)"; }
    }

    /**
     * Modèle représentant une semaine
     */
    public static class Semaine {
        private final LocalDate debut;
        private final LocalDate fin;
        private final int numeroSemaine;

        public Semaine(LocalDate debut) {
            this.debut = debut;
            this.fin = debut.plusDays(6);
            this.numeroSemaine = debut.get(WeekFields.of(Locale.FRANCE).weekOfYear());
        }

        public LocalDate getDebut() { return debut; }
        public LocalDate getFin() { return fin; }
        public int getNumeroSemaine() { return numeroSemaine; }

        @Override
        public String toString() {
            return String.format("Semaine %d (%s - %s)",
                    numeroSemaine,
                    debut.format(DateTimeFormatter.ofPattern("dd/MM")),
                    fin.format(DateTimeFormatter.ofPattern("dd/MM"))
            );
        }
    }

    /**
     * Modèle représentant un créneau horaire
     */
    public static class CreneauHoraire {
        private final String heureDebut;
        private final String heureFin;
        private final boolean occupe;
        private final String cours;
        private final String professeur;

        public CreneauHoraire(String heureDebut, String heureFin, boolean occupe, String cours, String professeur) {
            this.heureDebut = heureDebut;
            this.heureFin = heureFin;
            this.occupe = occupe;
            this.cours = cours;
            this.professeur = professeur;
        }

        public String getHeureDebut() { return heureDebut; }
        public String getHeureFin() { return heureFin; }
        public boolean isOccupe() { return occupe; }
        public String getCours() { return cours; }
        public String getProfesseur() { return professeur; }

        public String getHoraire() { return heureDebut + "\n" + heureFin; }

        public String getStatutTexte() {
            return occupe ? "⚫ Cours réservé" : "⚪ Disponible";
        }

        public String getDetailsTexte() {
            if (occupe && cours != null && professeur != null) {
                return cours + " - " + professeur;
            }
            return "Aucune réservation";
        }
    }

    // ===== ÉTAT DU CONTRÔLEUR =====

    private User currentUser;
    private ViewType currentView;
    private final List<User> enseignants;
    private final List<Salle> salles;
    private final List<Semaine> semaines;

    // Listeners pour les changements d'état
    public interface AuthenticationListener {
        void onLogin(User user);
        void onLogout();
    }

    public interface ViewChangeListener {
        void onViewChanged(ViewType oldView, ViewType newView);
    }

    private final List<AuthenticationListener> authListeners;
    private final List<ViewChangeListener> viewListeners;

    // ===== CONSTRUCTEUR =====

    public HomeUIController() {
        this.enseignants = new ArrayList<>();
        this.salles = new ArrayList<>();
        this.semaines = new ArrayList<>();
        this.authListeners = new ArrayList<>();
        this.viewListeners = new ArrayList<>();
        this.currentView = ViewType.DASHBOARD;

        initializeData();
    }

    // ===== MÉTHODES D'INITIALISATION =====

    /**
     * Initialise les données de test
     */
    private void initializeData() {
        // Initialiser les salles
        salles.add(new Salle("S001", "Amphithéâtre A", 150, "Amphithéâtre"));
        salles.add(new Salle("S002", "Salle 101", 30, "TD"));
        salles.add(new Salle("S003", "Salle 102", 30, "TD"));
        salles.add(new Salle("S004", "Labo Info 1", 25, "Laboratoire"));
        salles.add(new Salle("S005", "Labo Info 2", 25, "Laboratoire"));
        salles.add(new Salle("S006", "Salle Visio", 20, "Visioconférence"));

        // Initialiser les enseignants
        enseignants.add(new User("prof1", "Dr. Marie Dubois", UserRole.ENSEIGNANT, "marie.dubois@univ.fr"));
        enseignants.add(new User("prof2", "Prof. Jean Martin", UserRole.ENSEIGNANT, "jean.martin@univ.fr"));
        enseignants.add(new User("prof3", "Dr. Sophie Bernard", UserRole.ENSEIGNANT, "sophie.bernard@univ.fr"));
        enseignants.add(new User("resp1", "Prof. Pierre Durand", UserRole.RESPONSABLE, "pierre.durand@univ.fr"));

        // Initialiser les semaines (12 semaines à partir d'aujourd'hui)
        LocalDate today = LocalDate.now();
        LocalDate debutSemaine = today.with(WeekFields.of(Locale.FRANCE).dayOfWeek(), 1);

        for (int i = 0; i < 12; i++) {
            semaines.add(new Semaine(debutSemaine.plusWeeks(i)));
        }
    }

    // ===== MÉTHODES D'AUTHENTIFICATION =====

    /**
     * Valide les identifiants de connexion
     */
    public boolean validateLogin(String username, String password) {
        // Validation simple pour la démo
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return false;
        }

        // Identifiants de test
        return (username.equals("admin") && password.equals("admin")) ||
                (username.equals("responsable") && password.equals("pass")) ||
                (username.equals("enseignant") && password.equals("pass"));
    }

    /**
     * Connecte un utilisateur
     */
    public void login(String username, String password) {
        if (!validateLogin(username, password)) {
            ModernNotificationUtils.showErrorToast("Identifiants incorrects");
            return;
        }

        // Créer l'utilisateur selon les identifiants
        User user = createUserFromCredentials(username);
        if (user != null) {
            this.currentUser = user;
            notifyAuthListeners(true);
            ModernNotificationUtils.showSuccessToast("Connexion réussie !");
        }
    }

    /**
     * Déconnecte l'utilisateur actuel
     */
    public void logout() {
        this.currentUser = null;
        this.currentView = ViewType.DASHBOARD;
        notifyAuthListeners(false);
        ModernNotificationUtils.showInfoToast("Déconnexion réussie");
    }

    /**
     * Crée un utilisateur à partir des identifiants
     */
    private User createUserFromCredentials(String username) {
        return switch (username) {
            case "admin", "responsable" ->
                    new User(username, "Admin Responsable", UserRole.RESPONSABLE, "admin@univ.fr");
            case "enseignant" ->
                    new User(username, "Prof. Enseignant", UserRole.ENSEIGNANT, "enseignant@univ.fr");
            default -> null;
        };
    }

    // ===== MÉTHODES DE NAVIGATION =====

    /**
     * Change la vue actuelle
     */
    public void changeView(ViewType newView) {
        ViewType oldView = this.currentView;

        // Vérifier les permissions
        if (!hasPermissionForView(newView)) {
            ModernNotificationUtils.showWarningToast("Accès non autorisé pour ce rôle");
            return;
        }

        this.currentView = newView;
        notifyViewListeners(oldView, newView);
    }

    /**
     * Vérifie si l'utilisateur a les permissions pour une vue
     */
    private boolean hasPermissionForView(ViewType view) {
        if (currentUser == null) {
            return view == ViewType.DASHBOARD || view == ViewType.PLANNING;
        }

        return switch (view) {
            case DASHBOARD, PLANNING, RESERVATION_SALLE, RESERVATION_MATERIEL, RECAP_HORAIRE -> true;
            case GESTION_ENSEIGNANTS -> currentUser.isResponsable();
        };
    }

    // ===== MÉTHODES DE DONNÉES =====

    /**
     * Retourne la liste des salles disponibles
     */
    public ObservableList<Salle> getSalles() {
        return FXCollections.observableArrayList(salles);
    }

    /**
     * Retourne la liste des semaines
     */
    public ObservableList<Semaine> getSemaines() {
        return FXCollections.observableArrayList(semaines);
    }

    /**
     * Retourne la liste des enseignants (pour les responsables)
     */
    public ObservableList<User> getEnseignants() {
        if (currentUser != null && currentUser.isResponsable()) {
            return FXCollections.observableArrayList(enseignants);
        }
        return FXCollections.emptyObservableList();
    }

    // ===== MÉTHODES DE PLANIFICATION =====

    /**
     * Génère les données de planning pour une salle et une semaine
     */
    private List<CreneauHoraire> generatePlanningData(Salle salle, Semaine semaine) {
        List<CreneauHoraire> creneaux = new ArrayList<>();

        // Créneaux horaires standards
        String[][] horaires = {
                {"08:00", "10:00"},
                {"10:15", "12:15"},
                {"13:30", "15:30"},
                {"15:45", "17:45"}
        };

        String[] cours = {"Mathématiques", "Physique", "Informatique", "Chimie", "Biologie", "Histoire"};
        String[] professeurs = {"Prof. Martin", "Dr. Dubois", "Prof. Bernard", "Dr. Durand", "Prof. Leclerc", "Prof. Rousseau"};

        for (String[] horaire : horaires) {
            // Simuler aléatoirement des créneaux occupés
            boolean occupe = Math.random() > 0.5;
            String coursName = null;
            String prof = null;

            if (occupe) {
                coursName = cours[(int)(Math.random() * cours.length)];
                prof = professeurs[(int)(Math.random() * professeurs.length)];
            }

            creneaux.add(new CreneauHoraire(horaire[0], horaire[1], occupe, coursName, prof));
        }

        return creneaux;
    }

    /**
     * Crée les cartes de planning avec horaires verticaux
     */
    /**
     * Crée les cartes de planning avec horaires verticaux - VERSION CORRIGÉE
     */
    public VBox createPlanningCards(Salle salle, Semaine semaine) {
        if (salle == null || semaine == null) {
            Label messageLabel = new Label("Sélectionnez une salle et une semaine pour voir le planning détaillé.");
            messageLabel.getStyleClass().add("section-description");
            messageLabel.setWrapText(true);
            messageLabel.setPadding(new Insets(16));
            messageLabel.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 20;");

            VBox container = new VBox();
            container.getChildren().add(messageLabel);
            return container;
        }

        VBox planningContainer = new VBox(20);
        planningContainer.getStyleClass().add("planning-container");

        // Générer tous les jours de la semaine (lundi à vendredi)
        String[] nomsJours = {"LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI"};
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.FRENCH);

        // Parcourir tous les jours de la semaine
        for (int i = 0; i < nomsJours.length; i++) {
            VBox daySection = new VBox(15);
            daySection.getStyleClass().add("planning-day-section");

            // Calculer la vraie date pour ce jour
            LocalDate dateJour = semaine.getDebut().plusDays(i);
            String jourFormate = nomsJours[i] + " " + dateJour.format(dateFormatter).toUpperCase();

            // Titre du jour
            Label dayTitle = new Label(jourFormate);
            dayTitle.getStyleClass().add("planning-day-title");
            daySection.getChildren().add(dayTitle);

            // Générer les créneaux pour ce jour
            List<CreneauHoraire> creneaux = generatePlanningData(salle, semaine);

            for (CreneauHoraire creneau : creneaux) {
                HBox timeRow = new HBox(15);
                timeRow.getStyleClass().add("planning-time-row");
                timeRow.setAlignment(Pos.CENTER_LEFT);

                // Label horaire (colonne fixe)
                VBox timeBox = new VBox(2);
                timeBox.setAlignment(Pos.CENTER);
                timeBox.setMinWidth(80);
                timeBox.setMaxWidth(80);

                Label startTime = new Label(creneau.getHeureDebut());
                Label endTime = new Label(creneau.getHeureFin());
                startTime.getStyleClass().add("planning-time-label");
                endTime.getStyleClass().add("planning-time-label");

                // Séparateur entre les heures
                Label separator = new Label("|");
                separator.getStyleClass().add("planning-time-label");
                separator.setStyle("-fx-font-size: 10px; -fx-text-fill: #999999;");

                timeBox.getChildren().addAll(startTime, separator, endTime);

                // Carte du créneau
                VBox timeSlot = new VBox(5);
                timeSlot.getStyleClass().add("planning-time-slot");

                // Appliquer la couleur selon l'état
                if (creneau.isOccupe()) {
                    timeSlot.getStyleClass().add("planning-time-slot-occupied"); // Bleu
                } else {
                    timeSlot.getStyleClass().add("planning-time-slot-free"); // Vert
                }

                // Contenu du créneau
                Label statusLabel = new Label(creneau.isOccupe() ? "⚫ Cours réservé" : "⚪ Disponible");
                statusLabel.getStyleClass().add("planning-status-label");
                if (creneau.isOccupe()) {
                    statusLabel.getStyleClass().add("planning-status-occupied");
                } else {
                    statusLabel.getStyleClass().add("planning-status-free");
                }

                Label courseLabel = new Label(creneau.getDetailsTexte());
                courseLabel.getStyleClass().add("planning-course-label");

                timeSlot.getChildren().addAll(statusLabel, courseLabel);

                // Permettre à la carte de prendre tout l'espace disponible
                HBox.setHgrow(timeSlot, Priority.ALWAYS);

                timeRow.getChildren().addAll(timeBox, timeSlot);
                daySection.getChildren().add(timeRow);
            }

            planningContainer.getChildren().add(daySection);
        }

        return planningContainer;
    }
    /**
     * Génère le contenu du planning pour une salle et une semaine (version texte - conservée pour compatibilité)
     */
    public String getPlanningContent(Salle salle, Semaine semaine) {
        if (salle == null || semaine == null) {
            return "Sélectionnez une salle et une semaine pour voir le planning.";
        }

        StringBuilder content = new StringBuilder();
        content.append("Planning de ").append(salle.getNom()).append("\n");
        content.append(semaine.toString()).append("\n\n");

        // Simuler des données de planning
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};
        String[] heures = {"08:00-10:00", "10:15-12:15", "13:30-15:30", "15:45-17:45"};

        for (String jour : jours) {
            content.append("📅 ").append(jour).append("\n");
            for (String heure : heures) {
                // Simuler aléatoirement des créneaux occupés
                if (Math.random() > 0.6) {
                    content.append("  ⚫ ").append(heure).append(" - Cours réservé\n");
                } else {
                    content.append("  ⚪ ").append(heure).append(" - Libre\n");
                }
            }
            content.append("\n");
        }

        return content.toString();
    }

    /**
     * Crée les contrôles de planning (ComboBox)
     */
    public Node createPlanningControls(Runnable onSelectionChange) {
        HBox controls = new HBox(16);
        controls.getStyleClass().add("planning-controls");
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(16));

        // Label pour la salle
        Label salleLabel = new Label("Salle :");
        salleLabel.getStyleClass().add("combo-label");

        // ComboBox pour les salles
        ComboBox<Salle> salleCombo = new ComboBox<>(getSalles());
        salleCombo.setPromptText("Sélectionner une salle");
        salleCombo.getStyleClass().add("combo-box-modern");
        salleCombo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(salleCombo, Priority.ALWAYS);

        // Label pour la semaine
        Label semaineLabel = new Label("Semaine :");
        semaineLabel.getStyleClass().add("combo-label");

        // ComboBox pour les semaines
        ComboBox<Semaine> semaineCombo = new ComboBox<>(getSemaines());
        semaineCombo.setPromptText("Sélectionner une semaine");
        semaineCombo.getStyleClass().add("combo-box-modern");
        semaineCombo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(semaineCombo, Priority.ALWAYS);

        // Définir la semaine actuelle par défaut
        if (!semaines.isEmpty()) {
            semaineCombo.setValue(semaines.get(0));
        }

        // Listeners pour les changements
        salleCombo.setOnAction(e -> {
            if (onSelectionChange != null) {
                onSelectionChange.run();
            }
        });

        semaineCombo.setOnAction(e -> {
            if (onSelectionChange != null) {
                onSelectionChange.run();
            }
        });

        // Stocker les références pour pouvoir les récupérer
        controls.getProperties().put("salleCombo", salleCombo);
        controls.getProperties().put("semaineCombo", semaineCombo);

        controls.getChildren().addAll(salleLabel, salleCombo, semaineLabel, semaineCombo);

        return controls;
    }

    /**
     * Récupère la salle sélectionnée depuis les contrôles
     */
    @SuppressWarnings("unchecked")
    public Salle getSelectedSalle(Node planningControls) {
        if (planningControls instanceof HBox hbox) {
            ComboBox<Salle> combo = (ComboBox<Salle>) hbox.getProperties().get("salleCombo");
            return combo != null ? combo.getValue() : null;
        }
        return null;
    }

    /**
     * Récupère la semaine sélectionnée depuis les contrôles
     */
    @SuppressWarnings("unchecked")
    public Semaine getSelectedSemaine(Node planningControls) {
        if (planningControls instanceof HBox hbox) {
            ComboBox<Semaine> combo = (ComboBox<Semaine>) hbox.getProperties().get("semaineCombo");
            return combo != null ? combo.getValue() : null;
        }
        return null;
    }

    // ===== MÉTHODES DE GÉNÉRATION DE CONTENU =====

    /**
     * Génère le contenu du dashboard selon le rôle
     */
    public String getDashboardContent() {
        if (currentUser == null) {
            return "Bienvenue dans le système de réservation !\n\n" +
                    "• Consultez le planning sans connexion\n" +
                    "• Connectez-vous pour accéder aux réservations";
        }

        StringBuilder content = new StringBuilder();
        content.append("Bienvenue, ").append(currentUser.getFullName()).append(" !\n\n");

        if (currentUser.isResponsable()) {
            content.append("🎯 Tableau de bord Responsable\n\n");
            content.append("Fonctionnalités disponibles :\n");
            content.append("• Gestion des réservations de salles et matériels\n");
            content.append("• Consultation et modification des récaps horaires\n");
            content.append("• Gestion des enseignants\n");
            content.append("• Supervision générale du planning\n\n");
            content.append("📊 Statistiques :\n");
            content.append("• ").append(enseignants.size()).append(" enseignants gérés\n");
            content.append("• ").append(salles.size()).append(" salles disponibles\n");
        } else {
            content.append("👨‍🏫 Espace Enseignant\n\n");
            content.append("Fonctionnalités disponibles :\n");
            content.append("• Réservation de salles\n");
            content.append("• Réservation de matériels\n");
            content.append("• Consultation de votre récap horaire\n");
            content.append("• Consultation du planning général\n");
        }

        return content.toString();
    }

    /**
     * Génère le contenu de la vue consultation planning
     */
    public String getConsultationContent() {
        StringBuilder content = new StringBuilder();

        if (currentUser != null) {
            content.append("Planning Général - ").append(currentUser.getRole().getDisplayName()).append("\n\n");
            /*content.append("🔍 Fonctionnalités avancées :\n");
            content.append("• Filtrage par salle et semaine\n");
            content.append("• Export du planning\n");
            content.append("• Notifications de disponibilité\n");*/
            if (currentUser.isResponsable()) {
                content.append("• Modification des réservations\n");
            }
        } else {
            content.append("Planning Général (Mode Public)\n\n");
            /*content.append("👀 Consultation libre :\n");
            content.append("• Visualisation des créneaux occupés/libres\n");
            content.append("• Vérification de la disponibilité des salles\n");
            content.append("• Informations sur les capacités\n\n");
            content.append("💡 Connectez-vous pour effectuer des réservations");*/
        }

        return content.toString();
    }

    // ===== GETTERS ET SETTERS =====

    public User getCurrentUser() { return currentUser; }
    public boolean isLoggedIn() { return currentUser != null; }
    public ViewType getCurrentView() { return currentView; }

    // ===== GESTION DES LISTENERS =====

    public void addAuthenticationListener(AuthenticationListener listener) {
        authListeners.add(listener);
    }

    public void removeAuthenticationListener(AuthenticationListener listener) {
        authListeners.remove(listener);
    }

    public void addViewChangeListener(ViewChangeListener listener) {
        viewListeners.add(listener);
    }

    public void removeViewChangeListener(ViewChangeListener listener) {
        viewListeners.remove(listener);
    }

    private void notifyAuthListeners(boolean login) {
        for (AuthenticationListener listener : authListeners) {
            if (login) {
                listener.onLogin(currentUser);
            } else {
                listener.onLogout();
            }
        }
    }

    private void notifyViewListeners(ViewType oldView, ViewType newView) {
        for (ViewChangeListener listener : viewListeners) {
            listener.onViewChanged(oldView, newView);
        }
    }
}