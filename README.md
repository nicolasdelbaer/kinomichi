## Structure du Menu

**“Que voulez-vous faire ?”**

1. Gérer les rassemblements

   *Afficher Liste des rassemblements → id . Nom du rassemblement + date début*

    1. Create new
    2. Update id
    3. Archiver id (pas de delete, on conserve l’info mais elle est cachée)
    4. Gérer les périodes

       Afficher les périodes

        1. Ajouter période
        2. Modifier période; id
        3. Archiver période; id
        4. Supprimer période; id (check pas d’inscription/participation “pending”)
    5. Réafficher liste des rassemblements
2. Gérer les participants
    1. Créer un participant
    2. Modifier un participant; id
    3. Archiver un participant; id
    4. Supprimer un participant; id (check pas d’inscription/participation “pending”)
3. Encoder une inscription ou participation

   *Afficher liste des périodes actives (définir) → id. Nom rassemblement + nom formatteur + date & plage horaire*

    1. Choisissez une période

       *Afficher Liste des participants ?*

        1. Inscrire; pId → Set statut as (REGISTERED)
        2. Désinscrire; pId → Set statut as (WITHDRAWN)
        3. A participé; pId → Set statut as (PENDING)
        4. A payé; pId → Set statut as (PAYED)
        5. N’a pas participé; pId → Set statut as (CANCELLED)
        6. Absent; pId → Set statut as (ABSENT)
        7. Réafficher liste des participants
4. Définir des tarifs

   *Afficher Liste des tarifs → Tableau Participant Type → Event Type : id. Price*

    1. Ajouter un tarif (id pt; id et; price)
    2. Updater un tarif
    3. Réafficher liste des tarifs
5. Consulter un rapport
    1. Liste des paiements en attente
    2. Rapport sur rassemblement