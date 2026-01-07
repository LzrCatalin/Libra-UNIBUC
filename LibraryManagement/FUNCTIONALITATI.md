# Sistem de Management Bibliotecă - Documentație Funcționalități

## Prezentare Generală
Sistemul de Management Bibliotecă este o aplicație API RESTful construită cu Spring Boot care gestionează operațiunile bibliotecii incluzând cărți, autori, membri, împrumuturi, rezervări și amenzi.

---

## 1. Gestionarea Cărților

### Operațiuni de Bază
- **Creare Carte**: Înregistrarea unei cărți noi în sistem cu titlu, descriere, an publicare, copii disponibile, categorie și autorii asociați
- **Actualizare Carte**: Modificarea detaliilor cărții și reatribuirea autorilor
- **Ștergere Carte**: Eliminarea unei cărți din sistem (se propagă la asocierile carte-autor)
- **Obținere Carte după ID**: Recuperarea informațiilor detaliate despre o carte specifică incluzând autorii săi
- **Obținere Toate Cărțile**: Listarea tuturor cărților din bibliotecă cu autorii lor

### Căutare și Filtrare
- **Căutare după Titlu**: Găsirea cărților după titlu folosind potrivire parțială case-insensitive
- **Filtrare după Categorie**: Recuperarea cărților aparținând unei categorii specifice (Ficțiune, Non-Ficțiune, Tehnică, etc.)
- **Găsire Cărți Disponibile**: Listarea doar a cărților cu copii disponibile pentru împrumut

### Reguli de Business
- Fiecare carte trebuie să aibă cel puțin un autor
- Relațiile carte-autor sunt validate înainte de creare/actualizare
- Copiile disponibile sunt gestionate automat în timpul operațiunilor de împrumut

---

## 2. Gestionarea Autorilor

### Operațiuni de Bază
- **Creare Autor**: Adăugarea unui autor nou cu prenume, nume, biografie, dată naștere și naționalitate
- **Actualizare Autor**: Modificarea informațiilor autorului
- **Ștergere Autor**: Eliminarea unui autor din sistem
- **Obținere Autor după ID**: Recuperarea informațiilor detaliate despre un autor specific
- **Obținere Toți Autorii**: Listarea tuturor autorilor din sistem

### Căutare și Relații
- **Căutare după Nume**: Găsirea autorilor după prenume sau nume folosind potrivire parțială case-insensitive
- **Obținere Cărți după Autor**: Recuperarea tuturor cărților scrise de un autor specific

---

## 3. Gestionarea Membrilor

### Operațiuni de Bază
- **Înregistrare Membru**: Crearea unui nou membru al bibliotecii cu detalii personale, tip abonament și status
- **Actualizare Membru**: Modificarea informațiilor membrului incluzând tipul de abonament și status
- **Ștergere Membru**: Eliminarea unui membru din sistem
- **Obținere Membru după ID**: Recuperarea informațiilor detaliate despre un membru specific
- **Obținere Toți Membrii**: Listarea tuturor membrilor înregistrați

### Căutare și Filtrare
- **Căutare după Nume**: Găsirea membrilor după nume folosind potrivire parțială case-insensitive
- **Căutare după Email**: Găsirea membrilor după email folosind potrivire parțială case-insensitive
- **Filtrare după Tip Abonament**: Listarea membrilor după tip (Standard, Premium, Student)
- **Filtrare după Status**: Listarea membrilor după status (Activ, Suspendat, Expirat)

### Activitate Membru
- **Obținere Împrumuturi Membru**: Vizualizarea tuturor tranzacțiilor de împrumut pentru un membru specific
- **Obținere Rezervări Membru**: Vizualizarea tuturor rezervărilor de cărți pentru un membru specific
- **Obținere Amenzi Membru**: Vizualizarea tuturor amenzilor asociate cu un membru specific

---

## 4. Gestionarea Împrumuturilor

### Operațiuni de Bază
- **Creare Împrumut**: Emiterea unei cărți către un membru cu calcul automat al datei scadente (14 zile)
- **Actualizare Împrumut**: Modificarea detaliilor împrumutului (date, status)
- **Ștergere Împrumut**: Eliminarea unei înregistrări de împrumut din sistem
- **Obținere Împrumut după ID**: Recuperarea informațiilor detaliate despre un împrumut specific
- **Obținere Toate Împrumuturile**: Listarea tuturor tranzacțiilor de împrumut

### Acțiuni Împrumut
- **Returnare Împrumut**: Procesarea returnării cărții, actualizarea statusului și incrementarea copiilor disponibile
- **Reînnoire Împrumut**: Extinderea duratei împrumutului cu 14 zile (doar pentru împrumuturi active)

### Căutare și Filtrare
- **Filtrare după Status**: Listarea împrumuturilor după status (Activ, Returnat, Întârziat)
- **Obținere Împrumuturi după Membru**: Vizualizarea tuturor împrumuturilor pentru un membru specific
- **Obținere Împrumuturi după Carte**: Vizualizarea istoricului de împrumut pentru o carte specifică
- **Numărare Împrumuturi Active**: Obținerea numărului de împrumuturi active pentru un membru

### Reguli de Business
- Cărțile trebuie să aibă copii disponibile pentru a fi împrumutate
- Copiile disponibile sunt decrementate când un împrumut este creat
- Copiile disponibile sunt incrementate când un împrumut este returnat
- Doar împrumuturile active pot fi reînnoite
- Împrumuturile nu pot fi returnate de două ori

---

## 5. Gestionarea Rezervărilor

### Operațiuni de Bază
- **Creare Rezervare**: Rezervarea unei cărți care este în prezent indisponibilă cu dată de expirare automată (7 zile)
- **Actualizare Rezervare**: Modificarea detaliilor rezervării
- **Ștergere Rezervare**: Eliminarea unei rezervări din sistem
- **Obținere Rezervare după ID**: Recuperarea informațiilor detaliate despre o rezervare specifică
- **Obținere Toate Rezervările**: Listarea tuturor rezervărilor

### Acțiuni Rezervare
- **Anulare Rezervare**: Anularea unei rezervări în așteptare
- **Onorare Rezervare**: Marcarea unei rezervări ca onorată când cartea devine disponibilă
- **Expirare Rezervare**: Marcarea unei rezervări ca expirată când nu este onorată la timp

### Căutare și Filtrare
- **Filtrare după Status**: Listarea rezervărilor după status (În așteptare, Onorată, Anulată, Expirată)
- **Obținere Rezervări după Membru**: Vizualizarea tuturor rezervărilor pentru un membru specific
- **Obținere Rezervări după Carte**: Vizualizarea cozii de rezervări pentru o carte specifică
- **Numărare Rezervări Active**: Obținerea numărului de rezervări în așteptare pentru un membru

### Reguli de Business
- Rezervările pot fi create doar pentru cărți indisponibile (0 copii disponibile)
- Doar rezervările în așteptare pot fi anulate, onorate sau expirate
- Rezervările expiră automat după 7 zile

---

## 6. Gestionarea Amenzilor

### Operațiuni de Bază
- **Creare Amendă**: Crearea manuală a unei amenzi pentru un împrumut cu sumă și motiv personalizat
- **Actualizare Amendă**: Modificarea detaliilor amenzii (sumă, motiv, status)
- **Ștergere Amendă**: Eliminarea unei înregistrări de amendă din sistem
- **Obținere Amendă după ID**: Recuperarea informațiilor detaliate despre o amendă specifică
- **Obținere Toate Amenzile**: Listarea tuturor amenzilor din sistem

### Acțiuni Amendă
- **Plată Amendă**: Marcarea unei amenzi ca plătită cu dată automată de plată
- **Anulare Amendă**: Iertarea unei amenzi (schimbarea statusului la anulată)

### Generare Automată Amenzi
- **Generare Amendă pentru Împrumut**: Calcularea și crearea automată a unei amenzi pentru un împrumut întârziat (0.50 pe zi)
- **Generare Amenzi Întârziate**: Generarea în masă a amenzilor pentru toate împrumuturile active întârziate

### Căutare și Filtrare
- **Filtrare după Status**: Listarea amenzilor după status (În așteptare, Plătită, Anulată)
- **Obținere Amenzi după Membru**: Vizualizarea tuturor amenzilor pentru un membru specific
- **Obținere Amendă după Împrumut**: Recuperarea amenzii asociate cu un împrumut specific
- **Obținere Amenzi Neplătite**: Listarea tuturor amenzilor în așteptare

### Rapoarte Financiare
- **Total Neplătit după Membru**: Calcularea sumei totale a amenzilor restante pentru un membru
- **Total Plătit după Membru**: Calcularea sumei totale a amenzilor plătite pentru un membru
- **Total Încasat**: Obținerea sumei totale a tuturor amenzilor plătite la nivel de sistem
- **Total Restant**: Obținerea sumei totale a tuturor amenzilor în așteptare la nivel de sistem

### Reguli de Business
- Doar o amendă poate exista pe împrumut
- Amenzile sunt calculate automat ca: zile întârziere × 0.50
- Doar amenzile în așteptare pot fi plătite sau anulate
- Amenzile pot fi generate doar pentru împrumuturi active întârziate

---

## Caracteristici Tehnice

### Bază de Date
- Bază de date PostgreSQL cu migrări Liquibase
- Relații many-to-many gestionate prin tabele de joncțiune

---

## Sumar Endpoint-uri API

| Entitate | Cale de Bază | Operațiuni Cheie |
|----------|--------------|------------------|
| Cărți | `/books` | CRUD, căutare, filtrare după categorie, găsire disponibile |
| Autori | `/authors` | CRUD, căutare, obținere cărți după autor |
| Membri | `/members` | CRUD, căutare, filtrare, obținere împrumuturi/rezervări/amenzi |
| Împrumuturi | `/loans` | CRUD, returnare, reînnoire, filtrare după status/membru/carte |
| Rezervări | `/reservations` | CRUD, anulare, onorare, expirare, filtrare după status |
| Amenzi | `/fines` | CRUD, plată, anulare, generare, rapoarte financiare |
