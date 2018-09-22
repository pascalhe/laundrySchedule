const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

exports.createUser = functions.firestore
    .document('users/{userId}')
    .onCreate((snap, context) => {
      const user = snap.data();
      var userObject = {
          uid: snap.id,
          displayName : user.userName,
          password: "password",
          email : user.email
      };

      return admin.auth().createUser(userObject)
        .then(function(userRecord) {
          // See the UserRecord reference doc for the contents of userRecord.
          console.log("Successfully created new user:", userRecord.uid);
          
        })
        .catch(function(error) {
          console.log("Error creating new user:", error);
        });
    });

exports.updateUser = functions.firestore
    .document('users/{userId}')
    .onUpdate((change, context) => {
      const newValue = change.after.data();
      const previousValue = change.before.data();
      const uid = change.before.id;

      if (previousValue.userName != newValue.userName){
        return admin.auth().updateUser(uid, {
          displayName: newValue.userName
        })
          .then(function(userRecord) {
            // See the UserRecord reference doc for the contents of userRecord.
            console.log("Successfully updated user", userRecord.toJSON());
          })
          .catch(function(error) {
            console.log("Error updating user:", error);
          });
     }
      return null;
    });