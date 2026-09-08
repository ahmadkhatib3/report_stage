# Rapport de stage — Figures HTML/CSS

Diagrammes de Gantt et schémas techniques générés nativement en HTML et CSS pour le rapport de stage IT réalisé chez **CMA CGM Tripoli**.

## Aperçu

Ce dépôt contient le code source de 5 figures utilisées dans le rapport :

1. **Diagramme de Gantt global** — déroulement du stage sur 8 semaines
2. **Diagramme de Gantt** — projet de déploiement des terminaux Getac UX10G2
3. **Schéma** — séquence de démarrage Windows en UEFI
4. **Schéma** — répartition des partitions du disque d'un terminal Getac
5. **Diagramme de Gantt** — projet de remplacement des onduleurs (UPS)

Chaque diagramme de Gantt est construit à partir d'éléments HTML positionnés en pourcentage (pas d'image ni de bibliothèque externe), et les deux schémas structurels sont réalisés en SVG natif. La mise en forme est entièrement gérée par une feuille de style externe.

## Structure du dépôt

```
.
├── figures.html   # Structure et contenu des 5 figures
├── figures.css    # Feuille de style (couleurs, mise en page, typographie)
└── README.md
```

## Utilisation

1. Cloner ou télécharger le dépôt en gardant les deux fichiers dans le même dossier (le lien vers la feuille de style est relatif).
2. Ouvrir `figures.html` dans un navigateur.

Aucune dépendance, aucune installation nécessaire — tout fonctionne en ouvrant simplement le fichier HTML.

## Personnalisation

Les couleurs sont centralisées en haut de `figures.css` sous forme de variables CSS :

```css
:root {
  --teal: #0f6e56;
  --coral: #993c1d;
  --amber: #854f0b;
  --purple: #3c3489;
  --gray: #888780;
  --red: #b23a48;
}
```

Pour ajuster une barre d'un diagramme de Gantt, modifier les propriétés `left` et `width` (en pourcentage) de l'élément `.gantt-bar` correspondant dans `figures.html`.

## Auteur

Ahmad — stage IT, CMA CGM Tripoli (2026)

