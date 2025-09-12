# cs314MongoTest
Workspace for developing a mongoDB for CSU-CS314

### PopulateMongoDB
PopulateMongoDB is a mvn project, make sure mvn is installed and run 'mvn clean install'

To run PopulateMongoDB, cd into directory and run 'mvn exec:java'

This app does NOT clean up the collection or documents when done.

### mongosh
The app uses (or creates if not there) the 'cs314' database. So when using the shell, make sure to 'use cs314' before anything else
  >mongosh
  >use cs314
  >have fun
