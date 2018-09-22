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
      const promises=[];
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