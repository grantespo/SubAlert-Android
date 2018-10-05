# SubAlert-Android

App Page: https://play.google.com/store/apps/details?id=gespanet.com.subscriptionreminder&hl=en_US

This is one of the more recent Android Apps I created (Published March 2018). It's basically a Subscription Reminder App. 

`MainActivity.java` checks to see if there is data in the `SQLite`, If there is data present, rows will be added to the appropriate listview (freetrialsListView/subscriptionsListView). Each listview has an add button. When the add button is clicked, a new activity is started: `AddRow.java`.

`AddRow.java`. basically prompts the user to enter appropriate information about their subscription. After The user clicks the Add Button, A new row is created in a `SQLite` table. Then all activies are terminated and a new activity is started: `MainActivity.java`

For any present row in a listview, there is a remove button. When the remove button is clicked on a specific row, the data in that `SQLite` row will be deleted, thus the listview will be updated too.



