
In the real world of device to device process automation communication,
a dialogue or conversation between devices occurs systematically,
in many cases, over different types of communication networks and in different languages.
Depending on the process automation equipment manufacturer,
a very specific or proprietary language is used
or may be a language that is commonly open to the industry.
It is these open protocols many manufacturers adapt
to easily integrate their products in a market.
An “Open protocol” means the specifications are published
and may be used by anyone freely or by license.
Open protocols are usually backed by a combination of corporations,
user groups, professional societies, and governments.
This provides users with a much wider choice of devices or systems
that can be utilized to meet specific applications.
Advantages of open protocols include support by multiple manufacturers,
software vendors, and install/service organizations,
active community groups for support,
the ability to stay current and add capabilities in the future.
One of the most common automation communication protocols
of connecting industrial electronic devices used today is Modbus.
The Modbus communication protocol is the oldest
and by far the most popular automation protocol
in the field of process automation and SCADA
(Supervisory Control and Data Acquisition).
Knowing how to create Modbus based networks
is essential for any electrical technician
and engineer working in these occupation fields.
Being able to integrate devices from different manufacturers
is a skill that is in demand
and will ultimately make you more valuable and marketable in the industry.
Modbus is a communications protocol
published by Modicon in 1979
for use with its programmable logic controllers (PLCs).
Modicon is now owned by Schneider Electric.
Modbus provides common language
for devices and equipment to communicate with one another.
For example, Modbus enables devices on a system
that measures temperature and humidity connected on the same network
to communicate the results to a supervisory computer or PLC.
And the development and update of Modbus protocols
have been managed by the Modbus Organization.
The Modbus Organization is an association of users and suppliers
of Modbus-compliant devices.
Several versions of the Modbus protocol exist for the serial port
and Ethernet and the most common are Modbus RTU,
Modbus ASCII, Modbus TCP and Modbus Plus.
Modicon published the Modbus communication interface
for a multidrop network based on a master/slave architecture.
Communication between the Modbus nodes is achieved
with send request and read response type messages.
Modbus is an open standard that describes
the messaging communication dialog.
Modbus communicates over several types of physical media
such as serial RS-232, RS-485, RS-422 and over Ethernet.
The physical media will be selected at the time when you purchase the devices.
The original Modbus interface ran on RS-232 serial communication,
but most of the later Modbus implementations use RS-485
because it allowed longer distances,
higher speeds and the possibility of multiple devices on a single multi-drop network.
Master/Slave Modbus communication over serial RS-485 physical media
showing two-wire transmit and receive connections.
On simple interfaces like RS-485 and RS-232,
the Modbus messages are sent in plain form over the network
and the network will be dedicated to only Modbus communication.
However, if your network requires multiple heterogeneous devices
using a more versatile network system like TCP/IP over ethernet,
the Modbus messages are embedded in Ethernet packets
with the format prescribed for this physical interface.
So in this case, Modbus and other types of mixed protocols
can co-exist at the same physical interface at the same time.
The main Modbus message structure is peer-to-peer.
Modbus is able to function on both point to point and multidrop networks.
Modbus devices communicate using a master/slave (client-server for Ethernet) technique
in which only one device can initiate transactions (called queries).
The other devices respond by supplying the requested data to the master,
or by taking the action requested in the query.
A slave is any peripheral device such as an I/O transducer, valve, network drive,
or other measuring types of devices which processes information
and sends its response message to the master using Modbus.
Masters can address individual slaves
or initiate a broadcast message to all slaves.
Slaves return a response to all message queries addressed to them individually,
but do not respond to broadcast messages.
Slaves do not initiate messages on their own
and only respond to message queries transmitted from the master.
The master’s query will consist of a slave address (broadcast address),
a function code with a read or write data command to the slave,
along with the write command data
if a write command was initiated by the master,
and an error checking field.
The error checking is a value the master or slave creates
at the beginning of the transmission or response
and then checked when the message is received
to verify the contents are correct.
A slave’s response consists of fields confirming it received the request,
the data to be returned, and an error checking data.
If no error occurs, the slave’s response contains the data as requested.
If an error occurs in the message query received by the slave,
or if the slave is unable to perform the action requested,
the slave will return an exception message as its response.
The error check field of the slave’s message frame
allows the master to confirm that the contents of the message are valid.




Στον πραγματικό κόσμο της επικοινωνίας αυτοματισμού διεργασιών από συσκευή σε συσκευή,
ένας διάλογος ή μια συνομιλία μεταξύ συσκευών λαμβάνει χώρα συστηματικά,
σε πολλές περιπτώσεις, μέσω διαφορετικών τύπων δικτύων επικοινωνίας και σε διαφορετικές γλώσσες.
Ανάλογα με τον κατασκευαστή του εξοπλισμού αυτοματισμού διεργασιών,
χρησιμοποιείται μια πολύ συγκεκριμένη ή ιδιόκτητη γλώσσα
ή μπορεί να είναι μια γλώσσα που είναι κοινώς ανοικτή στον κλάδο.
Είναι αυτά τα ανοικτά πρωτόκολλα που πολλοί κατασκευαστές προσαρμόζουν
για να ενσωματώσουν εύκολα τα προϊόντα τους σε μια αγορά.
Ένα "ανοικτό πρωτόκολλο" σημαίνει ότι οι προδιαγραφές δημοσιεύονται
και μπορούν να χρησιμοποιηθούν από οποιονδήποτε ελεύθερα ή με άδεια χρήσης.
Τα ανοικτά πρωτόκολλα υποστηρίζονται συνήθως από έναν συνδυασμό εταιρειών,
ομάδων χρηστών, επαγγελματικών εταιρειών και κυβερνήσεων.
Αυτό παρέχει στους χρήστες μια πολύ ευρύτερη επιλογή συσκευών ή συστημάτων
που μπορούν να χρησιμοποιηθούν για την κάλυψη συγκεκριμένων εφαρμογών.
Τα πλεονεκτήματα των ανοικτών πρωτοκόλλων περιλαμβάνουν την υποστήριξη από πολλούς κατασκευαστές,
πωλητές λογισμικού και οργανισμούς εγκατάστασης/υπηρεσιών,
ενεργές ομάδες της κοινότητας για υποστήριξη,
τη δυνατότητα να παραμένουν επίκαιρα και να προσθέτουν δυνατότητες στο μέλλον.
Ένα από τα πιο κοινά πρωτόκολλα επικοινωνίας αυτοματισμού
σύνδεσης βιομηχανικών ηλεκτρονικών συσκευών που χρησιμοποιούνται σήμερα είναι το Modbus.

Το πρωτόκολλο επικοινωνίας Modbus είναι το παλαιότερο
και μακράν το πιο δημοφιλές πρωτόκολλο αυτοματισμού.
στον τομέα του αυτοματισμού διαδικασιών και του SCADA
(εποπτικός έλεγχος και απόκτηση δεδομένων).
Γνωρίζοντας πώς να δημιουργείτε δίκτυα βασισμένα στο Modbus
είναι απαραίτητο για κάθε ηλεκτρολόγο τεχνικό
και μηχανικό που εργάζεται σε αυτούς τους επαγγελματικούς τομείς.
Η ικανότητα ενσωμάτωσης συσκευών από διαφορετικούς κατασκευαστές
είναι μια δεξιότητα που έχει μεγάλη ζήτηση
και τελικά θα σας κάνει πιο πολύτιμους και εμπορεύσιμους στον κλάδο.
Το Modbus είναι ένα πρωτόκολλο επικοινωνίας
που δημοσιεύθηκε από τη Modicon το 1979
για χρήση με τους προγραμματιζόμενους λογικούς ελεγκτές της (PLC).
Η Modicon ανήκει πλέον στη Schneider Electric.
Το Modbus παρέχει κοινή γλώσσα
για συσκευές και εξοπλισμό που επικοινωνούν μεταξύ τους.
Για παράδειγμα, το Modbus επιτρέπει στις συσκευές ενός συστήματος
που μετράει τη θερμοκρασία και την υγρασία συνδεδεμένες στο ίδιο δίκτυο
να επικοινωνούν τα αποτελέσματα σε έναν υπολογιστή εποπτείας ή σε ένα PLC.
Και η ανάπτυξη και η ενημέρωση των πρωτοκόλλων Modbus
διαχειρίζεται ο οργανισμός Modbus.
Ο Οργανισμός Modbus είναι μια ένωση χρηστών και προμηθευτών
των συσκευών που είναι συμβατές με το Modbus.
Υπάρχουν διάφορες εκδόσεις του πρωτοκόλλου Modbus για τη σειριακή θύρα
και το Ethernet και οι πιο συνηθισμένες είναι το Modbus RTU,
Modbus ASCII, Modbus TCP και Modbus Plus.
Η Modicon δημοσίευσε τη διεπαφή επικοινωνίας Modbus
για ένα δίκτυο πολλαπλών διαδρομών που βασίζεται σε μια αρχιτεκτονική master/slave.
Η επικοινωνία μεταξύ των κόμβων Modbus επιτυγχάνεται
με μηνύματα τύπου αίτησης αποστολής και απόκρισης ανάγνωσης.
Το Modbus είναι ένα ανοικτό πρότυπο που περιγράφει
τον διάλογο επικοινωνίας μηνυμάτων.
Το Modbus επικοινωνεί μέσω διαφόρων τύπων φυσικών μέσων
όπως σειριακά RS-232, RS-485, RS-422 και μέσω Ethernet.
Το φυσικό μέσο θα επιλεγεί κατά την αγορά των συσκευών.
Η αρχική διεπαφή Modbus λειτουργούσε με σειριακή επικοινωνία RS-232,αλλά οι περισσότερες από τις μεταγενέστερες υλοποιήσεις Modbus χρησιμοποιούν RS-485
επειδή επέτρεπε μεγαλύτερες αποστάσεις, υψηλότερες ταχύτητες και τη δυνατότητα πολλαπλών συσκευών σε ένα ενιαίο δίκτυο πολλαπλής διοχέτευσης.
Επικοινωνία Master/Slave Modbus μέσω σειριακού φυσικού μέσου RS-485
που δείχνει συνδέσεις εκπομπής και λήψης δύο καλωδίων.
Σε απλές διεπαφές όπως οι RS-485 και RS-232,
τα μηνύματα Modbus αποστέλλονται σε απλή μορφή μέσω του δικτύου
και το δίκτυο θα είναι αφιερωμένο μόνο στην επικοινωνία Modbus.
Ωστόσο, εάν το δίκτυό σας απαιτεί πολλαπλές ετερογενείς συσκευές
χρησιμοποιείτε ένα πιο ευέλικτο σύστημα δικτύου, όπως το TCP/IP μέσω ethernet,
τα μηνύματα Modbus ενσωματώνονται σε πακέτα Ethernet
με τη μορφή που προβλέπεται για αυτή τη φυσική διεπαφή.
Έτσι, σε αυτή την περίπτωση, το Modbus και άλλοι τύποι μικτών πρωτοκόλλων
μπορούν να συνυπάρχουν ταυτόχρονα στην ίδια φυσική διεπαφή.
Η κύρια δομή μηνυμάτων Modbus είναι ομότιμη.
Το Modbus είναι σε θέση να λειτουργεί τόσο σε δίκτυα σημείο προς σημείο όσο και σε δίκτυα πολλαπλών διαδρομών.
Οι συσκευές Modbus επικοινωνούν με την τεχνική master/slave (client-server για το Ethernet).
στην οποία μόνο μία συσκευή μπορεί να ξεκινήσει συναλλαγές (που ονομάζονται ερωτήματα).
Οι άλλες συσκευές απαντούν παρέχοντας τα ζητούμενα δεδομένα στον κύριο,
ή εκτελώντας την ενέργεια που ζητείται στο ερώτημα.
Slave είναι οποιαδήποτε περιφερειακή συσκευή, όπως ένας μετατροπέας εισόδου/εξόδου, μια βαλβίδα, μια μονάδα δικτύου,
ή άλλοι τύποι συσκευών μέτρησης που επεξεργάζονται πληροφορίες
και αποστέλλει το μήνυμα απάντησής της στον κύριο χρησιμοποιώντας το Modbus.
Οι κύριοι μπορούν να απευθύνονται σε μεμονωμένους σκλάβους
ή να εκκινήσουν ένα μήνυμα εκπομπής προς όλους τους σκλάβους.
Οι σκλάβοι επιστρέφουν απάντηση σε όλα τα ερωτήματα μηνυμάτων που απευθύνονται σε αυτούς ξεχωριστά,
αλλά δεν απαντούν σε μηνύματα εκπομπής.
Οι σκλάβοι δεν ξεκινούν από μόνοι τους μηνύματα.
και απαντούν μόνο σε ερωτήματα μηνυμάτων που διαβιβάζονται από τον κύριο.
Το ερώτημα του master θα αποτελείται από μια διεύθυνση slave (διεύθυνση εκπομπής),
έναν κωδικό λειτουργίας με εντολή ανάγνωσης ή εγγραφής δεδομένων προς τον σκλάβο,
μαζί με τα δεδομένα της εντολής εγγραφής
εάν η εντολή εγγραφής ξεκίνησε από τον κύριο,
και ένα πεδίο ελέγχου σφαλμάτων.
Ο έλεγχος σφάλματος είναι μια τιμή που δημιουργεί ο κύριος ή ο σκλάβος
στην αρχή της μετάδοσης ή της απάντησης
και στη συνέχεια ελέγχεται κατά τη λήψη του μηνύματος
για να επαληθεύσει ότι το περιεχόμενο είναι σωστό.
Η απάντηση ενός slave αποτελείται από πεδία που επιβεβαιώνουν ότι έλαβε το αίτημα,
τα δεδομένα που πρέπει να επιστραφούν, και ένα δεδομένο ελέγχου σφάλματος.
Εάν δεν προκύψει σφάλμα, η απάντηση του slave περιέχει τα δεδομένα όπως ζητήθηκαν.
Εάν παρουσιαστεί σφάλμα στο ερώτημα μηνύματος που έλαβε ο σκλάβος,
ή αν η σκλάβα δεν είναι σε θέση να εκτελέσει την ενέργεια που ζητήθηκε,
η σκλάβα θα επιστρέψει ένα μήνυμα εξαίρεσης ως απάντησή της.
Το πεδίο ελέγχου σφάλματος του πλαισίου μηνύματος της δούλης
επιτρέπει στον κύριο να επιβεβαιώσει ότι τα περιεχόμενα του μηνύματος είναι έγκυρα.