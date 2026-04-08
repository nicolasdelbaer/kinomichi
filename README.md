# Projet Atelier Technifutur

## Introduction

- Développement Java 
- Application console de gestion de stages de Kinomichi (art martial)

## Consignes
Je suis organisateur de stages internationaux de Kinomichi. J'ai besoin d'une application simple pour gérer mes stages, les inscriptions des participants et suivre mon budget.
C'est moi seul qui gère tout : j'encode les participants, je construis le programme, je suis les inscriptions.

### Ce que je veux pouvoir faire
Gérer mes stages: Un stage a un intitulé et se déroule sur plusieurs jours. Les jours comportent des créneaux horaires. Chaque créneau dure en général 1h30 et est animé par un formateur.
Pour un samedi soir, je propose en option un souper et/ou un hébergement.

Gérer mes participants: Chaque personne a un nom, prénom, téléphone, email et un club d'appartenance. Ce sont soit des participants classiques, soit des formateurs.

Gérer les inscriptions: Un participant s'inscrit à un stage et choisit ses créneaux selon ce qui lui est accessible. Je veux voir en un coup d'œil qui est inscrit à quoi. Je dois pouvoir encoder les inscriptions et gérer les participations le jour de l'évenement.

Gérer les tarifs: Les participants peuvent avoir des tarifs différents selon leur catégorie et le type d'animation.

### Objectifs & Contraintes
- Application monoposte côté gestion : un seul organisateur encode et administre.
- Renforcement des bases, experimentation
- Développement sans base de données
- Java vanilla, sans framework

## Post Mortem
### En bonus
Suivi de budget -> Avoir une vue claire des finances liées à chaque stage ou participants.

### Apprentissage
- Utilisation des streams
- Mise en place de patterns
- Utilisation de jar, la gestion du menu est un projet séparé

### Patterns et techniques utilisées
- Factory
- Builder
- Composite
- Observer Pattern
- Génériques
- Adapter / Proxy
- MVC basique
- Composants type renderer pour rendu de vues
- Héritage, interface, records, DTO
- Passage de référence dans lambdas
- Serializable

### Faiblesses
- Modèle MVC "old school", trop de couplage, aller vers une structure hexagonale.
- Trop de responsabilité sur les controllers, la vue devrait interroger et non se faire piloter.
- Communication difficile entre les controllers.
- Besoin d'utilisation de services dédiés pour une communication et flexibilité accrue.
- Besoin de unit testing.
- Les enums sont trop rigides pour le projet au niveau du type de participants.
- Manque de commentaires
