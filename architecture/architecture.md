# Architecture

:heavy_check_mark:_(COMMENT) Add a description of the architecture of your application and create a diagram like the one below. Link to the diagram in this document._

![Architecture from Mehmet Karakoc](https://github.com/user-attachments/assets/574ee605-c3a6-4b0e-a4d7-7cbc0768620a)

## **Gedetailleerde Use Cases en Communicatie**

### **US1: Posts aanmaken**
- **Beschrijving**: Als redacteur wil ik nieuwe posts aanmaken, zodat ik nieuws en updates kan delen.
- **Communicatie**: **Synchronisch**
- **Microservice**: `PostService`
- **Reden**: De redacteur moet een directe bevestiging krijgen dat de post succesvol is aangemaakt.

---

### **US2: Posts opslaan als concept**
- **Beschrijving**: Als redacteur wil ik posts opslaan als concept om later verder te werken.
- **Communicatie**: **Synchronisch**
- **Microservice**: `PostService`
- **Reden**: Directe bevestiging nodig zodat de redacteur weet dat het concept is opgeslagen.

---

### **US3: Posts bewerken**
- **Beschrijving**: Als redacteur wil ik de inhoud van een post kunnen bewerken.
- **Communicatie**: **Synchronisch**
- **Microservice**: `PostService`
- **Reden**: Bewerkingen moeten direct worden bevestigd.

---

### **US4: Overzicht van gepubliceerde posts bekijken**
- **Beschrijving**: Als gebruiker wil ik een overzicht van gepubliceerde posts zien.
- **Communicatie**: **Synchronisch**
- **Microservice**: `PostService`
- **Reden**: Gebruikers willen direct toegang tot de laatste nieuwsberichten.

---

### **US5: Posts filteren op basis van inhoud, auteur, datum**
- **Beschrijving**: Gebruikers willen posts filteren op inhoud, auteur en datum.
- **Communicatie**: **Synchronisch**
- **Microservice**: `PostService`
- **Reden**: Gebruikers verwachten real-time filtering.

---

### **US7: Ingediende posts goedkeuren of afwijzen**
- **Beschrijving**: Als redacteur wil ik ingediende posts goedkeuren of afwijzen.
- **Communicatie**: **Mixed**
  - **Synchronisch**: Directe goedkeuring of afwijzing.
  - **Asynchronisch**: Update van status en notificaties via message bus.
- **Microservices**: `ReviewService`, `PostService`

---

### **US8: Melding ontvangen bij goedkeuring/afwijzing**
- **Beschrijving**: Als redacteur wil ik een melding ontvangen wanneer een post wordt goedgekeurd of afgewezen.
- **Communicatie**: **Asynchronisch**
- **Microservice**: `NotificationService`
- **Reden**: Notificaties kunnen in de achtergrond worden afgehandeld.

---

### **US9: Opmerkingen toevoegen bij afwijzing**
- **Beschrijving**: Redacteurs kunnen opmerkingen toevoegen bij afwijzing.
- **Communicatie**: **Synchronisch**
- **Microservice**: `ReviewService`
- **Reden**: Directe feedback aan de auteur vereist.

---

### **US10: Reactie plaatsen op een post**
- **Beschrijving**: Gebruikers willen reacties kunnen plaatsen.
- **Communicatie**: **Mixed**
  - **Synchronisch**: Reacties worden direct toegevoegd.
  - **Asynchronisch**: Notificaties naar andere gebruikers via message bus.
- **Microservices**: `CommentService`, `NotificationService`

---

### **US11: Reacties lezen**
- **Beschrijving**: Als gebruiker wil ik reacties van anderen kunnen lezen.
- **Communicatie**: **Synchronisch**
- **Microservice**: `CommentService`
- **Reden**: Reacties moeten direct beschikbaar zijn.

---

### **US12: Reacties bewerken of verwijderen**
- **Beschrijving**: Gebruikers kunnen hun eigen reacties bewerken of verwijderen.
- **Communicatie**: **Mixed**
  - **Synchronisch**: Wijzigingen moeten direct worden doorgevoerd.
  - **Asynchronisch**: Notificaties naar andere gebruikers bij updates.
- **Microservices**: `CommentService`, `NotificationService`
