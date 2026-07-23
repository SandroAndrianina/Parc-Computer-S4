# Récap technique — Refonte design "ParcInfo"

Ce doc résume les parties de code les plus délicates de la refonte, pour que tu
puisses t'y retrouver seul si tu dois modifier quelque chose plus tard.

---

## 1. Structure globale à retenir

```
/
├── css/theme.css          ← TOUT le style custom est ici (un seul fichier)
├── images/bg-gradient.jpg ← fond utilisé en background fixe
├── js/load-sidebar.js     ← injecte la sidebar dans les pages PHP
├── partials/
│   ├── head-libs.jsp      ← CDN communs (Bootswatch, FontAwesome, fonts)
│   ├── sidebar.jsp        ← sidebar pour les pages JSP
│   ├── sidebar.html       ← même sidebar, version statique pour les pages PHP
│   └── topbar.jsp         ← barre du haut pour les pages JSP
├── login.jsp, list.jsp, form.jsp, list_modele.jsp, form_modele.jsp
└── dashboard.php, historique.php, insertion_etat.php
```

**Règle d'or** : les pages JSP incluent `partials/sidebar.jsp` et `partials/topbar.jsp`
avec `<%@ include %>` (compilé une fois avec la page). Les pages PHP, elles,
n'ont pas de vrai moteur d'include côté serveur → elles chargent
`partials/sidebar.html` **en JavaScript** via `load-sidebar.js`. Si tu ajoutes
un lien dans la sidebar, **il faut le dupliquer dans les deux fichiers**
(`sidebar.jsp` ET `sidebar.html`) — c'est le seul endroit à ne pas oublier.

---

## 2. Le point le plus piégeux : `sidebar.jsp` (détection du lien actif)

```jsp
<%
    String uri = request.getRequestURI();
    java.util.function.Function<String,String> act =
        (key) -> uri.contains(key) ? "active" : "";
%>
...
<a class="nav-link <%= act.apply("/dashboard") %>" href="dashboard.php">
```

- `act` est une petite fonction Java (lambda) créée à la volée pour éviter de
  répéter `uri.contains(...) ? "active" : ""` sur chaque lien.
- **Piège** : `/list` et `/list_modele` contiennent tous les deux la chaîne
  `"list"`. C'est pour ça que le lien "Liste des ordinateurs" n'utilise pas
  `act.apply("/list")` mais un test plus strict :
  `uri.endsWith("/list")`. Si tu ajoutes une nouvelle route dont le nom est
  inclus dans un autre (ex. `/form` vs `/form_modele`), il faudra le même genre
  de test précis, sinon les deux liens s'allument en même temps.

---

## 3. `topbar.jsp` — titre de page dynamique

```jsp
<%
    String pageTitle = (String) request.getAttribute("pageTitle");
    ...
%>
```

Chaque page JSP doit faire, **avant** l'include de la topbar :

```jsp
<% request.setAttribute("pageTitle", "Mon titre"); %>
```

Si tu oublies cette ligne dans une nouvelle page, la topbar affichera juste
"Bonjour !" par défaut — ce n'est pas un bug, c'est le comportement de repli
prévu dans le code (`if (pageTitle == null) pageTitle = "Bonjour !";`).

---

## 4. Le CSS "glass" — comment ça marche concrètement

Dans `theme.css`, le fond d'écran est posé une seule fois, en globalité, via
un pseudo-élément :

```css
body::before {
  content: "";
  position: fixed;
  inset: 0;
  z-index: -1;
  background: url("../images/bg-gradient.jpg") center center / cover no-repeat;
}
```

- Le chemin `../images/...` est relatif au fichier **CSS** (`css/theme.css`),
  pas à la page HTML qui l'utilise. Si un jour tu déplaces `theme.css` dans un
  sous-dossier, il faudra corriger ce chemin.
- L'effet "verre" vient uniquement de `backdrop-filter: blur(...)` combiné à un
  fond semi-transparent (`rgba(255,255,255,0.55)`). C'est appliqué sur chaque
  carte individuellement (`.card-surface`, `.kpi-card`, `.app-sidebar`, etc.) —
  il n'y a pas de calque global unique, donc si tu crées une nouvelle carte,
  pense à lui donner la classe `.glass` (utilitaire déjà prêt dans le fichier)
  ou à copier le bloc `background / backdrop-filter / border` d'une carte
  existante.
- **Limite connue** : `backdrop-filter` ne marche pas sur les très vieux
  navigateurs (Firefox avant 2022 environ). Sans incidence pour un projet
  étudiant, mais bon à savoir si un jour on te pose la question en soutenance.

---

## 5. `insertion_etat.php` — la logique de validation de date

C'est la page la plus "logique" de tout le lot :

- Au chargement, elle récupère **tous** les ordinateurs (`?action=ordinateurs`)
  et stocke, pour chacun, la date de son dernier changement d'état dans
  l'objet JS `dernieresDates` (clé = id ordinateur).
- Quand tu changes l'ordinateur sélectionné dans le `<select>`, la fonction
  `mettreAJourMinDate()` met à jour l'attribut `min` du champ date, pour
  empêcher de saisir une date **antérieure** au dernier changement connu.
- À la soumission, il y a une **double validation** :
  1. côté JS (confort utilisateur, message immédiat sans recharger la page),
  2. côté serveur (implicite, dans ton servlet — c'est la vraie protection,
     ne jamais faire confiance uniquement au JS).
- Si tu changes le format de date retourné par le serveur (`derniereDate`),
  assure-toi qu'il reste comparable en tant que chaîne `"YYYY-MM-DD"` — la
  comparaison `date < derniereDate` est une comparaison de **texte**, pas de
  vraies dates, donc ça ne marche que si le format est strictement `YYYY-MM-DD`.

---

## 6. Le sujet des accents JSP (rappel du diagnostic)

Pas encore corrigé dans ce lot de fichiers — à vérifier de ton côté :

- Les fichiers PHP affichent bien les accents, les JSP non → indique un
  problème d'**encodage du fichier source** `.jsp` (souvent enregistré en
  ISO-8859-1 par l'éditeur) plutôt qu'un problème Tomcat.
- Vérifie dans ton IDE l'encodage réel de sauvegarde des `.jsp`, et assure-toi
  que `pageEncoding="UTF-8"` est bien la toute première ligne du fichier.

---

## Conseils pour ne pas te perdre

1. **Un seul fichier CSS à ouvrir** : tout le style custom vit dans
   `css/theme.css`. Les anciens fichiers (`main.css`, `list.css`, etc.) ne sont
   plus utilisés — supprime-les pour éviter de modifier le mauvais fichier par
   erreur.
2. **Teste une page à la fois** après avoir remplacé les fichiers : commence
   par `login.jsp` (le plus simple, aucune dépendance), puis `list.jsp`, puis
   le reste.
3. **Si un lien de la sidebar ne s'allume pas comme attendu**, le réflexe est
   toujours le même : regarder `uri.contains(...)` dans `sidebar.jsp` et
   vérifier qu'aucune autre route ne contient la même sous-chaîne.
4. **Si le fond ou l'effet verre ne s'affiche pas**, vérifie dans l'ordre :
   le chemin de `images/bg-gradient.jpg`, puis que `theme.css` est bien chargé
   *après* le CSS Bootswatch (sinon Bootswatch écrase tes styles).
5. Garde ce fichier à côté de toi pendant que tu recolles le nouveau design à
   tes servlets existants — il couvre les seuls endroits où il y a une vraie
   logique (pas juste du HTML/CSS).
