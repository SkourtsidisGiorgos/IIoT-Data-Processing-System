# Project goals

* Προς το παρόν δεν έχω καταφέρει να διαβάσω απο το ModbusPal
* Τι είδους μετρήσεις θέλουμε να γράφουμε (πχ θερμοκρασία, υγρασία, ένταση ηλ. ρεύματος) -> θα εχουμε θερμοκρασια, ισχύς, πίεση.
* Τι αρχιτεκτονική θα πρέπει να έχει το modbus?
πχ

holding registers 1-10 -> θερμοκρασία
holding registers 11-20 -> πίεση
etc

* Tι επεξεργασία θέλουμε στις μετρήσεις? Πχ aggregation, μέσους όρους? -> ανάλογα με τον τυπο μέτρησης. Θερμοκρασία, πίεση μέσος όρος, ισχύς=άθροισμα.
* Graphana: απεικονίζουμε τα δεδομένα (query influx every X second) ή κάτι ακόμα? -> χρονοσειρές, sums aggregations
* Αποθηκεύω late events στο Kafka ή σε βάση? -> table late events, και δείξε και στο graphana
* Το κείμενο τι θα περιέχει?
  Θεωρία
  - βιομηχανικά πρωτόκολλα
  - Kafka ΟΛΑ
  - Spring Boot περιληπτικά
  - Κafka Streams, ΚΤables, KStreams
  - Influx DB -> retention policy -> send to hdfs(parquet)
  - Redis.
  - Web Sockets in Graphana Live, read from Kafka and send to Graphana livestreaming, and reverse (from graphana to kafka)
  Παρουσίαση της εφαρμογής:
  - Περιγραφή και εικόνα
  - Βοηθητικό code snippet και εξήγηση
  - Πλεονεκτήματα υλοποίησης μέ χρήση spring boot
